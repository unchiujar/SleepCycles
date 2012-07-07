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

import java.util.Date;

public class AlarmOption {
    private Date time;
    private int sleepLength;
    private byte cycles;

    /** @param time the time at which to go to bed
     * @param sleepLength the total sleepLength of sleep in minutes
     * @param cycles the number of cycles of sleep */
    public AlarmOption(Date time, int sleepLength, byte cycles) {
        super();
        this.time = time;
        this.sleepLength = sleepLength;
        this.cycles = cycles;
    }

    /** @return the time at which to go to bed */
    public Date getBedTime() {
        return time;
    }

    /** @param time the time at which to go to bed */
    public void setBedTime(Date time) {
        this.time = time;
    }

    /** @return the sleep length in minutes */
    public int getSleepLength() {
        return sleepLength;
    }

    /** @param length the sleep length in minutes */
    public void setSleepLength(int length) {
        this.sleepLength = length;
    }

    /** @return the cycles of sleep */
    public byte getCycles() {
        return cycles;
    }

    /** @param cycles the cycles of sleep */
    public void setCycles(byte cycles) {
        this.cycles = cycles;
    }

    @Override
    public String toString() {
        return "AlarmOption [time=" + time + ", sleepLength=" + sleepLength + ", cycles=" + cycles + "]";
    }

}
