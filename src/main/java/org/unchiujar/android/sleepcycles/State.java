
package org.unchiujar.android.sleepcycles;

/**
 * Holds state of the application.
 * 
 * @author vasile.jureschi
 */
public class State {

    private boolean started;
    private long startTime;
    private long stopTime;
    private int cycleLength;
    private int calibrations;

    public int getCycleLength() {
        return cycleLength;
    }

    public void setCycleLength(int cycleLength) {
        this.cycleLength = cycleLength;
    }

    public int getCalibrations() {
        return calibrations;
    }

    public void setCalibrations(int calibrations) {
        this.calibrations = calibrations;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long starTime) {
        this.startTime = starTime;
    }

    public long getStopTime() {
        return stopTime;
    }

    public void setStopTime(long stopTime) {
        this.stopTime = stopTime;
    }

    public void reset() {
        started = false;
        startTime = 0;
        stopTime = 0;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + calibrations;
        result = prime * result + cycleLength;
        result = prime * result + (int) (startTime ^ (startTime >>> 32));
        result = prime * result + (started ? 1231 : 1237);
        result = prime * result + (int) (stopTime ^ (stopTime >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        State other = (State) obj;
        if (calibrations != other.calibrations)
            return false;
        if (cycleLength != other.cycleLength)
            return false;
        if (startTime != other.startTime)
            return false;
        if (started != other.started)
            return false;
        if (stopTime != other.stopTime)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "State [started=" + started + ", startTime=" + startTime + ", stopTime=" + stopTime
                + ", cycleLength=" + cycleLength + ", calibrations=" + calibrations + "]";
    }

}
