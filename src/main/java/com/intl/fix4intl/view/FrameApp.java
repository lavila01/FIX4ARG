/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intl.fix4intl.view;

import com.intl.fix4intl.*;
import com.intl.fix4intl.Observable.LogonEvent;
import com.intl.fix4intl.Observable.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import quickfix.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.URL;
import java.util.Iterator;
import java.util.logging.Level;

/**
 *
 * @author yosbel
 */
public class FrameApp extends javax.swing.JFrame implements PropertyChangeListener {
    
    private static Logger log = LoggerFactory.getLogger(FrameApp.class);
    private boolean initiatorStarted = false;
    SessionSettings settings;
    private static FrameApp frameApp;
    private OrderTableModel orderTableModel;
    private App application;
    private Order order = null;//Orden seleccionada
    private Initiator initiator;

    /**
     * Creates new form FrameApp
     */
    public FrameApp() throws ConfigError, IOException {
        initComponents();
        orderTableModel = new OrderTableModel();
        InstrumentTableModel instrumentTableModel = new InstrumentTableModel();
        ExecutionTableModel executionTableModel = new ExecutionTableModel();
        String dir = getpath().getAbsolutePath() + "/config/app.cfg";
        File f = new File(dir);
        if (!f.isFile()) {
            f = new File("config/app.cfg");
        }
        InputStream inputStream = new BufferedInputStream(new FileInputStream(f));
        settings = new SessionSettings(inputStream);
        inputStream.close();
        boolean logHeartbeats = Boolean.parseBoolean(System.getProperty("logHeartbeats", "true"));
        
        application = new App(orderTableModel, executionTableModel, instrumentTableModel, settings);
        
        application.addLogonObservable(this);
        application.addOrderObservable(this);
        
        
        this.setComboValues(orderTableModel, executionTableModel, instrumentTableModel);
        MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
        LogFactory logFactory = new ScreenLogFactory(true, true, true, logHeartbeats);
        MessageFactory messageFactory = new DefaultMessageFactory();
        
        initiator = new SocketInitiator(application, messageStoreFactory, settings, logFactory,messageFactory);
        
        ////socketData
        
        
        //new Thread(client);
        //client.
//        DataSocket socket = new DataSocket(application,5555);
//        new Thread(socket).start();
        
        //socket.start(5555);
        
    }
    
    private void setComboValues(OrderTableModel orderTableModel, ExecutionTableModel executionTableModel, InstrumentTableModel instrumentTableModel) {
        ComboBoxModel<String> sideModel = new DefaultComboBoxModel(OrderSide.toArray());
        ComboBoxModel<String> typeModel = new DefaultComboBoxModel(OrderType.toArray());
        ComboBoxModel<String> tifModel = new DefaultComboBoxModel(OrderTIF.toArray());
        ComboBoxModel<String> setTypeModel = new DefaultComboBoxModel(OrderSettType.toArray());
        
        this.sideComboBox.setModel(sideModel);
        this.typeComboBox.setModel(typeModel);
        this.tifComboBox.setModel(tifModel);
        this.setTypeComboBox.setModel(setTypeModel);
        
        this.orderTable.setModel(orderTableModel);
        this.executionTable.setModel(executionTableModel);
        this.instrumentTable.setModel(instrumentTableModel);
        
        this.orderTable.getSelectionModel().addListSelectionListener(new OrderSelection());
        this.typeComboBox.addItemListener(new PriceListener());
        
    }
    
