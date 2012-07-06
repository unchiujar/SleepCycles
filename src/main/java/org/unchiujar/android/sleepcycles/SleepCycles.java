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

import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.app.TabActivity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Chronometer.OnChronometerTickListener;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TabHost;
import android.widget.TextView;

@ContentView(R.layout.main)
public class SleepCycles extends TabActivity {
    private static final String CHRONO_START_TIME = "org.unchiujar.android.sleepcycles.sleepcycles.chrono_start_time";

    private static final String CHRONO_STARTED = "org.unchiujar.android.sleepcycles.sleepcycles.chrono_started";

    private static final Logger LOG = LoggerFactory.getLogger(SleepCycles.class);

    private static final String CALIBRATION_CYCLES = "org.unchiujar.android.sleepcycles.sleepcycles.calibration_cycles";
    private static final String CYCLE_LENGTH = "org.unchiujar.android.sleepcycles.sleepcycles.cycle_length";
    private final static byte SLEEP_CYCLE_MIN_LENGTH = 60;
    private final static byte SLEEP_CYCLE_MAX_LENGTH = 120;

    private SharedPreferences prefs = null;

    @InjectView(R.id.timeDisplay)
    private TextView mTimeDisplay;

    @InjectView(R.id.btnStart)
    private Button btnStart;
    @InjectView(R.id.btnStop)
    private Button btnStop;

    @InjectView(R.id.sleepTime)
    private Chronometer chrono;

    @InjectView(R.id.txtSleepCycles)
    private TextView txtCycles;

    @InjectView(R.id.txtSleepLength)
    private TextView txtSleepLength;

    @InjectView(R.id.txtCycleLength)
    private TextView txtCycleLength;

    @InjectView(R.id.pickHour)
    private SeekBar hourSeek;
    @InjectView(R.id.pickMinute)
    private SeekBar minuteSeek;

    private TabHost mTabHost;
    private AlarmOptionsAdapter alarms;

    private byte wakeHour;
    private byte wakeMinute;

    private State state;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // create tabs
        mTabHost = getTabHost();
        LOG.debug("Creating calibrate tab.");
        mTabHost.addTab(mTabHost.newTabSpec("tab_test1")
                .setIndicator(getString(R.string.calibrate)).setContent(R.id.calibrate_tab));
        LOG.debug("Creating alarm tab.");
        mTabHost.addTab(mTabHost.newTabSpec("tab_test2").setIndicator(getString(R.string.alert))
                .setContent(R.id.alert_tab));
        mTabHost.setCurrentTab(0);

        // get current time to set on seek bars
        Calendar currentTime = Calendar.getInstance();
        wakeHour = (byte) currentTime.get(Calendar.HOUR_OF_DAY);
        wakeMinute = (byte) currentTime.get(Calendar.MINUTE);

        hourSeek.setProgress(wakeHour);
        minuteSeek.setProgress(wakeMinute);

        btnStop.setEnabled(false);

        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LOG.debug("Sleep timer started");
                // save start time
                state.setStartTime(SystemClock.elapsedRealtime());
                state.setStarted(true);
                chrono.setBase(SystemClock.elapsedRealtime());
                chrono.setOnChronometerTickListener(new OnChronometerTickListener() {
                    public void onChronometerTick(Chronometer arg0) {
                        // NO-OP
                    }
                });

