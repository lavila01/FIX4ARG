/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intl.fix4intl.Socket;

import com.intl.fix4intl.App;
import com.intl.fix4intl.Observable.QuotationEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mar
 */
//public class DataSocket implements PropertyChangeListener{
//
//    private ServerSocket serverSocket;
//    
//    Queue<String> line;
//    
//    public DataSocket() {
//        this.line = new LinkedBlockingQueue<>();
//    }
//    
//    public void start(int port) {
//        try {
//            serverSocket = new ServerSocket(port);
//            
//            while (true) {
//                new DataSocketClientHandler(serverSocket.accept()).start();
//            }
//           
//        } catch (IOException ex) {
//            Logger.getLogger(DataSocket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    public void stop() {
//        try {
//            serverSocket.close();
//        } catch (IOException ex) {
//            Logger.getLogger(DataSocket.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    @Override
//    public void propertyChange(PropertyChangeEvent evt) {
//         if (evt.getNewValue() instanceof QuotationEvent) {
//          QuotationEvent event = (QuotationEvent) evt.getNewValue();
//             line.add(event.getQuotations().getData());
//         }         
//    }
//
//    private  class DataSocketClientHandler extends Thread {
//
//        private Socket clientSocket;
//        private PrintWriter out;
//        private BufferedReader in;
//        
//        public DataSocketClientHandler(Socket socket) {
//            this.clientSocket = socket;
//            Logger.getLogger(DataSocket.class.getName()).log(Level.INFO, "Server Started ... ");
//        }
//
//        public void run() {
//            try {
//                out = new PrintWriter(clientSocket.getOutputStream(), true);
//                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//
//                String inputLine;
//                while((inputLine = in.readLine()) != null){
//                    if(inputLine.contentEquals("start")){
//                        System.out.println(inputLine);
//                        while (!line.isEmpty()) {
//                            out.println(line.poll());
//                            
//                        }
//                    } else if(inputLine.contentEquals("stop")){
//                        out.println("bye");
//                        in.close();
//                        out.close();
//                        clientSocket.close();
//                        break;
//                    }    
//                }
//                
////                while ((inputLine = in.readLine()) != null) {
////                    
////                    if (inputLine.equals("stop")) {
////                        out.println("bye");
////                        in.close();
////                        out.close();
////                        clientSocket.close();
////                        break;
////                    }
////                }
//               
//            } catch (IOException ex) {
//                Logger.getLogger(DataSocket.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
//}

public class DataSocket implements Runnable{

    protected int          serverPort   = 8080;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
    protected Thread       runningThread= null;
    protected App application;
    public DataSocket(App application,int port){
        this.serverPort = port;
        this.application = application;
    }

    public void run() {
        synchronized (this) {
            this.runningThread = Thread.currentThread();
        }
        openServerSocket();
        while (!isStopped()) {
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if (isStopped()) {
                    System.out.println("Server Stopped.");
                    return;
                }
                throw new RuntimeException(
                        "Error accepting client connection", e);
            }
            
            WorkerRunnable wr = new WorkerRunnable(clientSocket, "Multithreaded Server");
            //application.addQuotationsbservable(wr);
            new Thread(wr).start();
        }
        System.out.println("Server Stopped.");
    }


    private synchronized boolean isStopped() {
        return this.isStopped;
    }

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port "+this.serverPort, e);
        }
    }
    
}


