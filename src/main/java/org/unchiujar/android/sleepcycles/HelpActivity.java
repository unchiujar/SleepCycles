
package org.unchiujar.android.sleepcycles;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.inject.Inject;

@ContentView(R.layout.help)
public class HelpActivity extends RoboActivity {

    private static final String TURN_OFF_HELP = "org.unchiujar.android.sleepcycles.sleepcycles.turn_off_help";
    @InjectView(R.id.btn_turn_off) private Button mTurnOff;
    @InjectView(R.id.btn_next) private Button mNext;
    @InjectView(R.id.help_view) private TextView mHelpView;
    @InjectResource(R.string.help_text) private String mStrHelp;
    @Inject private Util mUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // if the turn off flag has been set skip the help screen
        if (getPreferences(MODE_PRIVATE).getBoolean(TURN_OFF_HELP, false)) {
            startSleepCycles();
        }
        mHelpView.setText(Html.fromHtml(mStrHelp));
        mUtil.setFont(mUtil.ROBOTO_THIN, mTurnOff, mNext, mHelpView);
        // if the turn off button is clicked, save the flag so
        // the help activity is not shown on the next run
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
