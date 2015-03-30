package com.thilenius.flame.utilities.types;

/**
 * Created by Alec on 3/29/15.
 */
public class CoolDownTimer {

    private long m_startTime;
    private long m_coolDownTime;

    public CoolDownTimer(float coolDown) {
        m_coolDownTime = (long)(coolDown * 1000000000.0f);
        m_startTime = System.nanoTime();
    }

    public Boolean isOnCoolDown() {
        return (m_startTime + m_coolDownTime) < System.nanoTime();
    }

}
