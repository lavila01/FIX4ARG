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
import com.intl.fix4intl.Observable.OrderObservable;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import quickfix.FieldNotFound;
import quickfix.Group;
import quickfix.Message;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.StringField;
import quickfix.field.Account;
import quickfix.field.ClOrdID;
import quickfix.field.Currency;
import quickfix.field.DeliverToCompID;
import quickfix.field.ExpireDate;
import quickfix.field.MDUpdateType;
import quickfix.field.MarketDepth;
import quickfix.field.MarketID;
import quickfix.field.MarketSegmentID;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.PartyIDSource;
import quickfix.field.PartyRole;
import quickfix.field.SecurityID;
import quickfix.field.SecurityListRequestType;
import quickfix.field.SecurityListType;
import quickfix.field.SecurityReqID;
import quickfix.field.SecurityType;
import quickfix.field.SettlType;
import quickfix.field.Side;
import quickfix.field.SubscriptionRequestType;
import quickfix.field.Symbol;
import quickfix.field.TotNoRelatedSym;
import quickfix.field.TradSesStatus;
import quickfix.field.TransactTime;
import quickfix.fix50sp2.MarketDataIncrementalRefresh;
import quickfix.fix50sp2.MarketDataRequest;
import quickfix.fix50sp2.MarketDataSnapshotFullRefresh;
import quickfix.fix50sp2.NewOrderSingle;
import quickfix.fix50sp2.OrderCancelReplaceRequest;
import quickfix.fix50sp2.OrderCancelRequest;
import quickfix.fix50sp2.SecurityListRequest;
import quickfix.fix50sp2.component.SecListGrp;

/**
 *
 * @author yosbel
 */
public class RofexManager extends Manager {

    private final List<MarketSecurityListRequestInfo> marketInfoList = new LinkedList();
    public final List<Instrument> instruments = new ArrayList<>();
    private AtomicInteger countHeartBeat = new AtomicInteger(0);

    public RofexManager(OrderTableModel orderTableModel,
            ExecutionTableModel executionTableModel,
            InstrumentTableModel instrumentTableModel, OrderObservable orderObservable) {
        super(orderTableModel, executionTableModel, instrumentTableModel, orderObservable);
    }

    @Override
    public void sendOrder(Order order) throws FieldNotFound {
        Instrument in = this.getInstrument(order.getSymbol(), this.getInstruments());
        if (in != null) {
            NewOrderSingle newOrderSingle = new NewOrderSingle(
                    new ClOrdID(order.getID()), sideToFIXSide(order.getSide()),
                    new TransactTime(), typeToFIXType(order.getType()));
            newOrderSingle.set(new Currency(in.getCurrency()));
            newOrderSingle.setField(new Account("111786"));//leonel
            newOrderSingle.setField(setTypeToFIXSetType(order.getSetType()));
//            newOrderSingle.setField(new SecurityExchange("XMEV"));
            newOrderSingle.set(new OrderQty(order.getQuantity()));
            newOrderSingle.set(new Symbol(in.getAbreviatura()));
//            newOrderSingle.getHeader().setField(new StringField(DeliverToCompID.FIELD, "FGW"));
            newOrderSingle.setField(new SecurityType(in.getSecurityType()));
            newOrderSingle.setField(new ExpireDate("20191210"));

            NewOrderSingle.NoPartyIDs noPartyIDs = new NewOrderSingle.NoPartyIDs();
            noPartyIDs.setField(new PartyIDSource(PartyIDSource.PROPRIETARY_CUSTOM_CODE));
//            noPartyIDs.setField(new PartyID("dmax240a"));
            noPartyIDs.setField(new PartyRole(PartyRole.TRADER_MNEMONIC));
            newOrderSingle.addGroup(noPartyIDs);
            send(populateOrder(order, newOrderSingle), order.getSessionID());

        } else {
            System.out.println("Instrument NOT FOUND");
        }
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
        System.out.println("tradig status ROFEX --> " + message);
        if (message.getField(new TradSesStatus()).valueEquals(TradSesStatus.OPEN)) {

            List<MarketSecurityListRequestInfo> marketListTemp = marketInfoList.stream().filter(m -> m.getSessionID().equals(sessionID)).collect(Collectors.toList());
            if (marketListTemp.isEmpty()) {
                List<MarketSegmentID> marketSegmentIDList = new LinkedList<>();
                marketSegmentIDList.add(new MarketSegmentID(message.getString(MarketSegmentID.FIELD)));
                MarketSecurityListRequestInfo marketInfoTest = new MarketSecurityListRequestInfo(sessionID, marketSegmentIDList);
                marketInfoList.add(marketInfoTest);
            } else {
                marketInfoList.stream().filter(m -> m.getSessionID().equals(sessionID)).collect(Collectors.toList())
                        .get(0)
                        .getMarketSegmentID().add(new MarketSegmentID(message.getString(MarketSegmentID.FIELD)));
            }

            requestSecurityList(message, sessionID);
        }

    }

