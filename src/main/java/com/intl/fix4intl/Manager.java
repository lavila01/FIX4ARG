/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intl.fix4intl;

import com.intl.fix4intl.Observable.OrderEvent;
import com.intl.fix4intl.Observable.OrderObservable;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import quickfix.DefaultMessageFactory;
import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.field.AggregatedBook;
import quickfix.field.AvgPx;
import quickfix.field.BeginString;
import quickfix.field.BusinessRejectReason;
import quickfix.field.ClOrdID;
import quickfix.field.CumQty;
import quickfix.field.Currency;
import quickfix.field.DKReason;
import quickfix.field.ExecID;
import quickfix.field.LastPx;
import quickfix.field.LastShares;
import quickfix.field.LeavesQty;
import quickfix.field.LocateReqd;
import quickfix.field.MDEntryType;
import quickfix.field.MDReqID;
import quickfix.field.MsgSeqNum;
import quickfix.field.MsgType;
import quickfix.field.OrdStatus;
import quickfix.field.OrdType;
import quickfix.field.OrderID;
import quickfix.field.OrderQty;
import quickfix.field.OrigClOrdID;
import quickfix.field.Price;
import quickfix.field.RefMsgType;
import quickfix.field.RefSeqNum;
import quickfix.field.SecurityType;
import quickfix.field.SenderCompID;
import quickfix.field.SessionRejectReason;
import quickfix.field.SettlType;
import quickfix.field.Side;
import quickfix.field.StopPx;
import quickfix.field.SubscriptionRequestType;
import quickfix.field.Symbol;
import quickfix.field.TargetCompID;
import quickfix.field.Text;
import quickfix.field.TimeInForce;
import quickfix.fix50sp2.MarketDataIncrementalRefresh;
import quickfix.fix50sp2.MarketDataRequest;
import quickfix.fix50sp2.MarketDataSnapshotFullRefresh;

/**
 *
 * @author yosbel
 */
public abstract class Manager {

    private final DefaultMessageFactory messageFactory = new DefaultMessageFactory();
    private static final HashMap<SessionID, HashSet<ExecID>> execIDs = new HashMap<SessionID, HashSet<ExecID>>();

    protected OrderTableModel orderTableModel;
    protected ExecutionTableModel executionTableModel;
    protected InstrumentTableModel instrumentTableModel;
    protected OrderObservable orderObservable;

    private static final TwoWayMap sideMap = new TwoWayMap();
    private static final TwoWayMap typeMap = new TwoWayMap();
    private static final TwoWayMap tifMap = new TwoWayMap();
    private static final TwoWayMap setTypeMap = new TwoWayMap();

    public Manager(OrderTableModel orderTableModel,
            ExecutionTableModel executionTableModel,
            InstrumentTableModel instrumentTableModel, OrderObservable orderObservable) {
        this.executionTableModel = executionTableModel;
        this.orderTableModel = orderTableModel;
        this.instrumentTableModel = instrumentTableModel;
        this.orderObservable = orderObservable;
        fillValuesMap();
    }

    public abstract List<Instrument> getInstruments();
    
    public abstract void sendOrder(Order order) throws FieldNotFound;

    public abstract void cancel(Order order);

    public abstract void replace(Order order, Order newOrder);

    public abstract void fillCotizaciones(MarketDataSnapshotFullRefresh message, SessionID sessionID) throws FieldNotFound, InterruptedException, InvocationTargetException;

    public abstract void fillCotizaciones(MarketDataIncrementalRefresh message, SessionID sessionID) throws FieldNotFound, InterruptedException, InvocationTargetException;

    public void sendBusinessReject(Message message, int rejectReason, String rejectText) throws FieldNotFound, SessionNotFound {
        Message reply = createMessage(message, MsgType.BUSINESS_MESSAGE_REJECT);
        reverseRoute(message, reply);
        String refSeqNum = message.getHeader().getString(MsgSeqNum.FIELD);
        reply.setString(RefSeqNum.FIELD, refSeqNum);
        reply.setString(RefMsgType.FIELD, message.getHeader().getString(MsgType.FIELD));
        reply.setInt(BusinessRejectReason.FIELD, rejectReason);
        reply.setString(Text.FIELD, rejectText);
        Session.sendToTarget(reply);
    }

