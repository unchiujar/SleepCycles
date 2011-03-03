package org.unchiujar.sleepcycles.test;

import org.unchiujar.sleepcycles.SleepCycles;

import android.test.ActivityInstrumentationTestCase2;

public class SleepCyclesActivityTest extends ActivityInstrumentationTestCase2<SleepCycles> {

    public SleepCyclesActivityTest() {
        super(SleepCycles.class);
    }

    public void testActivity() {
        SleepCycles activity = getActivity();
        assertNotNull(activity);
    }
}