    private void submitOrder() throws FieldNotFound {
        Order newOrder = new Order();
        newOrder.setAccount(jTextAccount.getText());
        newOrder.setSide((OrderSide) sideComboBox.getSelectedItem());
        newOrder.setType((OrderType) typeComboBox.getSelectedItem());
        newOrder.setTIF((OrderTIF) tifComboBox.getSelectedItem());
        newOrder.setSetType((OrderSettType) setTypeComboBox.getSelectedItem());
        
        newOrder.setSymbol(symbolTextField.getText());
        newOrder.setQuantity(Integer.parseInt(quantityTextField.getText()));
        newOrder.setOpen(newOrder.getQuantity());
        
        OrderType type = newOrder.getType();
        if (type == OrderType.LIMIT || type == OrderType.STOP_LIMIT) {
            newOrder.setLimit(limitPriceTextField.getText());
        }
        if (type == OrderType.STOP || type == OrderType.STOP_LIMIT) {
            newOrder.setStop(stopPriceTextField.getText());
        }
        newOrder.setSessionID((SessionID) sessionComboBox.getSelectedItem());
        
        orderTableModel.addOrder(newOrder);
        application.sendOrder(newOrder);
    }
    
    private void replaceOrder(Order order) {
        Order newOrder = (Order) order.clone();
        newOrder.setQuantity(Integer.parseInt(cancelQuantityTextField.getText()));
        newOrder.setLimit(Double.parseDouble(cancelLimitPriceTextField.getText()));
        newOrder.setRejected(false);
        newOrder.setCanceled(false);
        newOrder.setOpen(0);
        newOrder.setExecuted(0);
        application.replace(order, newOrder);
    }
    
    private void clearMessage(String message) {
        messageLabel.setText(message);
        if (message == null || message.equals("")) {
            messageLabel.setText(" ");
        }
        
    }
    
    public void setOrder(Order order) {
        if (order == null) {
            return;
        }
        this.order = order;
        quantityTextField.setText(Integer.toString(order.getOpen()));
        
        Double limit = order.getLimit();
        if (limit != null) {
            limitPriceTextField.setText(order.getLimit().toString());
        }
    }
    
    private class OrderSelection implements ListSelectionListener {
        
        @Override
        public void valueChanged(ListSelectionEvent e) {
            ListSelectionModel selection = frameApp.orderTable.getSelectionModel();
            if (selection.isSelectionEmpty()) {
                frameApp.clearMessage(null);
                return;
            }
            
            int firstIndex = e.getFirstIndex();
            int lastIndex = e.getLastIndex();
            int selectedRow = 0;
            int numSelected = 0;
            
            for (int i = firstIndex; i <= lastIndex; ++i) {
                if (selection.isSelectedIndex(i)) {
                    selectedRow = i;
                    numSelected++;
                }
            }
            
            if (numSelected > 1) {
                frameApp.clearMessage(null);
            } else {
                Order order = (Order) orderTableModel.getOrder(selectedRow);
                if (order != null) {
                    frameApp.clearMessage(order.getMessage());
                    frameApp.setOrder(order);
                }
            }
        }
    }
    
    private class PriceListener implements ItemListener {
        
        @Override
        public void itemStateChanged(ItemEvent e) {
            OrderType item = (OrderType) typeComboBox.getSelectedItem();
            if (item == OrderType.MARKET) {
                enableLimitPrice(false);
                enableStopPrice(false);
            } else if (item == OrderType.STOP) {
                enableLimitPrice(false);
                enableStopPrice(true);
            } else if (item == OrderType.LIMIT) {
                enableLimitPrice(true);
                enableStopPrice(false);
            } else {
                enableLimitPrice(true);
                enableStopPrice(true);
            }
            activateSubmit();
        }
        
        private void activateSubmit() {
            OrderType type = (OrderType) typeComboBox.getSelectedItem();
            boolean activate = symbolTextField.getText() != null && quantityTextField.getText() != null
                    && sessionComboBox.getSelectedItem() != null;
            
            if (type == OrderType.MARKET) {
                submitButton.setEnabled(activate);
            } else if (type == OrderType.LIMIT) {
                submitButton.setEnabled(activate && limitPriceTextField.getText() != null);
            } else if (type == OrderType.STOP) {
                submitButton.setEnabled(activate && stopPriceTextField.getText() != null);
            } else if (type == OrderType.STOP_LIMIT) {
                submitButton.setEnabled(activate && limitPriceTextField.getText() != null
                        && stopPriceTextField.getText() != null);
            }
        }
        
