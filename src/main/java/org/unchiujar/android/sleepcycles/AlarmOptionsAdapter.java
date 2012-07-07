/*******************************************************************************
 **    Author: Vasile Jureschi <vasile.jureschi@gmail.com>
 **
 **    This file is part of SleepCycles.
 **
 **    SleepCycles is free software: you can redistribute it and/or modify
 **    it under the terms of the GNU General Public License as published by
 **    the Free Software Foundation, either version 3 of the License, or
 **    (at your option) any later version.
 **
 **    SleepCycles is distributed in the hope that it will be useful,
 **    but WITHOUT ANY WARRANTY; without even the implied warranty of
 **    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 **    GNU General Public License for more details.
 **
 **    You should have received a copy of the GNU General Public License
 **    along with SleepCycles.  If not, see <http://www.gnu.org/licenses/>.
 **
 *******************************************************************************/

package org.unchiujar.android.sleepcycles;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.inject.Inject;

public class AlarmOptionsAdapter extends ArrayAdapter<AlarmOption> {
    private final static Logger LOG = LoggerFactory.getLogger(AlarmOptionsAdapter.class);
    private final Context context;
    private final AlarmOption[] values;
    @Inject
    private Util util;

    public AlarmOptionsAdapter(Context context, AlarmOption[] values) {
        super(context, R.layout.list_element, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alarmOptionView = inflater.inflate(R.layout.list_element, parent, false);
        TextView lengthView = (TextView) alarmOptionView.findViewById(R.id.timeView);
        TextView hourView = (TextView) alarmOptionView.findViewById(R.id.lengthView);

        Button alarmButton = (Button) alarmOptionView.findViewById(R.id.setAlarm);
        alarmButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                long alarmTime = values[position].getBedTime().getTime();
                long wakeTime = values[position].getSleepLength() * 60 * 1000;
                // TODO test code
                util.setAlarm(alarmTime, wakeTime);

                Toast.makeText(
                        context,
                        "Notification added for "
                                + util.formatTimeText(alarmTime - Calendar.getInstance()
                                        .getTimeInMillis())
                                + " for wake time in "
                                + util.formatTimeText(wakeTime +
                                        alarmTime - Calendar.getInstance().getTimeInMillis()),
                        Toast.LENGTH_LONG).show();
            }
        });
        lengthView.setText("Length "
                + util.formatTimeText(values[position].getSleepLength()));
        hourView.setText("Time " + values[position].getBedTime().getHours() + ":"
                + values[position].getBedTime().getMinutes()
                + " | Cycles " + values[position].getCycles());
        return alarmOptionView;

    }
}
