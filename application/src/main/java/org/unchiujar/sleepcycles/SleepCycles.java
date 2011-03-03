package org.unchiujar.sleepcycles;

import java.util.Random;
import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Chronometer.OnChronometerTickListener;

public class SleepCycles extends Activity {
    private final static byte   SLEEP_CYCLE_MIN_LENGTH = 60;
    private final static byte   SLEEP_CYCLE_MAX_LENGTH = 120;

    private final static String TAG                    =
                                                               SleepCycles.class
                                                                       .toString();
    private Chronometer         chrono;
    private TextView            txtCycles;
    private TextView            txtSleepLength;
    private TextView            txtCycleLength;


    private int                 seconds;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        chrono = (Chronometer) findViewById(R.id.sleepTime);
        
        txtCycles = (TextView) findViewById(R.id.txtSleepCycles);
        txtSleepLength= (TextView) findViewById(R.id.txtSleepLength);
        txtCycleLength = (TextView) findViewById(R.id.txtCycleLength);

        
        final Button btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "Sleep timer started");

                chrono.setBase(SystemClock.elapsedRealtime());

                seconds = 0;
                chrono
                        .setOnChronometerTickListener(new OnChronometerTickListener() {
                            @Override
                            public void onChronometerTick(Chronometer arg0) {
                                seconds++;
                            }
                        });

                chrono.start();
                btnStart.setText("Restart");

            }
        });

        final Button btnStop = (Button) findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "Sleep timer stopped");
                chrono.stop();

                Random rng = new Random();
                int mockSleepLength = rng.nextInt(180 * 60) + 5 * 60 * 60;
                int mockCycleLength = calculateCycleApproximation(mockSleepLength);
                
                txtCycles.setText("Sleep cycles " + mockSleepLength / 60 / mockCycleLength);
                txtCycleLength.setText("Cycle Length " + mockCycleLength );
                txtSleepLength.setText("Sleep Length " + mockSleepLength /60);
                
                
                chrono.setBase(SystemClock.elapsedRealtime());
                btnStart.setText("Start");

            }
        });

    }

    /**
     * Checks against every integer between SLEEP_CYCLE_MIN_LENGTH and
     * SLEEP_CYCLE_MAX_LENGTH for best fit and returns the most likely cycle
     * length.
     * 
     * @param sleepMinutes
     * @return
     */
    private int calculateCycleApproximation(int sleepMinutes) {

        Vector<Integer> remainder =
                new Vector<Integer>(SLEEP_CYCLE_MAX_LENGTH
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

                Log.v(TAG, "Remainder for cycle length " + cycleLength + " is "
                        + remainder.get(k));
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
            int smallest = Math.min(Math.abs(SLEEP_CYCLE_MIN_LENGTH + i - remainder.get(i)), remainder.get(i));
            if (minimum >  smallest) {
                minimum = smallest;
                k = i;
            }
        }
        Log.d(TAG, "Minimum is " + minimum
                + " which coresponds to cycle length "
                + (SLEEP_CYCLE_MIN_LENGTH + k));

        return SLEEP_CYCLE_MIN_LENGTH + k;
    }
}