    public void executionReport(Message message, SessionID sessionID) throws FieldNotFound {
        ExecID execID = (ExecID) message.getField(new ExecID());
        if (alreadyProcessed(execID, sessionID)) {
            return;
        }

        Order order = orderTableModel.getOrder(message.getField(new ClOrdID()).getValue());//new Order(message.getField(new ClOrdID()).getValue());
        if (order == null) {
            return;
        }

        double fillSize = 0;

        try {
            LastShares lastShares = new LastShares();
            message.getField(lastShares);
            fillSize = lastShares.getValue();
        } catch (FieldNotFound e) {
            // FIX 4.0
            LeavesQty leavesQty = new LeavesQty();
            message.getField(leavesQty);
            fillSize = order.getQuantity() - leavesQty.getValue();
        }

        if (fillSize > 0) {
            // DK over the limit fills on buys
            if (order.getType().getName().equals("Limit")) {
                double limit = order.getLimit();
                double lastPx =message.isSetField(new LastPx())? message.getField(new LastPx()).getValue():0.0;
                String side = order.getSide().toString();
                if (side.equals("Buy") && lastPx > limit) {
                    try {
                        dk(message, DKReason.PRICE_EXCEEDS_LIMIT, "Price exceeds limit", order);
                    } catch (FieldNotFound | SessionNotFound e) {
                    }
                    return;
                }
            }
            order.setOpen((int) (order.getOpen() - fillSize));
            order.setExecuted((int) message.getField(new CumQty()).getValue());
            try {
                double avg=message.isSetField(new AvgPx())?message.getField(new AvgPx()).getValue():0.0;
                order.setAvgPx(avg);
            } catch (FieldNotFound e) {

            }
        }

        OrdStatus ordStatus = (OrdStatus) message.getField(new OrdStatus());

        if (ordStatus.valueEquals(OrdStatus.REJECTED)) {
            order.setRejected(true);
            order.setOpen(0);
        } else if (ordStatus.valueEquals(OrdStatus.CANCELED)
                || ordStatus.valueEquals(OrdStatus.DONE_FOR_DAY)) {
            order.setCanceled(true);
            order.setOpen(0);
        } else if (ordStatus.valueEquals(OrdStatus.NEW)) {
            if (order.isNew()) {
                order.setNew(false);
            }
        }

        try {
            order.setMessage(message.getField(new Text()).getValue());
        } catch (FieldNotFound e) {
        }

        this.orderTableModel.updateOrder(order, message.getField(new ClOrdID()).getValue());
        this.orderObservable.setOrderEvent(new OrderEvent(order));

        if (fillSize > 0) {
            Execution execution = new Execution();
            execution.setExchangeID(sessionID + message.getField(new ExecID()).getValue());
            execution.setSymbol(message.getField(new Symbol()).getValue());
            execution.setQuantity((int) fillSize);
            try {
                  double lastPx=message.isSetField(new LastPx())?message.getField(new LastPx()).getValue():0.0;
                execution.setPrice(lastPx);
            } catch (FieldNotFound ex) {
            }

            Side side = (Side) message.getField(new Side());
            execution.setSide(FIXSideToSide(side));
            String text=message.isSetField(new Text())?message.getField(new Text()).getValue():"";
            execution.setText(text);
            executionTableModel.addExecution(execution);
        }

    }

    public void cancelReject(Message message, SessionID sessionID) throws FieldNotFound {
        String id = message.getField(new ClOrdID()).getValue();
        Order order = this.orderTableModel.getOrder(id);//new Order(id);//
        if (order == null) {
            return;
        }
        if (order.getOriginalID() != null) {
            order = this.orderTableModel.getOrder(order.getOriginalID());
        }

        order.setMessage(message.getField(new Text()).getValue());

        this.orderTableModel.updateOrder(order, message.getField(new OrigClOrdID()).getValue());
    }

    public void sendSessionReject(Message message, int rejectReason) throws FieldNotFound,
            SessionNotFound {
        Message reply = createMessage(message, MsgType.REJECT);
        reverseRoute(message, reply);
        String refSeqNum = message.getHeader().getString(MsgSeqNum.FIELD);
        reply.setString(RefSeqNum.FIELD, refSeqNum);
        reply.setString(RefMsgType.FIELD, message.getHeader().getString(MsgType.FIELD));
        reply.setInt(SessionRejectReason.FIELD, rejectReason);
        Session.sendToTarget(reply);
    }

    public abstract void fillMarketSecurityListRequestInfo(Message message, SessionID sessionID) throws FieldNotFound, SessionNotFound;

    public abstract void requestSecurityList(Message message, SessionID sessionID) throws SessionNotFound, FieldNotFound;

    public abstract void fillInstrumentList(Message message, SessionID sessionID) throws FieldNotFound;

    public abstract void createMarketDataRequest(int dUpdateType, SessionID sessionID) throws SessionNotFound;