        private void enableLimitPrice(boolean enabled) {
            Color bgColor = enabled ? Color.white : Color.gray;
            limitPriceTextField.setEnabled(enabled);
            limitPriceTextField.setBackground(bgColor);
        }
        
        private void enableStopPrice(boolean enabled) {
            Color bgColor = enabled ? Color.white : Color.gray;
            stopPriceTextField.setEnabled(enabled);
            stopPriceTextField.setBackground(bgColor);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel5 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        symbolTextField = new javax.swing.JTextField();
        quantityTextField = new javax.swing.JTextField();
        sideComboBox = new javax.swing.JComboBox<>();
        typeComboBox = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        limitPriceTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        stopPriceTextField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        tifComboBox = new javax.swing.JComboBox<>();
        sessionComboBox = new javax.swing.JComboBox();
        submitButton = new javax.swing.JButton();
        messageLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        setTypeComboBox = new javax.swing.JComboBox<>();
        jLabel12 = new javax.swing.JLabel();
        jTextAccount = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        instrumentTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        orderTable = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        executionTable = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cancelQuantityTextField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cancelLimitPriceTextField = new javax.swing.JTextField();
        cancelButton = new javax.swing.JButton();
        replaceButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("FIX4ARG");

        jLabel1.setText("Instrumento");

        jLabel2.setText("Cantidad");

        jLabel3.setText("Operacion");

        jLabel4.setText("Tipo");

        jLabel5.setText("Precio");

        jLabel6.setText("Parar");

        jLabel7.setText("Time In Force");

        submitButton.setText("Enviar");
        submitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitButtonActionPerformed(evt);
            }
        });

        jLabel10.setText("Mercado");

        jLabel11.setText("Plazo de Liquidacion");

        jLabel12.setText("Cuenta");

        jTextAccount.setText("REM2030");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(submitButton))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(symbolTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(27, 27, 27)
                                        .addComponent(jLabel1)))
                                .addGap(26, 26, 26)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(quantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel2)))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sessionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(sideComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(typeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(135, 135, 135)
                                .addComponent(messageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(limitPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(stopPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tifComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(setTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(20, 20, 20))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(symbolTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(quantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sideComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(typeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(limitPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stopPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tifComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(setTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(sessionComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12)
                            .addComponent(jTextAccount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(messageLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE))
                    .addComponent(submitButton))
                .addContainerGap())
        );

        instrumentTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Instrumento", "Moneda", "U. Operado", "Variacion", "Vencimiento", "Sec. Id", "MDReqID", "F. Actualizacion", "F. Operativa", "Mercado", "Apertura", "Ajuste", "Minimo", "Volumen", "Cierre", "Sec. Type", "Mat. Date", "Offers", "Trades", "Bids"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        instrumentTable.setName("instrumentTable"); // NOI18N
        jScrollPane1.setViewportView(instrumentTable);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 979, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Instrumentos", jPanel1);

        orderTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Instrumento", "Cantidad", "Open", "Executed", "Side", "Type", "Limit", "Stop", "AvgPx", "Target"
            }
        ));
        orderTable.setCellSelectionEnabled(true);
        jScrollPane2.setViewportView(orderTable);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 991, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Ordenes", jPanel2);

        executionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Instrumento", "Cantidad", "Side", "Precio", "Mensaje"
            }
        ));
        jScrollPane3.setViewportView(executionTable);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 979, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 239, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Ejecuciones", jPanel3);

        jLabel8.setText("Cantidad a Cancelar:");

        jLabel9.setText("Limite:");

        cancelButton.setText("Cancelar");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        replaceButton.setText("Reemplazar");
        replaceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                replaceButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(cancelQuantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addComponent(cancelLimitPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(81, 81, 81)
                .addComponent(cancelButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 56, Short.MAX_VALUE)
                .addComponent(replaceButton)
                .addGap(44, 44, 44))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelQuantityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(cancelLimitPriceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelButton)
                    .addComponent(replaceButton))
                .addGap(33, 33, 33))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jTabbedPane1)
                        .addContainerGap())))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        
        application.cancel(this.order);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void replaceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_replaceButtonActionPerformed
        
        this.replaceOrder(this.order);
    }//GEN-LAST:event_replaceButtonActionPerformed

    private void submitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitButtonActionPerformed
        try {
            this.submitOrder();
        } catch (FieldNotFound ex) {
            java.util.logging.Logger.getLogger(FrameApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_submitButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrameApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrameApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrameApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrameApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    frameApp = new FrameApp();
                    frameApp.setVisible(true);
                    frameApp.logon();
                } catch (FileNotFoundException ex) {
                    java.util.logging.Logger.getLogger(FrameApp.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ConfigError ex) {
                    java.util.logging.Logger.getLogger(FrameApp.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(FrameApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
    }
    
    public synchronized void logon() {
        if (!initiatorStarted) {
            try {
                initiator.start();
                initiatorStarted = true;
            } catch (Exception e) {
                log.error("Logon failed", e);
            }
        } else {
            Iterator<SessionID> sessionIds = initiator.getSessions().iterator();
            while (sessionIds.hasNext()) {
                SessionID sessionId = (SessionID) sessionIds.next();
                Session.lookupSession(sessionId).logon();
            }
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField cancelLimitPriceTextField;
    private javax.swing.JTextField cancelQuantityTextField;
    private javax.swing.JTable executionTable;
    private javax.swing.JTable instrumentTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextAccount;
    private javax.swing.JTextField limitPriceTextField;
    private javax.swing.JLabel messageLabel;
    private javax.swing.JTable orderTable;
    private javax.swing.JTextField quantityTextField;
    private javax.swing.JButton replaceButton;
    private javax.swing.JComboBox sessionComboBox;
    private javax.swing.JComboBox<String> setTypeComboBox;
    private javax.swing.JComboBox<String> sideComboBox;
    private javax.swing.JTextField stopPriceTextField;
    private javax.swing.JButton submitButton;
    private javax.swing.JTextField symbolTextField;
    private javax.swing.JComboBox<String> tifComboBox;
    private javax.swing.JComboBox<String> typeComboBox;
    // End of variables declaration//GEN-END:variables

    @Override
    public void propertyChange(PropertyChangeEvent arg) {
        if (arg.getNewValue() instanceof LogonEvent) {
            LogonEvent logonEvent = (LogonEvent) arg.getNewValue();
            if (logonEvent.isLoggedOn()) {
                sessionComboBox.addItem(logonEvent.getSessionID());
            } else {
                sessionComboBox.removeItem(logonEvent.getSessionID());
            }
        } else if (arg.getNewValue() instanceof OrderEvent) {
            setOrder(this.order);
        }
        
    }
    
    private static File WORKING_DIRECTORY;

    public static File getpath() {
        String Recurso = FrameApp.class.getSimpleName() + ".class";
        if (WORKING_DIRECTORY == null) {
            try {
                URL url = FrameApp.class.getResource(Recurso);
                if (url.getProtocol().equals("file")) {
                    File f = new File(url.toURI());
                    do {
                        f = f.getParentFile();
                    } while (!f.isDirectory());
                    WORKING_DIRECTORY = f;
                } else if (url.getProtocol().equals("jar")) {
                    String expected = "!/" + Recurso;
                    String s = url.toString();
                    s = s.substring(4);
                    s = s.substring(0, s.length() - expected.length());
                    File f = new File(new URL(s).toURI());
                    do {
                        f = f.getParentFile();
                    } while (!f.isDirectory());
                    WORKING_DIRECTORY = f;
                }
            } catch (Exception e) {
                WORKING_DIRECTORY = new File(".");
            }
        }
        return WORKING_DIRECTORY;
    }
    
}
