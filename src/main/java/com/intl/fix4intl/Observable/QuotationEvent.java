/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intl.fix4intl.Observable;

import com.intl.fix4intl.Model.Quotations;

/**
 *
 * @author mar
 */
public class QuotationEvent {
    
     private Quotations quotations;

    public QuotationEvent(Quotations quotations) {
        this.quotations = quotations;
    }

    public Quotations getQuotations() {
        return quotations;
    }

    public void setQuotations(Quotations quotations) {
        this.quotations = quotations;
    }
 
}
