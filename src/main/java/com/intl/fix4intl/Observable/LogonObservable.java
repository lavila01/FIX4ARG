/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intl.fix4intl.Observable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import quickfix.SessionID;

/**
 *
 * @author yosbel
 */
public class LogonObservable {

    private LogonEvent logonEvent;
    private PropertyChangeSupport support;

    public LogonObservable() {
        support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void setLogonEvent(LogonEvent logonEvent) {
        support.firePropertyChange("logon", this.logonEvent, logonEvent);
        this.logonEvent = logonEvent;
    }

}
