
package org.unchiujar.android.sleepcycles;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HelpActivity extends Activity {

    private static final String TURN_OFF_HELP = "org.unchiujar.android.sleepcycles.sleepcycles.turn_off_help";
    private Button mTurnOff;
    private Button mNext;
    private TextView mHelpView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // if the turn off flag has been set skip the help screen
        if (getPreferences(MODE_PRIVATE).getBoolean(TURN_OFF_HELP, false)) {
            startSleepCycles();
        }
        setContentView(R.layout.help);
        mTurnOff = (Button) findViewById(R.id.btn_turn_off);
        mNext = (Button) findViewById(R.id.btn_next);
        mHelpView = (TextView) findViewById(R.id.help_text);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Thin.ttf");
        Util.setFont(font, mTurnOff, mNext, mHelpView);

        mTurnOff.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Editor editor = getPreferences(MODE_PRIVATE).edit();
                editor.putBoolean(TURN_OFF_HELP, true);
                editor.commit();
                startSleepCycles();

            }
        });

        mNext.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                startSleepCycles();
            }
        });

    }

    private void startSleepCycles() {
        Intent intent = new Intent(this, SleepCycles.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_help, menu);
        return true;
    }

}
