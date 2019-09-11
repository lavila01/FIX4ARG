/*
 * File     : InstrumentTableModel.java
 *
 * Author   : Zoltan Feledy
 * 
 * Contents : This class is the TableModel for the Instrument1 Table.
 * 
 */
package com.intl.fix4intl;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class InstrumentTableModel extends AbstractTableModel {

    private final List<Instrument> allInstruments;

    private static final String[] columns
            = {"Instrumento", "Moneda", "U. Operado", "Variacion", "Vencimiento",
                "Sec. Type", "Sec. Id",  "F. Act", "F.Operativa",
                "Mercado", "Apertura", "Ajuste", "Min", "Max", "Volumen", "Cierre",
                 "Offers", "Trades", "Bids"
            };

    public InstrumentTableModel() {
        this.allInstruments = new ArrayList<>();

    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public int getRowCount() {
        return this.allInstruments.size();
    }

    @Override
    public Object getValueAt(int row, int column) {

        Instrument instrument = this.allInstruments.get(row);
        if (column == 0) {
            return instrument.getAbreviatura();
        }
        if (column == 1) {
            return instrument.getCurrency();
        }
        if (column == 2) {
            return instrument.getUltimoOperado();
        }
        if (column == 3) {
            return instrument.getVariacion();
        }
        if (column == 4) {
            return instrument.getSettlType();
        }
        if (column == 5) {
            return instrument.getSecurityType();
        }
        if (column == 6) {
            return instrument.getSecurityID();
        }
      
        if (column == 7) {
            return instrument.getFechaActualizacion();
        }
        if (column == 8) {
            return instrument.getFormaOperativa();
        }
        if (column == 9) {
            return instrument.getMercado();
        }
        if (column == 10) {
            return instrument.getApertura();
        }
        if (column == 11) {
            return instrument.getAjuste();
        }
        if (column == 12) {
            return instrument.getMinimo();
        }
        if (column == 13) {
            return instrument.getMaximo();
        }
        if (column == 14) {
            return instrument.getVolumen();
        }
        if (column == 15) {
            return instrument.getCierre();
        }
       
        if (column == 16) {
            return instrument.getOffers().toString();
        }
        if (column == 17) {
            return instrument.getTrades().toString();
        }
        if (column == 18) {
            return instrument.getBids().toString();
        }
        return "";
    }

    public void update(List<Instrument> instruments) {
        this.allInstruments.addAll(instruments);
        fireTableDataChanged();
    }
}
