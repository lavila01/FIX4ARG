/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intl.fix4intl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.intl.fix4intl.DBController.QuotationsJpaController;
import com.intl.fix4intl.Model.Quotations;
import com.intl.fix4intl.Observable.ObservableQuotations;
import com.intl.fix4intl.Observable.OrderObservable;
import com.intl.fix4intl.Observable.QuotationEvent;
import com.intl.fix4intl.RestOrdersGson.RestOrderService;
import org.apache.commons.lang.StringUtils;
import quickfix.Message;
import quickfix.*;
import quickfix.field.*;
import quickfix.fix50sp2.*;
import quickfix.fix50sp2.component.SecListGrp;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yosbel
 */
public class RofexManager extends Manager {

    private final List<MarketSecurityListRequestInfo> marketInfoList = new LinkedList();
    public final List<Instrument> instruments = new ArrayList<>();
    //private AtomicInteger countHeartBeat = new AtomicInteger(0);
    ExecutorService executor;
    QuotationsJpaController con;

    public RofexManager(OrderTableModel orderTableModel,
                        ExecutionTableModel executionTableModel,
                        InstrumentTableModel instrumentTableModel, OrderObservable orderObservable, ObservableQuotations observableQuotations, RestOrderService restService, QuotationsJpaController con) {
        super(orderTableModel, executionTableModel, instrumentTableModel, orderObservable, observableQuotations, restService, con);
    }

    @Override
    public void sendOrder(Order order) {
        //try {
        //Instrument in = getInstrument(order.getSymbol(), this.getInstruments());

        NewOrderSingle newOrderSingle = new NewOrderSingle(
                new ClOrdID(order.getID()), sideToFIXSide(order.getSide()),
                new TransactTime(), typeToFIXType(order.getType()));
        newOrderSingle.setField(order.getCurrency());
        newOrderSingle.setField(new Account(order.getAccount()));
        newOrderSingle.setField(setTypeToFIXSetType(order.getSetType()));
        newOrderSingle.setField(new SecurityExchange(order.getSessionID().getTargetCompID()));
        newOrderSingle.set(new OrderQty(order.getQuantity()));
        newOrderSingle.set(new Symbol(order.getSymbol()));
        newOrderSingle.getHeader().setField(new StringField(DeliverToCompID.FIELD, order.getSessionID().getTargetCompID()));
        newOrderSingle.setField(new SecurityExchange(order.getSessionID().getTargetCompID()));
        newOrderSingle.set(new SettlType(idToOrderDto.get(order.getID()).getDiasLiquidacion()));
        // newOrderSingle.setField(new ExpireDate("20191210"));//
        //NewOrderSingle.NoPartyIDs noPartyIDs = new NewOrderSingle.NoPartyIDs();
        //noPartyIDs.setField(new PartyIDSource(PartyIDSource.PROPRIETARY_CUSTOM_CODE));
        //noPartyIDs.setField(new PartyID("dmax240a"));
        //noPartyIDs.setField(new PartyRole(PartyRole.TRADER_MNEMONIC));//PartyRole.ORDER_ORIGINATION_TRADER  11
        //newOrderSingle.addGroup(noPartyIDs);
        send(populateOrder(order, newOrderSingle), order.getSessionID());

//        } catch (FieldNotFound fieldNotFound) {
//            fieldNotFound.printStackTrace();
//        }

    }

    @Override
    public void cancel(Order order) {
        String id = order.generateID();
        ClOrdID clorId = new ClOrdID(id);
        Side sideToFixSide = sideToFIXSide(order.getSide());
        OrderCancelRequest message = new OrderCancelRequest(clorId, sideToFixSide, new TransactTime());

//        message.getHeader().setField(new StringField(DeliverToCompID.FIELD, "FGW"));
        message.setField(new OrderQty(order.getQuantity()));

        orderTableModel.addID(order, id);
        send(message, order.getSessionID());

    }

    @Override
    public void replace(Order order, Order newOrder) {
        ClOrdID clorId = new ClOrdID(newOrder.getID());
        Side side = sideToFIXSide(order.getSide());
        OrdType ordType = typeToFIXType(order.getType());
        OrderCancelReplaceRequest message = new OrderCancelReplaceRequest(clorId, side, new TransactTime(), ordType);
        message.getHeader().setField(new StringField(DeliverToCompID.FIELD, "FGW"));
        this.orderTableModel.addID(order, newOrder.getID());
        Message populateCancelReplace = populateCancelReplace(order, newOrder, message);
        send(populateCancelReplace, order.getSessionID());
    }

    @Override
    public void fillMarketSecurityListRequestInfo(Message message, SessionID sessionID) throws FieldNotFound, SessionNotFound {
        if (message.getField(new TradSesStatus()).valueEquals(TradSesStatus.OPEN)) {
            requestSecurityList(message, sessionID);
        }
    }