    @Override
    public void requestSecurityList(Message message, SessionID sessionID) throws SessionNotFound, FieldNotFound {

        MarketID marketID = new MarketID(message.getString(MarketID.FIELD));
        MarketSegmentID marketSegmentID = new MarketSegmentID(message.getString(MarketSegmentID.FIELD));
        DateFormat hourdateFormat = new SimpleDateFormat("ddMMyyyyHHmmss");
        Date date = new Date();
        String reqId = "SecListReq-INTL-" + hourdateFormat.format(date);

        SecurityListRequest securityListRequest = new SecurityListRequest();
        securityListRequest.setField(new SecurityReqID(reqId));
        securityListRequest.setField(new SecurityListRequestType(SecurityListType.NEWSPAPER_LIST));
        securityListRequest.setField(marketID);
        securityListRequest.setField(marketSegmentID);
        securityListRequest.setField(new SubscriptionRequestType(SubscriptionRequestType.SNAPSHOT_UPDATES));
        Session.sendToTarget(securityListRequest, sessionID);
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
            countHeartBeat = new AtomicInteger(1);

        }
    }

    @Override
    public void fillCotizaciones(MarketDataSnapshotFullRefresh message, SessionID sessionID) throws FieldNotFound, InterruptedException, InvocationTargetException {
        MarketDataSnapshotFullRefresh.NoMDEntries group = new MarketDataSnapshotFullRefresh.NoMDEntries();
        int length = message.getNoMDEntries().getValue();

        Symbol symbol = message.getSymbol();
        Instrument instrument = getInstrument(symbol.getValue(), instruments);

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
        if (instrument != null) {
            insertQuotes(instrument, message);
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
        if (instrument != null) {
            insertQuotes(instrument, message);
        }
        instrumentTableModel.update(instruments);

    }

    private void insertQuotes(Instrument instrument, Message message) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);

        try {
            Quotations q = new Quotations();
            q.setSymbol(instrument.getAbreviatura());
            q.setFecha(new Timestamp(new Date().getTime()));
            q.setFixMessage(message.toRawString());
            q.setUltimoOperado(instrument.getUltimoOperado());
            q.setVariacion(instrument.getVariacion());
            if (instrument.getSecurityID() != null) {
                q.setSymbol(instrument.getSecurityID());
            }

            String data = mapper.writeValueAsString(instrument);
            System.out.println("DATA: " + data);
            q.setData(data);
            QuotationsJpaController c = new QuotationsJpaController();
            c.createIfnotExist(q);
        } catch (FieldNotFound | JsonProcessingException ex) {
        } catch (NullPointerException ex) {
            String x = ex.getMessage();
        } catch (Exception ex) {
        }

    }

    @Override
    public List<Instrument> getInstruments() {
        return this.instruments;
    }
}
