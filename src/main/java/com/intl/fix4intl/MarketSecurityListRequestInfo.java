/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intl.fix4intl;

import java.util.List;
import quickfix.SessionID;
import quickfix.field.MarketSegmentID;

/**
 *
 * @author mar
 */
public class MarketSecurityListRequestInfo {
    private SessionID sessionID;
    private List<MarketSegmentID> marketSegmentID;

    public MarketSecurityListRequestInfo(SessionID sessionID, List<MarketSegmentID> marketSegmentID) {
        this.sessionID = sessionID;
        this.marketSegmentID = marketSegmentID;
    }

     public MarketSecurityListRequestInfo() {
    }

    public SessionID getSessionID() {
        return sessionID;
    }

    public List<MarketSegmentID> getMarketSegmentID() {
        return marketSegmentID;
    }

    public void setSessionID(SessionID sessionID) {
        this.sessionID = sessionID;
    }

    public void setMarketSegmentID(List<MarketSegmentID> marketSegmentID) {
        this.marketSegmentID = marketSegmentID;
    }

    @Override
    public String toString() {
        return "MarketInfo{" + "sessionID=" + sessionID + ", marketSegmentID=" + marketSegmentID.toString() + '}';
    }

   
    
}
