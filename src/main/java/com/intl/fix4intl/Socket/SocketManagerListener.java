/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intl.fix4intl.Socket;

import com.intl.fix4intl.Observable.QuotationEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author mar
 */
public class SocketManagerListener implements PropertyChangeListener {
    
// @Override
//    public void propertyChange(PropertyChangeEvent evt) {
//        if (evt.getNewValue() instanceof QuotationEvent) {
//          QuotationEvent event = (QuotationEvent) evt.getNewValue();
//          line.add(event.getQuotations().getData());
//             System.out.println("!!!!=============================+=+=+=+=+=+=+=+=+=+=+=");
//             System.out.println(" data added ");
//             
//             System.out.println("!!!!!=============================+=+=+=+=+=+=+=+=+=+=+=");
//             System.out.println(sendMessage(line.poll()));
//         }
//        
//       System.out.println("!!!!=============================+=+=+=+=+=+=+=+=+=+=+=");
//       System.out.println(" data added "+ line.size());      
//       System.out.println("!!!!!=============================+=+=+=+=+=+=+=+=+=+=+=");
//    }

   EchoClient client;

    public SocketManagerListener(EchoClient client) {
        this.client = client;
    }
   

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue() instanceof QuotationEvent) {
           QuotationEvent event = (QuotationEvent) evt.getNewValue();
           client.sendMessage(event.getQuotations().getData());
        }
    }
}

