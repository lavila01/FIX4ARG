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
 * @author mar
 */
public class ObservableQuotations {
    
    private QuotationEvent quotationEvent;
    private PropertyChangeSupport support;

    public ObservableQuotations() {
        support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    public void setQuotationEvent(QuotationEvent quotationEvent) {
        support.firePropertyChange("quotation", this.quotationEvent, quotationEvent);
        this.quotationEvent = quotationEvent;
    }

}
