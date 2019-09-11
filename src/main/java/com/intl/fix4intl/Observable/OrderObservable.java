/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intl.fix4intl.Observable;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author yosbel
 */
public class OrderObservable {

    private OrderEvent orderEvent;
    private PropertyChangeSupport support;

    public OrderObservable() {
        support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void setOrderEvent(OrderEvent orderEvent) {
        support.firePropertyChange("order", this.orderEvent, orderEvent);
        this.orderEvent = orderEvent;
    }

}
