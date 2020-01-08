package com.webs.beewyka819.lwjglengine.engine;

/**
 * A highly accurate timer that keeps track of the amount
 * of time elapsed between any two points in runtime.
 */
public class Timer {
    /** 
     * The last measured time, recorded when either init() or
     * getElapsedTime() are called.
     */
    private double lastLoopTime;

    /**
     * Initializes the timer by setting the last measured time to the
     * current value of getTime().
     */
    public void init() {
        lastLoopTime = getTime();
    }

    /**
     * Returns the current value of the running 
     * Java Virtual Machine's high-resolution time source, in seconds
     * <p>
     * This method can only be used to measure elapsed time and is not 
     * related to any other notion of system or wall-clock time. 
     * The value returned represents seconds since some fixed but 
     * arbitrary origin time (perhaps in the future, so values may be 
     * negative). The same origin is used by all invocations of this 
     * method in an instance of a Java virtual machine; other virtual 
     * machine instances are likely to use a different origin.
     * <p>
     * This method provides nanosecond precision, but not necessarily 
     * nanosecond resolution (that is, how frequently the value changes)
     *  - no guarantees are made except that the resolution is at least 
     * as good as that of nanoTime().
     * <p>
     * The values returned by this method become meaningful only 
     * when the difference between two such values, obtained within 
     * the same instance of a Java virtual machine, is computed.
     * 
     * @return the current value of the running 
     * Java Virtual Machine's high-resolution time source, in seconds
     */
    public double getTime() {
        return System.nanoTime() / 1000_000_000.0;
    }
    
    /**
     * Calculates the amount of time elapsed since either init() or
     * getElapsedTime() had last been called.
     * <p>
     * Note: init() must be called before calling this method
     * for the first time.
     * @return the amount of time elapsed since either init() or
     * getElapsedTime() had last been called.
     */
    public float getElapsedTime() {
        double time = getTime();
        float elapsedTime = (float) (time - lastLoopTime);
        lastLoopTime = time;
        return elapsedTime;
    }

    /**
     * @return the last measured time, recorded when either init() or
     * getElapsedTime() are called.
     */
    public double getLastLoopTime() {
        return lastLoopTime;
    }
}