                chrono.start();
                btnStart.setText(getString(R.string.restart_button));
                btnStop.setEnabled(true);
            }

        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LOG.debug("Sleep timer stopped");
                chrono.stop();
                int sleepMinutes = (int) ((SystemClock.elapsedRealtime() - state.getStartTime()) / 1000 / 60);
                // calculate cycle length in minutes
                int cycleLength = calculateCycleApproximation(sleepMinutes);
                state.setCycleLength(cycleLength);
                state.setStarted(false);
                chrono.setBase(SystemClock.elapsedRealtime());

                txtCycles.setText("Sleep cycles " + sleepMinutes / cycleLength);
                txtCycleLength.setText("Cycle Length "
                        + Util.formatTimeText(SleepCycles.this, cycleLength));
                txtSleepLength.setText("Sleep Length "
                        + Util.formatTimeText(SleepCycles.this, sleepMinutes));

                saveCycleLength(cycleLength);
                LOG.debug("Total sleep length {}, sleep cycle length {}", sleepMinutes, cycleLength);
                btnStop.setEnabled(false);
                btnStart.setText(getString(R.string.start_button));

            }
        });

        // time picker code

        hourSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LOG.debug("Hour changed to {}", progress);
                wakeHour = (byte) progress;
                updateDisplay();
            }
        });

        minuteSeek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub

            }

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                LOG.debug("Minute changed to {}", progress);
                wakeMinute = (byte) progress;
                updateDisplay();
            }
        });

        // set roboto font
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        Util.setFont(font, txtCycles, txtSleepLength, txtCycleLength, btnStart, btnStop);

        // bold condensed for chronometer and wake time text
        font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-BoldCondensed.ttf");
        Util.setFont(font, chrono);

        // regular for labels
        font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");

        restoreState();

        if (loadCalibrationCycles() == 0) {
            // display the current date
            hourSeek.setEnabled(false);
            minuteSeek.setEnabled(false);
        } else {
            updateDisplay();
        }

    }

    /**
     * Restore state of the application.
     */
    private void restoreState() {
        LOG.debug("Restoring state...");
        state = new State();
        prefs = getPreferences(MODE_PRIVATE);
        state.setStarted(prefs.getBoolean(CHRONO_STARTED, false));
        state.setStartTime(prefs.getLong(CHRONO_START_TIME, 0));
        state.setCycleLength(prefs.getInt(CYCLE_LENGTH, 60));
        state.setCalibrations(prefs.getInt(CALIBRATION_CYCLES, 0));

        // set buttons state
        LOG.debug("Setting buttons...");

        setChronoState(state.isStarted());
    }

    private void setChronoState(boolean started) {
        if (started) {
            btnStart.setText(getString(R.string.restart_button));
            btnStop.setEnabled(true);
            // calculate hours, minutes, seconds from elapsed time
            chrono.setBase(state.getStartTime());
            chrono.start();
        } else {
            btnStart.setText(getString(R.string.start_button));
            btnStop.setEnabled(false);
            chrono.stop();
        }
    }

    /**
     * Returns the elapsed time since the chronometer was started divided in hours, minutes, and
     * seconds.
     * 
     * @param startTime the time in milliseconds when the chronometer was started
     * @return an array of int[3] containg hours, minutes, seconds of elapsed time
     */
    private int[] calculateTime(long startTime) {
        int[] time = new int[3];
        long timePassed = SystemClock.elapsedRealtime() - startTime;
        time[0] = (int) (timePassed / 1000 / 60 / 60);
        time[1] = (int) ((timePassed - time[0] * 60 * 60 * 1000) / 1000 / 60);
        time[2] = (int) ((timePassed - time[0] * 60 / 60 / 1000 - time[1] * 60 * 1000) / 1000);
        return time;
    }

    /**
     * Save the state of the application.
     */
    private void saveState() {
        prefs = getPreferences(MODE_PRIVATE);
        Editor edit = prefs.edit();
        edit.putBoolean(CHRONO_STARTED, state.isStarted());
        edit.putLong(CHRONO_START_TIME, state.getStartTime());
        edit.putInt(CYCLE_LENGTH, state.getCycleLength());
        edit.putInt(CALIBRATION_CYCLES, state.getCalibrations());
        edit.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState();
    }

    @Override
    public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        restoreState();
    }

    /**
     * Checks against every integer between SLEEP_CYCLE_MIN_LENGTH and SLEEP_CYCLE_MAX_LENGTH for
     * best fit and returns the most likely cycle length.
     * 
     * @param sleepMinutes the total minutes of sleep
     * @return the sleep cycle length in minutes
     */
    private int calculateCycleApproximation(int sleepMinutes) {

        ArrayList<Integer> remainder = new ArrayList<Integer>(SLEEP_CYCLE_MAX_LENGTH
                - SLEEP_CYCLE_MIN_LENGTH + 1);
        int k = 0;
        for (int cycleLength = SLEEP_CYCLE_MIN_LENGTH; cycleLength <= SLEEP_CYCLE_MAX_LENGTH; cycleLength++) {
            // check if cycle length is shorter than sleep length
            // add -1 to the vector to mark that every value after
            // is useless and break
            if (cycleLength > sleepMinutes) {
                remainder.add(-1);
                break;
            } else {
                remainder.add(sleepMinutes % cycleLength);

                LOG.debug("Remainder for cycle length {} is {}", cycleLength, remainder.get(k));
                k++;
            }
        }

        // find minimum
        int minimum = SLEEP_CYCLE_MAX_LENGTH;

        for (int i = 0; i < remainder.size(); i++) {
            if (remainder.get(i) == -1) {
                break;
            }

            // calculate the shortest distance to
            // a full cycle
            int smallest = Math.min(Math.abs(SLEEP_CYCLE_MIN_LENGTH + i - remainder.get(i)),
                    remainder.get(i));
            if (minimum > smallest) {
                minimum = smallest;
                k = i;
            }
        }
        LOG.debug("Minimum is  {} which coresponds to cycle length  {}", minimum,
                SLEEP_CYCLE_MIN_LENGTH + k);

        return SLEEP_CYCLE_MIN_LENGTH + k;
    }

    /**
     * Saves the the sleep cycle length.
     * 
     * @param length the sleep cycle length in minutes
     */
    private void saveCycleLength(int length) {
        hourSeek.setEnabled(true);
        minuteSeek.setEnabled(true);

        // get previous length
        int savedLength = loadCycleLength();
        int calibrationCycles = loadCalibrationCycles() + 1;
        Editor editor = prefs.edit();
        // check if savedLength is zero and the estimated length is the first
        // length counted
        editor.putInt(CYCLE_LENGTH, savedLength + length / calibrationCycles);
        editor.commit();
        setCalibrationCycles(calibrationCycles);
    }

    private void setCalibrationCycles(int calibrationCycles) {
        Editor editor = prefs.edit();
        // check if savedLength is zero and the estimated length is the first
        // length counted
        editor.putInt(CALIBRATION_CYCLES, calibrationCycles);
        editor.commit();

    }

    private int loadCalibrationCycles() {
        return state.getCalibrations();
    }

    /**
     * Gets the saved sleep cycle length.
     * 
     * @return the saved sleep cycle length in minutes
     */
    private int loadCycleLength() {
        // // TODO remove hardcoding
        // return 60;
        return state.getCycleLength();
    }

    // updates the time we display in the TextView
    private void updateDisplay() {
        mTimeDisplay.setText(new StringBuilder().append("Wakeup time ").append(pad(wakeHour))
                .append(":").append(pad(wakeMinute)));
        // generate list adapter data

        int wakeMinutes = wakeHour * 60 + wakeMinute;

        Calendar nowTime = Calendar.getInstance();
        int nowMinutes = nowTime.get(Calendar.HOUR_OF_DAY) * 60 + nowTime.get(Calendar.MINUTE);
        int timeToWake = wakeMinutes - nowMinutes;

        // if the time to wake is smaller in value than the current time (i.e. 2
        // AM vs 23 PM)
        // then add 24hours in minutes to the value in order to calculate the
        // distance properly

        timeToWake = timeToWake < 0 ? timeToWake + 24 * 60 : timeToWake;

        LOG.debug("Time to wake is {} minutes", timeToWake);
        // calculate how many full cycles are available
        int cycleLength = loadCycleLength();
        byte cycles = (byte) (timeToWake / cycleLength);
        LOG.debug("Number of possible cycles is {} ", cycles);

        int extraTime = timeToWake - (cycles * cycleLength);
        // create list
        AlarmOption[] alarmOptions = new AlarmOption[cycles];
        Calendar sleepTime = Calendar.getInstance();

        for (byte i = cycles; i > 0; i--) {
            sleepTime.setTime(new Date());

            sleepTime.add(Calendar.MINUTE, extraTime + (cycles - i) * cycleLength);
            alarmOptions[cycles - i] = new AlarmOption(sleepTime.getTime(), i * cycleLength, i);

        }

        alarms = new AlarmOptionsAdapter(getApplicationContext(), alarmOptions);
        ListView alarmsList = (ListView) findViewById(R.id.alarmList);
        alarmsList.setAdapter(alarms);
    }

    private static String pad(int amount) {
        if (amount >= 10) {
            return String.valueOf(amount);
        } else {
            return "0" + String.valueOf(amount);
        }
    }

}
