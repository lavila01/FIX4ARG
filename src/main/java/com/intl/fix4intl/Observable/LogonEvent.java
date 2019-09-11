/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intl.fix4intl.Observable;

import quickfix.SessionID;
/**
 *
 * @author mar
 */


public class LogonEvent {
    private SessionID sessionID;
    private boolean loggedOn;

    public LogonEvent(SessionID sessionID, boolean loggedOn) {
        this.sessionID = sessionID;
        this.loggedOn = loggedOn;
    }

    public SessionID getSessionID() {
        return sessionID;
    }
    public boolean isLoggedOn() {
        return loggedOn;
    }
}