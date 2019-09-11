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
public class Execution {
    private String symbol = null;
    private int quantity = 0;
    private OrderSide side = OrderSide.BUY;
    private double price;
    private String ID = null;
    private String Text = null;
    private String exchangeID = null;
    private static int nextID = 1;

    public Execution() {
        ID = Integer.toString(nextID++);
    }
    public Execution(String ID) {
        this.ID = ID;
    }

    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public OrderSide getSide() {
        return side;
    }
    public void setSide(OrderSide side) {
        this.side = side;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public String getID() {
        return ID;
    }
    public void setExchangeID(String exchangeID) {
        this.exchangeID = exchangeID;
    }
    public String getExchangeID() {
        return exchangeID;
    }

    public String getText() {
        return Text;
    }

    public void setText(String Text) {
        this.Text = Text;
    }
    
}
