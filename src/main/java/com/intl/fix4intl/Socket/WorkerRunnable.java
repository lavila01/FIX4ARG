/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intl.fix4intl.Socket;

import com.intl.fix4intl.Observable.QuotationEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author mar
 */
public class WorkerRunnable implements PropertyChangeListener, Runnable {

    protected Socket clientSocket = null;
    protected String serverText = null;
    private PrintWriter out;
    private BufferedReader in;
    //Queue<String> line;
    
    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText = serverText;
        //this.line = new LinkedBlockingQueue<>();
    }

    public void run() {
        try {
//            InputStream in = clientSocket.getInputStream();
//            OutputStream out = clientSocket.getOutputStream();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            long time = System.currentTimeMillis();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {

                System.out.println(inputLine);
                   
                if (inputLine.contentEquals("stop")) {
                    out.println("bye");
                    in.close();
                    out.close();
                    clientSocket.close();
                    break;
                }
            }
            //output.close();
            //input.close();
            System.out.println("Request processed: " + time);
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }
    }

     @Override
    public void propertyChange(PropertyChangeEvent evt) {
         if (evt.getNewValue() instanceof QuotationEvent) {
          QuotationEvent event = (QuotationEvent) evt.getNewValue();
          //line.add(event.getQuotations().getData());
             System.out.println("!!!!=============================+=+=+=+=+=+=+=+=+=+=+=");
             System.out.println(event.getQuotations().getData());
             System.out.println("!!!!!=============================+=+=+=+=+=+=+=+=+=+=+=");
         }        
    }
}
