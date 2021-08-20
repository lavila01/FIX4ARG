/*
 * File     : InstrumentTableModel.java
 *
 * Author   : Zoltan Feledy
 * 
 * Contents : This class is the TableModel for the Instrument1 Table.
 * 
 */
package com.intl.fix4intl;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InstrumentTableModel extends AbstractTableModel {

    private final Set<Instrument> allInstruments;

    private static final String[] columns
            = {"Instrumento", "Moneda", "U. Operado", "Tipo.Orden", "Variacion", "Vencimiento",
                "Sec.Type", "Sec.Id",  "F.Act", "F.Operativa",
                "Mercado", "Apertura", "Ajuste", "Min", "Max", "Volumen", "Cierre",
                 "Offers", "Trades", "Bids"
            };

    public InstrumentTableModel() {
        this.allInstruments = new HashSet<>();

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

        Instrument instrument = new ArrayList<>(allInstruments).get(row);
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
            return instrument.getOrdType();
        }
        if (column == 4) {
            return instrument.getVariacion();
        }
        if (column == 5) {
            return instrument.getSettlType();
        }
        if (column == 6) {
            return instrument.getSecurityType();
        }
        if (column == 7) {
            return instrument.getSecurityID();
        }
      
        if (column == 8) {
            return instrument.getFechaActualizacion();
        }
        if (column == 9) {
            return instrument.getFormaOperativa();
        }
        if (column == 10) {
            return instrument.getMercado();
        }
        if (column == 11) {
            return instrument.getApertura();
        }
        if (column == 12) {
            return instrument.getAjuste();
        }
        if (column == 13) {
            return instrument.getMinimo();
        }
        if (column == 14) {
            return instrument.getMaximo();
        }
        if (column == 15) {
            return instrument.getVolumen();
        }
        if (column == 16) {
            return instrument.getCierre();
        }
       
        if (column == 17) {
            return instrument.getOffers().toString();
        }
        if (column == 18) {
            return instrument.getTrades().toString();
        }
        if (column == 19) {
            return instrument.getBids().toString();
        }
        return "";
    }

    public void update(List<Instrument> instruments) {
        instruments.forEach(instrument -> {
            allInstruments.remove(instrument);
            allInstruments.add(instrument);
        });
        fireTableDataChanged();
    }
}