    public MarketDataRequest comunCreateMarketDataRequest(Instrument instrument, SessionID sessionID) throws SessionNotFound {
        DateFormat hourdateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
        String reqId = instrument.getAbreviatura() + "-INTL-" + hourdateFormat.format(new Date());

        MarketDataRequest mdr = new MarketDataRequest();
        mdr.setField(new MDReqID(reqId));
        mdr.setField(new SubscriptionRequestType(SubscriptionRequestType.SNAPSHOT));
        mdr.set(new AggregatedBook(AggregatedBook.BOOK_ENTRIES_TO_BE_AGGREGATED));

        MarketDataRequest.NoRelatedSym noRelatedSym = new MarketDataRequest.NoRelatedSym();
        noRelatedSym.set(new Symbol(instrument.getAbreviatura()));

        noRelatedSym.set(new SettlType(SettlType.CASH));

        if (instrument.getSecurityType() != null && !instrument.getSecurityType().equals("")) {
            noRelatedSym.set(new SecurityType(instrument.getSecurityType()));
        }
        if (instrument.getCurrency() != null && !instrument.getCurrency().equals("")) {
            noRelatedSym.set(new Currency(instrument.getCurrency()));
        }
        mdr.addGroup(noRelatedSym);

        MarketDataRequest.NoMDEntryTypes noMDEntryTypes = new MarketDataRequest.NoMDEntryTypes();

        noMDEntryTypes.set(new MDEntryType(MDEntryType.BID));
        mdr.addGroup(noMDEntryTypes);

        noMDEntryTypes.set(new MDEntryType(MDEntryType.OFFER));
        mdr.addGroup(noMDEntryTypes);

        noMDEntryTypes.set(new MDEntryType(MDEntryType.TRADE));
        mdr.addGroup(noMDEntryTypes);

        noMDEntryTypes.set(new MDEntryType(MDEntryType.INDEX_VALUE));
        mdr.addGroup(noMDEntryTypes);

        noMDEntryTypes.set(new MDEntryType(MDEntryType.OPENING_PRICE));
        mdr.addGroup(noMDEntryTypes);

        noMDEntryTypes.set(new MDEntryType(MDEntryType.CLOSING_PRICE));
        mdr.addGroup(noMDEntryTypes);

        noMDEntryTypes.set(new MDEntryType(MDEntryType.SETTLEMENT_PRICE));
        mdr.addGroup(noMDEntryTypes);

        noMDEntryTypes.set(new MDEntryType(MDEntryType.TRADING_SESSION_HIGH_PRICE));
        mdr.addGroup(noMDEntryTypes);

        noMDEntryTypes.set(new MDEntryType(MDEntryType.TRADING_SESSION_LOW_PRICE));
        mdr.addGroup(noMDEntryTypes);

        noMDEntryTypes.set(new MDEntryType(MDEntryType.TRADE_VOLUME));
        mdr.addGroup(noMDEntryTypes);

        if (sessionID.getTargetCompID().equals("ROFX")) {
            noMDEntryTypes.set(new MDEntryType(MDEntryType.OPEN_INTEREST));
            mdr.addGroup(noMDEntryTypes);
        }

        return mdr;
    }

    //Utils
    public Message populateOrder(Order order, Message newOrderSingle) {

        OrderType type = order.getType();
        if (type == OrderType.LIMIT) {
            newOrderSingle.setField(new Price(order.getLimit()));
        } else if (type == OrderType.STOP) {
            newOrderSingle.setField(new StopPx(order.getStop()));
        } else if (type == OrderType.STOP_LIMIT) {
            newOrderSingle.setField(new Price(order.getLimit()));
            newOrderSingle.setField(new StopPx(order.getStop()));
        }

        if (order.getSide() == OrderSide.SHORT_SELL
                || order.getSide() == OrderSide.SHORT_SELL_EXEMPT) {
            newOrderSingle.setField(new LocateReqd(false));
        }

        newOrderSingle.setField(tifToFIXTif(order.getTIF()));
        return newOrderSingle;
    }

    public Message populateCancelReplace(Order order, Order newOrder, Message message) {
        if (order.getQuantity() != newOrder.getQuantity()) {
            message.setField(new OrderQty(newOrder.getQuantity()));
        }
        if (!order.getLimit().equals(newOrder.getLimit())) {
            message.setField(new Price(newOrder.getLimit()));
        }
        return message;
    }

    public void send(quickfix.Message message, SessionID sessionID) {
        try {
            System.out.println("SEND ORDER --> " + message);
            Session.sendToTarget(message, sessionID);
        } catch (SessionNotFound e) {
            System.out.println(e);
        }
    }

