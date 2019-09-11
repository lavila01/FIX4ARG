/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intl.fix4intl;

/**
 *
 * @author mar
 */
import quickfix.SessionID;
import quickfix.field.Currency;

public class Order implements Cloneable {

    private SessionID sessionID = null;
    private String symbol = null;
    private String account = null;
    private int quantity = 0;
    private int open = 0;
    private int executed = 0;
    private OrderSide side = OrderSide.BUY;
    private OrderType type = OrderType.MARKET;
    private OrderTIF tif = OrderTIF.DAY;
    private OrderSettType setType = OrderSettType.CASH;
    private Double limit = null;
    private Double stop = null;
    private double avgPx = 0.0;
    private double price = 0.0;
    private Currency currency=null;
    private boolean rejected = false;
    private boolean canceled = false;
    private boolean isNew = true;
    private String message = null;
    private String ID = null;
    private String originalID = null;
    private static int nextID = 1;

    public Order() {
        ID = generateID();
    }

    public Order(String ID) {
        this.ID = ID;
    }

    @Override
    public Object clone() {
        try {
            Order order = (Order) super.clone();
            order.setOriginalID(getID());
            order.setID(order.generateID());
            return order;
        } catch (CloneNotSupportedException e) {
        }
        return null;
    }

    public String generateID() {
        return Long.valueOf(System.currentTimeMillis() + (nextID++)).toString();
    }

    public SessionID getSessionID() {
        return sessionID;
    }

    public void setSessionID(SessionID sessionID) {
        this.sessionID = sessionID;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public OrderTIF getTif() {
        return tif;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
    

    public OrderSettType getSetType() {
        return setType;
    }

    public void setSetType(OrderSettType setType) {
        this.setType = setType;
    }
    

    public void setTif(OrderTIF tif) {
        this.tif = tif;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public boolean isIsNew() {
        return isNew;
    }

    public void setIsNew(boolean isNew) {
        this.isNew = isNew;
    }

    public static int getNextID() {
        return nextID;
    }

    public static void setNextID(int nextID) {
        Order.nextID = nextID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public int getExecuted() {
        return executed;
    }

    public void setExecuted(int executed) {
        this.executed = executed;
    }

    public OrderSide getSide() {
        return side;
    }

    public void setSide(OrderSide side) {
        this.side = side;
    }

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public OrderTIF getTIF() {
        return tif;
    }

    public void setTIF(OrderTIF tif) {
        this.tif = tif;
    }

    public Double getLimit() {
        return limit;
    }

    public void setLimit(Double limit) {
        this.limit = limit;
    }

    public void setLimit(String limit) {
        if (limit == null || limit.equals("")) {
            this.limit = null;
        } else {
            this.limit = new Double(limit);
        }
    }

    public Double getStop() {
        return stop;
    }

    public void setStop(Double stop) {
        this.stop = stop;
    }

    public void setStop(String stop) {
        if (stop == null || stop.equals("")) {
            this.stop = null;
        } else {
            this.stop = new Double(stop);
        }
    }

    public void setAvgPx(double avgPx) {
        this.avgPx = avgPx;
    }

    public double getAvgPx() {
        return avgPx;
    }

    public void setRejected(boolean rejected) {
        this.rejected = rejected;
    }

    public boolean getRejected() {
        return rejected;
    }

    public void setCanceled(boolean canceled) {
        this.canceled = canceled;
    }

    public boolean getCanceled() {
        return canceled;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setOriginalID(String originalID) {
        this.originalID = originalID;
    }

    public String getOriginalID() {
        return originalID;
    }
}
