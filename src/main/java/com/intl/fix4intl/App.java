package com.intl.fix4intl;

import com.intl.fix4intl.DBController.QuotationsJpaController;
import com.intl.fix4intl.Observable.LogonEvent;
import com.intl.fix4intl.Observable.LogonObservable;
import com.intl.fix4intl.Observable.ObservableQuotations;
import com.intl.fix4intl.Observable.OrderObservable;
import com.intl.fix4intl.RestOrdersGson.OrderDTO;
import com.intl.fix4intl.RestOrdersGson.RestOrderService;
import com.intl.fix4intl.Socket.EchoClient;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix50sp2.MarketDataIncrementalRefresh;
import quickfix.fix50sp2.MarketDataSnapshotFullRefresh;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author mar
 */
public class App extends MessageCracker implements Application {    

    private final LogonObservable logonObservable = new LogonObservable();
    private final OrderObservable orderObservable = new OrderObservable();
    private final ObservableQuotations observableQuotations = new ObservableQuotations();
    private final AtomicInteger countHeartBeat = new AtomicInteger(0);
    
    private boolean isAvailable = true;
    private boolean starQuotes = false;
    private boolean isMissingField;

    
    QuotationsJpaController con;
    BymaManager bymaManager;
    RofexManager rofexManager;
    Manager manager = null;
    SessionSettings settings;
    
    EchoClient client;
    RestOrderService restService;
    OrderTableModel orderTableModel;

    public App(OrderTableModel orderTableModel,ExecutionTableModel executionTableModel,InstrumentTableModel instrumentTableModel, SessionSettings settings) throws ConfigError {
        con = new QuotationsJpaController();
        //service to send orders
        restService = new RestOrderService(settings);
        bymaManager = new BymaManager(orderTableModel, executionTableModel, instrumentTableModel, this.orderObservable, this.observableQuotations, restService) ;
        rofexManager = new RofexManager(orderTableModel, executionTableModel, instrumentTableModel, this.orderObservable, this.observableQuotations, restService);
        this.orderTableModel = orderTableModel;
        this.settings = settings;
        System.out.println("Connection -->" + con.getEntityManager().getProperties().toString());

    }

    private boolean mercadoEsBYMA(SessionID sessionID) {
        return sessionID.getTargetCompID().equals("STUN");
    }

    private boolean mercadoEsROFEX(SessionID sessionID) {
        return sessionID.getTargetCompID().equals("ROFX");
    }

    public BymaManager getBymaManager() {
        return bymaManager;
    }

    public void setBymaManager(BymaManager bymaManager) {
        this.bymaManager = bymaManager;
    }

    public RofexManager getRofexManager() {
        return rofexManager;
    }

    public void setRofexManager(RofexManager rofexManager) {
        this.rofexManager = rofexManager;
    }

    public void addLogonObservable(PropertyChangeListener observer) {
        logonObservable.addPropertyChangeListener(observer);
    }

    public void addOrderObservable(PropertyChangeListener observer) {
        orderObservable.addPropertyChangeListener(observer);
    }
    

    /**
     *
     * @param message
     * @param sessionId
     * @throws FieldNotFound
     * @throws IncorrectDataFormat
     * @throws IncorrectTagValue
     * @throws UnsupportedMessageType This is one of the core entry points for
     * your FIX application. Every application level request will come through
     * here. this is where you will get your new order requests. If you were a
     * buy side, you would get your execution reports here. If a FieldNotFound
     * exception is thrown, the counterparty will receive a reject indicating a
     * conditionally required field is missing. The Message class will throw
     * this exception when trying to retrieve a missing field, so you will
     * rarely need the throw this explicitly. You can also throw an
     * UnsupportedMessageType exception. This will result in the counterparty
     * getting a reject informing them your application cannot process those
     * types of messages. An IncorrectTagValue can also be thrown if a field
     * contains a value that is out of range or you do not support.
     */
    @Override
    public void fromApp(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
        crack(message, sessionId);
    }

