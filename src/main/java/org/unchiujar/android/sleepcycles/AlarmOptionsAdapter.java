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

public class AlarmOptionsAdapter extends ArrayAdapter<AlarmOption> {
    private final static Logger LOG = LoggerFactory.getLogger(AlarmOptionsAdapter.class);
    private final Context mContext;
    private final AlarmOption[] mValues;
    private final Util mUtil;
    private final String mStrNotificationAdded;
    private final String mStrForWakeTimeIn;
    private final String mStrLength;
    private final String mStrTime;
    private final String mStrCycles;

    public AlarmOptionsAdapter(Context context, AlarmOption[] values) {
        super(context, R.layout.list_element, values);
        this.mContext = context;
        this.mValues = values;
        mUtil = new Util(getContext());
        mStrNotificationAdded = mContext.getString(R.string.notification_added);
        mStrForWakeTimeIn = mContext.getString(R.string.for_wake_time_in);
        mStrLength = mContext.getString(R.string.length);
        mStrTime = mContext.getString(R.string.time);
        mStrCycles = mContext.getString(R.string.cycles);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alarmOptionView = inflater.inflate(R.layout.list_element, parent, false);
        TextView lengthView = (TextView) alarmOptionView.findViewById(R.id.timeView);
        TextView hourView = (TextView) alarmOptionView.findViewById(R.id.lengthView);

        Button alarmButton = (Button) alarmOptionView.findViewById(R.id.setAlarm);
        alarmButton.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                final long alarmTime = mValues[position].getBedTime().getTime();
                final long wakeTime = mValues[position].getSleepLength() * 60 * 1000;
                // TODO test code
                mUtil.setAlarm(alarmTime, wakeTime);
                final long notificationTime = alarmTime - Calendar.getInstance().getTimeInMillis();
                final long actualWakeTime = wakeTime + alarmTime - Calendar.getInstance().getTimeInMillis();
                Toast.makeText(
                        mContext,
                        mStrNotificationAdded + mUtil.formatTimeText(notificationTime) + mStrForWakeTimeIn
                                + mUtil.formatTimeText(actualWakeTime), Toast.LENGTH_LONG).show();
            }
        });
        lengthView.setText(mStrLength + mUtil.formatTimeText(mValues[position].getSleepLength()));
        int hours = mValues[position].getBedTime().getHours();
        int minutes = mValues[position].getBedTime().getMinutes();
        int cycles = mValues[position].getCycles();
        hourView.setText(mStrTime + mUtil.pad(hours) + ":" + mUtil.pad(minutes) + " | " + mStrCycles + cycles);

        return alarmOptionView;
    }
}
