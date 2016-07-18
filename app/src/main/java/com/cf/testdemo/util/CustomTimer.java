
package com.cf.testdemo.util;

import java.util.TimerTask;


public final class CustomTimer extends TimerTask {

    private Object m_cUserData;

    
    public interface HandleTimerDone {

        public void TimerExpired(CustomTimer pObjTimer, Object pUserData);
    }
    HandleTimerDone m_cTaksHndler;

    public CustomTimer(HandleTimerDone pTaskHndler) {
        this(pTaskHndler, null);
    }

    public CustomTimer(HandleTimerDone pTaskHndler, Object pUserData) {
        super();
        m_cUserData = pUserData;
        m_cTaksHndler = pTaskHndler;
    }

    public void run() {
        if (null != m_cTaksHndler) {
            m_cTaksHndler.TimerExpired(this, m_cUserData);
        }
    }

    public void Dispose() {
        m_cTaksHndler = null;
        m_cUserData = null;
    }
}
