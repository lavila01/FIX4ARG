/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intl.fix4intl;

import java.util.HashMap;

/**
 *
 * @author mar
 */
public class TwoWayMap {
    private final HashMap<Object, Object> firstToSecond = new HashMap<>();
    private final HashMap<Object, Object> secondToFirst = new HashMap<>();

    public void put(Object first, Object second) {
        firstToSecond.put(first, second);
        secondToFirst.put(second, first);
    }

    public Object getFirst(Object first) {
        return firstToSecond.get(first);
    }

    public Object getSecond(Object second) {
        return secondToFirst.get(second);
    }

    public String toString() {
        return firstToSecond.toString() + "\n" + secondToFirst.toString();
    }
}
