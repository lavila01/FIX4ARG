/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intl.fix4intl.Socket;

/**
 *
 * @author mar
 */
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import com.intl.fix4intl.Observable.QuotationEvent;
import java.beans.PropertyChangeEvent;
import java.io.*;
import java.net.*;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.*;

public class EchoClient implements Runnable{

    //private static final Logger LOG = Logger.getLogger(EchoClient.class);
    private static final Logger LOG = Logger.getLogger(EchoClient.class.getName());

    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String host;
    private int port;
    

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }
 
    public void startConnection() {
        try {
            clientSocket = new Socket(host, port);
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            LOG.info("conected ...");
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error when initializing connection {0}", e.getMessage());
        }

    }

    public String sendMessage(String msg) {
        //try {
            out.println(msg);
            return msg;
//        } catch (IOException e) {
//            return e.getMessage();
//        }
    }

    public void stopConnection() {
        try {
            in.close();
            out.close();
            clientSocket.close();
            LOG.info("disconected ...");
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "error when closing {0}", e.getMessage());
        }

    }

   

    @Override
    public void run() {
        startConnection();
    }
}
