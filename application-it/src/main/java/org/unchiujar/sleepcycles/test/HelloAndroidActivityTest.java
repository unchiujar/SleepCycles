package org.unchiujar.sleepcycles.test;

import android.test.ActivityInstrumentationTestCase2;
import org.unchiujar.sleepcycles.*;

public class HelloAndroidActivityTest extends ActivityInstrumentationTestCase2<HelloAndroidActivity> {

    public HelloAndroidActivityTest() {
        super(HelloAndroidActivity.class);
    }

    public void testActivity() {
        HelloAndroidActivity activity = getActivity();
        assertNotNull(activity);
    }
}

