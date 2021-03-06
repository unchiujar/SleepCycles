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

import static org.unchiujar.android.sleepcycles.R.string.bedtime_notification_text;
import static org.unchiujar.android.sleepcycles.R.string.bedtime_notification_ticker;
import static org.unchiujar.android.sleepcycles.R.string.bedtime_notification_title;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.inject.InjectResource;
import roboguice.service.RoboService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.IBinder;

import com.google.inject.Inject;

public class AlarmService extends RoboService {
    private static final int ALARM_ID = 2132131;
    private static final Logger LOG = LoggerFactory.getLogger(AlarmService.class);

    @Inject private Util util;

    @InjectResource(bedtime_notification_title) private String mStrBedtimeNotifTitle;
    @InjectResource(bedtime_notification_text) private String mStrBedtimeNotifText;
    @InjectResource(bedtime_notification_ticker) private String mStrBedtimeNotifTicker;
    @Inject private NotificationManager mNotificationManager;

    @Override
    public void onCreate() {
        LOG.debug("Alarm triggered.");
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        LOG.debug("Bound  {}", intent);
        byte hoursLeft = intent.getByteExtra(Util.HOURS_LEFT, (byte) 0);
        byte minutesLeft = intent.getByteExtra(Util.MINUTES_LEFT, (byte) 0);
        LOG.debug("Hours left {} minutes left {} ", hoursLeft, minutesLeft);

        // display a notification now
        Notification notification = new Notification(R.drawable.ic_launcher, mStrBedtimeNotifTicker,
                System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, AlarmService.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        notification.setLatestEventInfo(this, mStrBedtimeNotifTitle,
                mStrBedtimeNotifText + " " + util.formatTimeText(hoursLeft, minutesLeft), contentIntent);

        // add sound, lights, and vibration
        notification.defaults |= Notification.DEFAULT_ALL;
        mNotificationManager.notify(ALARM_ID, notification);

    }

}