    /**
     *
     * @param message
     * @param sessionID Receive message
     */
    @Override
    public void onMessage(quickfix.Message message, SessionID sessionID) {
        try {
            this.manager = mercadoEsBYMA(sessionID) ? this.bymaManager : this.rofexManager;
            SwingUtilities.invokeLater(new MessageProcessor(message, sessionID, this.manager));
        } catch (Exception e) {
        }
    }

    private void submitOrder(OrderDTO orderDTO, SessionID sessionId) throws FieldNotFound {
        //System.out.println(orderDTO);
        Order newOrder = new Order();
        newOrder.setAccount(orderDTO.getComitente().getCuentaZeni());
        newOrder.setSide(orderDTO.getTipoOperacion().getMultiplicador() == 1 ? OrderSide.BUY : OrderSide.SELL);
        newOrder.setType(OrderType.parse(orderDTO.getTipoPrecio().getCodigoMercado()));
        newOrder.setTIF(OrderTIF.DAY);// fijo!
        //newOrder.setSetType(OrderSettType.CASH);
        newOrder.setSymbol(orderDTO.getInstrumento().getAbreviaturaMercadoPorDefault());
        newOrder.setQuantity(orderDTO.getCantidad());
        newOrder.setOpen(newOrder.getQuantity());
        OrderType type = newOrder.getType();
        if (type.equals(OrderType.LIMIT) || type.equals(OrderType.STOP_LIMIT)) {
            newOrder.setLimit(orderDTO.getPrecio());
        }
//        if (type == OrderType.STOP || type == OrderType.STOP_LIMIT) {
//            newOrder.setStop(orderDTO.getPrecio());
//        }
        newOrder.setSessionID(sessionId);
        orderTableModel.addOrder(newOrder);
        manager = mercadoEsROFEX(sessionId) ? rofexManager : bymaManager;
        manager.idToOrderDto.put(newOrder.getID(), orderDTO);
        sendOrder(newOrder);
    }

    /**
     * @param sessionId This method is called when quickfix creates a new
     *                  session. A session comes into and remains in existence for the life of
     *                  the application. Sessions exist whether or not a counter party is
     *                  connected to it. As soon as a session is created, you can begin sending
     *                  messages to it. If no one is logged on, the messages will be sent at the
     *                  time a connection is established with the counterparts.
     */
    @Override
    public void onCreate(SessionID sessionId) {
        System.out.println("OnCreate --> " + sessionId.toString());
    }

    /**
     *
     * @param sessionId This callback notifies you when a valid logon has been
     * established with a counter party. This is called when a connection has
     * been established and the FIX logon process has completed with both
     * parties exchanging valid logon messages.
     */
    @Override
    public void onLogon(SessionID sessionId) {
        logonObservable.setLogonEvent(new LogonEvent(sessionId, true));
        System.out.println(sessionId + " --> id of session");
    }

    /**
     *
     * @param sessionId This callback notifies you when an FIX session is no
     * longer online. This could happen during a normal logout exchange or
     * because of a forced termination or a loss of network connection.
     */
    @Override
    public void onLogout(SessionID sessionId) {
        starQuotes = false;
        logonObservable.setLogonEvent(new LogonEvent(sessionId, false));
        System.out.println(sessionId + " --> logout session");
    }

    /**
     *
     * @param message
     * @param sessionId This callback provides you with a peak at the
     * administrative messages that are being sent from your FIX engine to the
     * counter party. This is normally not useful for an application however it
     * is provided for any logging you may wish to do. Notice that the
     * FIX::Message is not constant. This allows you to add fields before an
     * adminstrative message before it is sent out.
     */
    @Override
    public void toAdmin(Message message, SessionID sessionId) {

        if (MessageUtils.isLogon(message.toString())) {
            //byma test
            if (mercadoEsBYMA(sessionId)) {
                try {
                    //DEV BYMA
                    String user = settings.getString("SenderCompID");
                    String password = settings.getString("Password");
                    message.getHeader().setField(new StringField(Username.FIELD, user));
                    message.getHeader().setField(new StringField(Password.FIELD, password));
                    System.out.println("toAdmin --Logon BYMA>" + message);
                } catch (ConfigError ex) {
                    ex.printStackTrace();
                }
            }

            if (mercadoEsROFEX(sessionId)) {
                //rofex test  
                try {
                    System.out.println(settings.getString("UserName")+" - - "+settings.getString("Password"));
                    if(settings.isSetting("UserName") && settings.isSetting("Password")){
                        String user = settings.getString("UserName");
                        String password = settings.getString("Password");
                        message.getHeader().setField(new StringField(Username.FIELD, user));
                        message.getHeader().setField(new StringField(Password.FIELD, password));
                    }
                } catch (ConfigError ex) {
                    ex.printStackTrace();
                }
                System.out.println("toAdmin --Logon ROFEX>" + message);
            }

        }
        if (MessageUtils.isHeartbeat(message.toString())) {
            System.out.println("toAdmin --Heaetbeat>" + message);
            //ask for Orders to send
            try {
                List<OrderDTO> orderDTOS = restService.getOrdersGson();
                for (OrderDTO orderDTO: orderDTOS) {
                    submitOrder(orderDTO, sessionId);
                }

            } catch (IOException | FieldNotFound e) {
                e.printStackTrace();
            }

        }

    }