    public Instrument getInstrument(String abreviatura, List<Instrument> instruments) throws FieldNotFound {
        Instrument instrument = instruments.stream().filter(inst -> abreviatura.equals(inst.getAbreviatura())).findAny().orElse(null);
        return instrument;
    }

    private Message createMessage(Message message, String msgType) throws FieldNotFound {
        return messageFactory.create(message.getHeader().getString(BeginString.FIELD), msgType);
    }

    private void reverseRoute(Message message, Message reply) throws FieldNotFound {
        reply.getHeader().setString(SenderCompID.FIELD,
                message.getHeader().getString(TargetCompID.FIELD));
        reply.getHeader().setString(TargetCompID.FIELD,
                message.getHeader().getString(SenderCompID.FIELD));
    }

    private boolean alreadyProcessed(ExecID execID, SessionID sessionID) {
        HashSet<ExecID> set = execIDs.get(sessionID);
        if (set == null) {
            set = new HashSet<>();
            set.add(execID);
            execIDs.put(sessionID, set);
            return false;
        } else {
            if (set.contains(execID)) {
                return true;
            }
            set.add(execID);
            return false;
        }
    }

    private void dk(Message message, char dkReason, String reasonText, Order order) throws FieldNotFound, SessionNotFound {

        OrderID orderID = (OrderID) message.getField(new OrderID());
        ExecID execID = (ExecID) message.getField(new ExecID());
        DKReason reason = new DKReason(dkReason);
        Symbol symbol = (Symbol) message.getField(new Symbol());
        Side side = (Side) message.getField(new Side());
        quickfix.fix42.DontKnowTrade dk
                = new quickfix.fix42.DontKnowTrade(
                        orderID, execID, reason, symbol, side);
        Text text = new Text(reasonText);
        dk.set(text);
        Session.sendToTarget(dk, order.getSessionID());
    }

    private OrderSide FIXSideToSide(Side side) {
        return (OrderSide) sideMap.getSecond(side);
    }

    public Side sideToFIXSide(OrderSide side) {
        return (Side) sideMap.getFirst(side);
    }

    public OrdType typeToFIXType(OrderType type) {
        return (OrdType) typeMap.getFirst(type);
    }

    public TimeInForce tifToFIXTif(OrderTIF tif) {
        return (TimeInForce) tifMap.getFirst(tif);
    }

    public SettlType setTypeToFIXSetType(OrderSettType ost) {
        return (SettlType) setTypeMap.getFirst(ost);
    }

    private void fillValuesMap() {
        sideMap.put(OrderSide.BUY, new Side(Side.BUY));
        sideMap.put(OrderSide.SELL, new Side(Side.SELL));
        sideMap.put(OrderSide.SHORT_SELL, new Side(Side.SELL_SHORT));
        sideMap.put(OrderSide.SHORT_SELL_EXEMPT, new Side(Side.SELL_SHORT_EXEMPT));
        sideMap.put(OrderSide.CROSS, new Side(Side.CROSS));
        sideMap.put(OrderSide.CROSS_SHORT, new Side(Side.CROSS_SHORT));

        typeMap.put(OrderType.MARKET, new OrdType(OrdType.MARKET));
        typeMap.put(OrderType.LIMIT, new OrdType(OrdType.LIMIT));
        typeMap.put(OrderType.STOP, new OrdType(OrdType.STOP_STOP_LOSS));//tenia stop
        typeMap.put(OrderType.STOP_LIMIT, new OrdType(OrdType.STOP_LIMIT));

        tifMap.put(OrderTIF.DAY, new TimeInForce(TimeInForce.DAY));
        tifMap.put(OrderTIF.IOC, new TimeInForce(TimeInForce.IMMEDIATE_OR_CANCEL));
        tifMap.put(OrderTIF.OPG, new TimeInForce(TimeInForce.AT_THE_OPENING));
        tifMap.put(OrderTIF.GTC, new TimeInForce(TimeInForce.GOOD_TILL_CANCEL));
        tifMap.put(OrderTIF.GTD, new TimeInForce(TimeInForce.GOOD_TILL_DATE));
        tifMap.put(OrderTIF.GTX, new TimeInForce(TimeInForce.GOOD_TILL_CROSSING));

        setTypeMap.put(OrderSettType.CASH, new SettlType(SettlType.CASH));
        setTypeMap.put(OrderSettType.NEXT_DAY, new SettlType(SettlType.NEXT_DAY));
        setTypeMap.put(OrderSettType.T_2, new SettlType(SettlType.T_2));
    }

}
