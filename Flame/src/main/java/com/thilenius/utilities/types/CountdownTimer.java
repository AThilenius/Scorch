package com.thilenius.utilities.types;

/**
 * Created by Alec on 11/17/14.
 */
public class CountdownTimer {

    private long m_startTime;
    private long m_endTime;

    public CountdownTimer(float durationSeconds) {
        m_startTime = System.nanoTime();
        m_endTime = m_startTime + (long)(durationSeconds * 1000000000.0);
    }

    public float getRemainingTime() {
        long elapsedNs = System.nanoTime() - m_startTime;
        if (elapsedNs < 0) {
            elapsedNs = -elapsedNs;
        }
        return (float)((double)elapsedNs / 1000000000.0);
    }

    // Example Start  Elapsed         End
    //          15      5              30
    // Delta: 15
    // Elapsed: 5
    // Remaining: 10 (delta - elapsed)
    public float getRemainingRatio() {
        long elapsedNs = System.nanoTime() - m_startTime;
        if (elapsedNs < 0) {
            elapsedNs = -elapsedNs;
        }
        long deltaNs = m_endTime - m_startTime;
        return (float)((double)(deltaNs - elapsedNs) / (double)deltaNs);
    }

}