    @Override
    public void requestSecurityList(Message message, SessionID sessionID) throws SessionNotFound, FieldNotFound {

        MarketID marketID = new MarketID(message.getString(MarketID.FIELD));
        //DDA (agro), DDF (Financieros) DUAL (Otros) y MERV (ByMA)
        MarketSegmentID marketSegmentID = new MarketSegmentID(message.getString(MarketSegmentID.FIELD));
        System.out.println(marketSegmentID.getValue());
        if (StringUtils.isNotEmpty(marketSegmentID.getValue()) && !marketSegmentID.valueEquals("MATBA")) {
            DateFormat hourdateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
            Date date = new Date();
            String reqId = "SecListReq-INTL-" + hourdateFormat.format(date);

            SecurityListRequest securityListRequest = new SecurityListRequest();
            securityListRequest.setField(new SecurityReqID(reqId));
            securityListRequest.setField(new SecurityListRequestType(SecurityListType.NEWSPAPER_LIST));//NEWSPAPER_LIST));
            securityListRequest.setField(marketID);
            securityListRequest.setField(marketSegmentID);
            securityListRequest.setField(new SubscriptionRequestType(SubscriptionRequestType.SNAPSHOT_UPDATES));
            Session.sendToTarget(securityListRequest, sessionID);
        }
    }

    @Override
    public void fillInstrumentList(Message message, SessionID sessionID) throws FieldNotFound {

        int totElements = message.getInt(TotNoRelatedSym.FIELD);
        SecListGrp.NoRelatedSym noRelatedSym = new SecListGrp.NoRelatedSym();

        String securityID = message.getString(SecurityReqID.FIELD);

        for (int i = 1; i <= totElements; i++) {
            Instrument instrument = new Instrument(message.getGroup(i, noRelatedSym), sessionID, securityID);
            instruments.add(instrument);
        }
        instrumentTableModel.update(instruments);

    }

    @Override
    public void createMarketDataRequest(int dUpdateType, SessionID sessionID) throws SessionNotFound {

        for (Instrument instrument : instruments) {
            MarketDataRequest mdr = comunCreateMarketDataRequest(instrument, sessionID);
            mdr.set(new MarketDepth(0));
            mdr.setField(new MDUpdateType(dUpdateType));

            Session.sendToTarget(mdr, instrument.getSessionID());
            //countHeartBeat.incrementAndGet();

        }
    }

    @Override
    public void fillCotizaciones(MarketDataSnapshotFullRefresh message, SessionID sessionID) throws FieldNotFound, InterruptedException, InvocationTargetException {
        MarketDataSnapshotFullRefresh.NoMDEntries group = new MarketDataSnapshotFullRefresh.NoMDEntries();
        int length = message.getNoMDEntries().getValue();

        Symbol symbol = message.getSymbol();
        final Instrument instrument = getInstrument(symbol.getValue(), instruments);

        if (instrument == null) {
            System.out.println("El Symbol no esta en la lista: " + symbol.getValue());
        } else {

            for (int i = 1; i <= length; i++) {
                // Obtengo el subgrupo (imbalance, bid, offer, trade, etc)
                Group MDFullGrp = message.getGroup(i, group);
                String securityID = message.getMDReqID().getValue();
                instrument.setValues(MDFullGrp, securityID);

            }

        }
        if (instrument != null && con != null) {
            final Instrument inst = instrument;
            final Message mess = message;
            executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                try {
                    insertQuotes(inst, mess);
                } catch (Exception ex) {
                    Logger.getLogger(RofexManager.class.getName()).log(Level.SEVERE, null, ex);
                }
                 
            });
            
        }

        instrumentTableModel.update(instruments);
    }

    @Override
    public void fillCotizaciones(MarketDataIncrementalRefresh message, SessionID sessionID) throws FieldNotFound, InterruptedException, InvocationTargetException {

        MarketDataIncrementalRefresh.NoMDEntries group = new MarketDataIncrementalRefresh.NoMDEntries();
        int length = message.getNoMDEntries().getValue();
        Instrument instrument = null;
        for (int i = 1; i <= length; i++) {
            Group MDFullGrp = message.getGroup(i, new MarketDataIncrementalRefresh.NoMDEntries());
            String symbolStr = MDFullGrp.getField(new Symbol()).getValue();
            instrument = getInstrument(symbolStr, instruments);
            if (instrument != null) {
                String securityID = MDFullGrp.getField(new SecurityID()).getValue();
                securityID = securityID != null ? securityID : symbolStr;
                instrument.setValues(MDFullGrp, securityID);

            }
        }
        if (instrument != null && con != null) {
            final Instrument inst = instrument;
            final Message mess = message;

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                if (!Thread.interrupted()) {
                    try {
                        insertQuotes(inst, mess);
                    } catch (Exception ex) {
                        Logger.getLogger(RofexManager.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        }
        instrumentTableModel.update(instruments);

    }

    private synchronized void insertQuotes(Instrument instrument,Message message) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);

        try {
            Quotations q = new Quotations();
            q.setSymbol(instrument.getAbreviatura());
            q.setFecha(new Timestamp(new Date().getTime()));
            q.setFixMessage(message.toRawString());
            q.setUltimoOperado(instrument.getUltimoOperado());
            q.setVariacion(instrument.getVariacion());

            String data = mapper.writeValueAsString(instrument);

            q.setData(data);
            //notify Obsevable
            observableQuotations.setQuotationEvent(new QuotationEvent(q));
            con.createIfnotExist(q);
        } catch (FieldNotFound | JsonProcessingException | NullPointerException ex) {
            System.out.println(ex.getMessage());
        } 

    }

    @Override
    public List<Instrument> getInstruments() {
        return this.instruments;
    }
}
