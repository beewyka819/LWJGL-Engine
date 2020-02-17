package com.webs.beewyka819.gameengine.engine;

public class Timer {
    private double lastLoopTime;

    public void init() {
        lastLoopTime = getTime();
    }

    private double getTime() {
        return System.nanoTime() / 1000_000_000.0;
    }

    public float getElapsedTime() {
        double time = getTime();
        float elapsedTime = (float) (time - lastLoopTime);
        lastLoopTime = time;
        return elapsedTime;
    }

    public double getLastLoopTime() {
        return lastLoopTime;
    }
}