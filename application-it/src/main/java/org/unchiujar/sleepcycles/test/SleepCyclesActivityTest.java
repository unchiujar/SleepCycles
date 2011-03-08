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
