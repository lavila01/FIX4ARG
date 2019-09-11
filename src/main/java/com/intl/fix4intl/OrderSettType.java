/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intl.fix4intl;

import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author mar
 */


public class OrderSettType {
    static private Map<String, OrderSettType> known = new HashMap<String, OrderSettType>();
    static public final OrderSettType CASH = new OrderSettType("CASH");
    static public final OrderSettType NEXT_DAY = new OrderSettType("NEXT_DAY");
    static public final OrderSettType T_2 = new OrderSettType("T_2");
    

    static private OrderSettType[] array =
        { CASH, NEXT_DAY, T_2};

    private String name;

    private OrderSettType(String name) {
        this.name = name;
        synchronized(OrderSettType.class) {
            known.put(name, this);
        }
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    static public Object[] toArray() {
        return array;
    }

    public static OrderSettType parse(String type)
    throws IllegalArgumentException {
        OrderSettType result = known.get(type);
        if(result == null) {
            throw new IllegalArgumentException
            ("OrderSide:  " + type + " is unknown.");
        }
        return result;
    }
}