    public class fillMarketDataRequest implements Runnable {

        private final int dUpdateType;
        private final SessionID session;
        private final Manager manager;

        public fillMarketDataRequest(int dUpdateType, SessionID session, Manager manager) {
            this.dUpdateType = dUpdateType;
            this.session = session;
            this.manager = manager;

        }

        @Override
        public void run() {
            if (starQuotes) {
                try {
                    this.manager.createMarketDataRequest(dUpdateType, session);
//                    starQuotes = false;
                } catch (SessionNotFound ex) {
                }
            }
        }
    }

    @Override
    public void fromAdmin(Message message, SessionID sessionId) throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
        System.out.println("fromAdmin --> " + message);
        try {
            this.manager = mercadoEsBYMA(sessionId) ? this.bymaManager : this.rofexManager;
            fillMarketDataRequest fillMarketDataRequest = new fillMarketDataRequest(this.countHeartBeat.intValue() == 0 ? MDUpdateType.FULL_REFRESH : MDUpdateType.INCREMENTAL_REFRESH, sessionId, this.manager);
            System.out.println("Count Hear: " + fillMarketDataRequest.dUpdateType);
            fillMarketDataRequest.run();

        } catch (Exception e) {
        }
    }

    /**
     *
     * @param message
     * @param sessionId
     * @throws DoNotSend This is a callback for application messages that you
     * are being sent to a counterparty. If you throw a DoNotSend exception in
     * this function, the application will not send the message. This is mostly
     * useful if the application has been asked to resend a message such as an
     * order that is no longer relevant for the current market. Messages that
     * are being resent are marked with the PossDupFlag in the header set to
     * true; If a DoNotSend exception is thrown and the flag is set to true, a
     * sequence reset will be sent in place of the message. If it is set to
     * false, the message will simply not be sent. Notice that the FIX::Message
     * is not constant. This allows you to add fields before an application message
     * before it is sent out.
     */
    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
        //System.out.println("to app --> " + message);
    }

    public void cancel(Order order) {
        Manager man = mercadoEsBYMA(order.getSessionID()) ? this.bymaManager : this.rofexManager;
        man.cancel(order);
    }

    public void sendOrder(Order order) throws FieldNotFound {
        Manager man = mercadoEsBYMA(order.getSessionID()) ? this.bymaManager : this.rofexManager;
        man.sendOrder(order);
    }

    public void replace(Order order, Order newOrder) {
        Manager man = mercadoEsBYMA(order.getSessionID()) ? this.bymaManager : this.rofexManager;
        man.replace(order, newOrder);
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setMissingField(boolean isMissingField) {
        this.isMissingField = isMissingField;
    }

    public boolean isMissingField() {
        return isMissingField;
    }

    private void addQuotationsbservable(PropertyChangeListener observer) {
        observableQuotations.addPropertyChangeListener(observer);
        System.out.println("added property");
    }

    public class MessageProcessor implements Runnable {

        private final quickfix.Message message;
        private final SessionID sessionID;
        private final Manager manager;

        public MessageProcessor(Message message, SessionID sessionID, Manager manager) {
            this.message = message;
            this.sessionID = sessionID;
            this.manager = manager;

        }

        @Override
        public void run() {
            try {
                MsgType msgType = new MsgType();
                if (isAvailable) {
                    //System.out.println("MessageProcessor --> " + message);
                    if (isMissingField) {
                        // For OpenFIX certification testing
                        this.manager.sendBusinessReject(message, BusinessRejectReason.CONDITIONALLY_REQUIRED_FIELD_MISSING, "Conditionally required field missing");
                    } else if (message.getHeader().isSetField(DeliverToCompID.FIELD)) {
                        // This is here to support OpenFIX certification
                        this.manager.sendSessionReject(message, SessionRejectReason.COMPID_PROBLEM);
                    } else if (message.getHeader().getField(msgType).valueEquals(MsgType.EXECUTION_REPORT)) {
                        System.out.println("EXECUTION REPORT --> " + message);
                        this.manager.executionReport(message, sessionID);
                    } else if (message.getHeader().getField(msgType).valueEquals(MsgType.ORDER_CANCEL_REJECT)) {
                        this.manager.cancelReject(message, sessionID);
                    } else if (message.getHeader().getField(msgType).valueEquals(MsgType.TRADING_SESSION_STATUS)) {
                        //GetMarketInfo MarketSegmentID  .. DD .. MATB
                        System.out.println("TRADING_SESSION_STATUS -> " + message);
                        this.manager.fillMarketSecurityListRequestInfo(message, sessionID);
                    } else if (message.getHeader().getField(msgType).valueEquals(MsgType.TEST_REQUEST)) {
                        System.out.println("test Message --> " + message);
                    } else if (message.getHeader().getField(msgType).valueEquals(MsgType.NEWS)) {
                        System.out.println("News Message --> " + message);
                    } else if (message.getHeader().getField(msgType).valueEquals(MsgType.SECURITY_LIST)) {
                        //System.out.println("SECURITY_LIST -> " + message);
                        this.manager.fillInstrumentList(message, sessionID);
                        starQuotes = true;
                    } else if (message.getHeader().getField(msgType).valueEquals(MsgType.MARKET_DATA_SNAPSHOT_FULL_REFRESH)) {
                        //System.out.println("MARKET_DATA_SNAPSHOT_FULL_REFRESH -> " + message);
                        this.manager.fillCotizaciones((MarketDataSnapshotFullRefresh) message, sessionID);
                    } else if (message.getHeader().getField(msgType).valueEquals(MsgType.MARKET_DATA_INCREMENTAL_REFRESH)) {
                        //System.out.println("MARKET_DATA_INCREMENTAL_REFRESH -> " + message);
                        this.manager.fillCotizaciones((MarketDataIncrementalRefresh) message, sessionID);
                    } else if (message.getHeader().getField(msgType).valueEquals(MsgType.INDICATION_OF_INTEREST)) {
                        System.out.println("INDICATION_OF_INTEREST -> " + message);
                        this.manager.requestSecurityList(message, sessionID);
                    } else if (message.getHeader().getField(msgType).valueEquals(MsgType.MARKET_DATA_REQUEST_REJECT)) {
                        System.out.println("MARKET DATA REQUEST REJECT -> " + message);
                    } else if (message.getHeader().getField(msgType).valueEquals(MsgType.TRADE_CAPTURE_REPORT)) {
                        System.out.println("TRADE_CAPTURE_REPORT -> " + message);
                    } else if (message.getHeader().getField(msgType).valueEquals(MsgType.SECURITY_STATUS)) {
                        System.out.println("SECURITY STATUS -> " + message);
                    } else {
                        System.out.println("SIN MANEJAR -> " + message.getHeader().getField(msgType));
                        System.out.println("SIN MANEJAR -> " + message);
                        //sendBusinessReject(message, BusinessRejectReason.UNSUPPORTED_MESSAGE_TYPE,
                        //      "Unsupported Message Type");
                    }
                } else {
                    this.manager.sendBusinessReject(message, BusinessRejectReason.APPLICATION_NOT_AVAILABLE,
                            "Application not available");
                }
            } catch (FieldNotFound | SessionNotFound e) {
                System.out.println(e);
            } catch (InterruptedException | InvocationTargetException ex) {
            }

        }
    }
}
