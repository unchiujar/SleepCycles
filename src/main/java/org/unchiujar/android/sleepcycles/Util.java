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

import static org.unchiujar.android.sleepcycles.R.string.and;
import static org.unchiujar.android.sleepcycles.R.string.multipe_hours;
import static org.unchiujar.android.sleepcycles.R.string.multipe_minutes;
import static org.unchiujar.android.sleepcycles.R.string.one_hour;
import static org.unchiujar.android.sleepcycles.R.string.one_minute;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.widget.TextView;

public class Util {
    public static final String MINUTES_LEFT = "org.unchiujar.android.sleepcycles.sleepcycles.minutes";
    public static final String HOURS_LEFT = "org.unchiujar.android.sleepcycles.sleepcycles.hours";
    private static final Logger LOG = LoggerFactory.getLogger(Util.class);

    /**
     * Creates a {@link PendingIntent} with an alarm for a got to bed time.
     * 
     * @param alarmTime the time to go to bed
     * @param wakeTime the length of sleep
     * @param context the application context for which to create the
     *            PendingIntent
     * @return a PendingIntent with the alarm set
     */
    public PendingIntent setAlarm(long alarmTime, long wakeTime, Context context) {

        LOG.debug("Setting alarm for {} ", new Date(alarmTime));

        // calculate hours
        byte hours = (byte) (wakeTime / 1000 / 60 / 60);
        // calculate minutes
        byte minutes = (byte) ((wakeTime - hours * 60 * 60 * 1000) / 1000 / 60);
        Intent alarmService = new Intent(context, AlarmService.class);

        alarmService.putExtra(HOURS_LEFT, hours);
        alarmService.putExtra(MINUTES_LEFT, minutes);

        // TODO test code
        // alarmService.putExtra(HOURS_LEFT, (byte) 2);
        // alarmService.putExtra(MINUTES_LEFT, (byte) 2);

        PendingIntent pendingAlarmService = PendingIntent.getService(context, 0, alarmService, 0);

        AlarmManager alarms = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // set alarm
        alarms.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingAlarmService);
        return pendingAlarmService;
    }

    public void disableAlarm(PendingIntent alarmIntent) {
        throw new UnsupportedOperationException("Method not implemented");
    }

    /**
     * Utility method to set a font on any number of views.
     * 
     * @param type the font to be set
     * @param views the views to set the font on
     */
    public static void setFont(Typeface type, TextView... views) {
        for (TextView textView : views) {
            textView.setTypeface(type);
        }
    }

    /**
     * Convenience method to format the text displayed in the notification.
     * 
     * @param context context to get R.string
     * @param hoursLeft the hours left
     * @param minutesLeft the minutes left
     * @return a formatted string
     */
    public static String formatTimeText(Context context, byte hoursLeft, byte minutesLeft) {
        String message = "";

        if (hoursLeft > 0) {
            // add actual value
            message += hoursLeft + " ";

            // add qualifier
            message += hoursLeft > 1 ? context.getString(multipe_hours) : context
                    .getString(one_hour);

        }

        if (minutesLeft > 0) {
            // add "and" only if we have hours AND minutes to display
            message += hoursLeft > 0 ? " " + context.getString(and) + " " : "";

            // add actual value
            message += minutesLeft + " ";
            // add qualifier
            message += minutesLeft > 1 ? context.getString(multipe_minutes) : context
                    .getString(one_minute);
        }

        return message;
    }

    /**
     * Convenience method to format the text displayed in the notification.
     * 
     * @param context context to get R.string
     * @param minutes the minutes left
     * @return a formatted string
     */
    public static String formatTimeText(Context context, int minutes) {
        byte hoursLeft = (byte) (minutes / 60);
        byte minutesLeft = (byte) (minutes - hoursLeft * 60);
        return formatTimeText(context, hoursLeft, minutesLeft);
    }

    /**
     * Convenience method to format the text displayed in the notification.
     * 
     * @param context context to get R.string
     * @param millis milliseconds left
     * @return a formatted string
     */
    public static String formatTimeText(Context context, long millis) {
        return formatTimeText(context, (int) (millis / 1000 / 60));
    }
}
