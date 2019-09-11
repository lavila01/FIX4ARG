/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intl.fix4intl;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TreeMap;
import quickfix.FieldNotFound;
import quickfix.Group;
import quickfix.SessionID;
import quickfix.field.MDEntryPositionNo;
import quickfix.field.MDEntryPx;
import quickfix.field.MDEntrySize;
import quickfix.field.MDEntryType;
import quickfix.field.MaxTradeVol;
import quickfix.field.MinPriceIncrement;
import quickfix.field.MinTradeVol;
import quickfix.field.SettlType;
import quickfix.field.Symbol;

/**
 *
 * @author yosbel
 */
public class Instrument {

    @JsonProperty("abreviatura")
    public String abreviatura = "";
    @JsonProperty("SecurityID")
    public String SecurityID = "";
    @JsonProperty("SettlTypev")
    public String SettlType = "";
    @JsonProperty("MDReqID")
    public String MDReqID = "";

    @JsonProperty("fechaActualizacion")
    public String fechaActualizacion = "";

    @JsonProperty("FormaOperativa")
    public String FormaOperativa = "";

    @JsonProperty("Mercado")
    public String Mercado = "";

    @JsonProperty("Variacion")
    public Double Variacion = 0.0;

    @JsonProperty("Apertura")
    public Double Apertura = 0.0;

    @JsonProperty("Ajuste")
    public Double Ajuste = 0.0;

    @JsonProperty("Maximo")
    public Double Maximo = 0.0;

    @JsonProperty("Minimo")
    public Double Minimo = 0.0;

    @JsonProperty("Volumen")
    public Double Volumen = 0.0;

    @JsonProperty("Cierre")
    public Double Cierre = 0.0;

    @JsonProperty("Currency")
    public String Currency = "";

    @JsonProperty("SecurityType")
    public String SecurityType = "";

    @JsonProperty("UltimoOperado")
    public Double UltimoOperado = 0.0;

    @JsonProperty("ActivoSubyacente")
    public String ActivoSubyacente = "";

    // Precio de Ejercicio para una Opción
    @JsonProperty("PrecioEjercicioOpcionv")
    public Double PrecioEjercicioOpcion = 0.0;

    // Lamina minima de la opcion
    @JsonProperty("LaminaMinimaOpcion")
    public double LaminaMinimaOpcion = 0.0;

    // Fecha de Vencimiento de la opcion
    @JsonProperty("MaturityDateOpcion")
    public String MaturityDateOpcion = "";

    // Unidad de lote de la opcion
    @JsonProperty("UnidadLoteOpcion")
    public double UnidadLoteOpcion = 0.0;

    // Indica si una opcion es put o call
    @JsonProperty("PutOCallOpcion")
    public Integer PutOCallOpcion = null;

    // Fecha de liquidacion solo valido para Lebacs
    @JsonProperty("SettlementDateLebac")
    public String SettlementDateLebac = "";

    // Describe como son expresados los limites de las bandas de precio
    @JsonProperty("PriceLimitType")
    public Integer PriceLimitType = null;

    // Limite inferior de la banda de precio. Las ordenes con precios por debajo de este limite serán rechazadas
    @JsonProperty("LowLimitPricev")
    public Double LowLimitPrice = 0.0;

    // Limite superior de la banda de precio. Las ordenes con precios por encima de este limite serán rechazadas
    @JsonProperty("HighLimitPrice")
    public Double HighLimitPrice = 0.0;

    @JsonProperty("minPriceIncrem")
    public Double minPriceIncrem = 0.0;

    // La categoria del instrumento. Se usa en Rofex. Ej: Indice Soja Rosafé, Soja Fábrica, Dólar USA, etc
    @JsonProperty("Categoria")
    public String Categoria = null;

    @JsonProperty("sessionID")
    public SessionID sessionID;

    @JsonProperty("Bids")
    public TreeMap<Integer, EntradaBook> Bids = new TreeMap<>();

    @JsonProperty("Offers")
    public TreeMap<Integer, EntradaBook> Offers = new TreeMap<>();

    @JsonProperty("Trades")
    public TreeMap<String, Double> Trades = new TreeMap<String, Double>();

    public Instrument(Group g, SessionID sessionID, String securityID) throws FieldNotFound {
        if (g.isSetField(Symbol.FIELD)) {
            this.abreviatura = g.getString(Symbol.FIELD);
        }

        if (g.isSetField(quickfix.field.SecurityID.FIELD)) {
            this.SecurityID = g.getString(quickfix.field.SecurityID.FIELD);

        }

        if (g.isSetField(quickfix.field.HighLimitPrice.FIELD)) {
            this.HighLimitPrice = g.getDouble(quickfix.field.HighLimitPrice.FIELD);
        }

        if (g.isSetField(quickfix.field.LowLimitPrice.FIELD)) {
            this.LowLimitPrice = g.getDouble(quickfix.field.LowLimitPrice.FIELD);
        }

        if (g.isSetField(quickfix.field.MaturityDate.FIELD)) {
            this.MaturityDateOpcion = g.getString(quickfix.field.MaturityDate.FIELD);
        }

        if (g.isSetField(MinTradeVol.FIELD)) {
            this.Minimo = g.getDouble(MinTradeVol.FIELD);
        }

        if (g.isSetField(MaxTradeVol.FIELD)) {
            this.Maximo = g.getDouble(MaxTradeVol.FIELD);
        }

        if (g.isSetField(quickfix.field.Currency.FIELD)) {
            this.Currency = g.getString(quickfix.field.Currency.FIELD);
        }

        if (g.isSetField(MinPriceIncrement.FIELD)) {
            this.minPriceIncrem = g.getDouble(MinPriceIncrement.FIELD);
        }

        if (g.isSetField(quickfix.field.SecurityType.FIELD)) {
            this.SecurityType = g.getString(quickfix.field.SecurityType.FIELD);
        }

        if (g.isSetField(quickfix.field.SettlType.FIELD)) {
            this.SettlType = g.getString(quickfix.field.SettlType.FIELD);
        }

        this.sessionID = sessionID;
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.u");

        this.fechaActualizacion = dateFormat.format(date);
    }

    public void setValues(Group MDFullGrp, String securityID) throws FieldNotFound {
        char mDEntryType = MDFullGrp.isSetField(new MDEntryType()) ? MDFullGrp.getField(new MDEntryType()).getValue() : null;

        double entryPx = MDFullGrp.isSetField(new MDEntryPx()) ? MDFullGrp.getField(new MDEntryPx()).getValue() : 0.0;
        double entrySize = MDFullGrp.isSetField(new MDEntrySize()) ? MDFullGrp.getField(new MDEntrySize()).getValue() : 0.0;
        int entryPosition = MDFullGrp.isSetField(new MDEntryPositionNo()) ? MDFullGrp.getField(new MDEntryPositionNo()).getValue() : -1;

        this.SecurityID = securityID;
        if (securityID.indexOf('-') >= 0) {// Abrimos el SecurityID (si tiene) y obtenemos la moneda y plazo
            String[] splitSecurityID = securityID.split("-");
            this.SettlType = splitSecurityID.length > 3 && splitSecurityID[1] != null ? splitSecurityID[1] : this.SettlType;
            this.FormaOperativa = splitSecurityID.length > 3 && splitSecurityID[2] != null ? splitSecurityID[2] : this.FormaOperativa;
            this.Mercado = splitSecurityID.length > 3 && splitSecurityID[3] != null ? splitSecurityID[3] : this.Mercado;
        } else if (securityID.equals("OPT")) { // Es una OPCION                   
            this.SettlType = MDFullGrp.isSetField(new SettlType()) ? MDFullGrp.getField(new SettlType()).getValue() : this.SettlType;
        }

        EntradaBook newEntry = new EntradaBook();
        newEntry.EntryPx = entryPx;
        newEntry.EntrySize = entrySize;

        switch (mDEntryType) {
            case MDEntryType.TRADE:
                this.UltimoOperado = entryPx;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                this.Trades.put(dtf.format(now), this.UltimoOperado);
                break;
            case MDEntryType.BID:
                this.Bids.put(entryPosition, newEntry);
                break;
            case MDEntryType.OFFER:
                this.Offers.put(entryPosition, newEntry);
                break;
            case MDEntryType.OPENING_PRICE:
                this.Apertura = entryPx;
                break;
            case MDEntryType.CLOSING_PRICE:
                this.Cierre = entryPx;
                break;
            case MDEntryType.SETTLEMENT_PRICE:
                this.Ajuste = entryPx;
                break;
            case MDEntryType.TRADING_SESSION_HIGH_PRICE:
                this.Maximo = entryPx;
                break;
            case MDEntryType.TRADING_SESSION_LOW_PRICE:
                this.Minimo = entryPx;
                break;
            case MDEntryType.TRADE_VOLUME:
                this.Volumen = entryPx;
                break;
            default:
                break;
        }

        if (this.Variacion != 0.0) {
            this.Variacion = calcularVariacion();
        }
    }

    private Double calcularVariacion() {
        Double variacion;

        if (this.Apertura != 0) {
            variacion = this.UltimoOperado * 100 / this.Apertura - 100;
        } else {
            variacion = 0.0;
        }

        return variacion;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public void setSecurityID(String SecurityID) {
        this.SecurityID = SecurityID;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public String getSecurityID() {
        return SecurityID;
    }

    public String getSettlType() {
        return SettlType;
    }

    public void setSettlType(String SettlType) {
        this.SettlType = SettlType;
    }

    public String getMDReqID() {
        return MDReqID;
    }

    public void setMDReqID(String MDReqID) {
        this.MDReqID = MDReqID;
    }

    public String getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(String fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public String getFormaOperativa() {
        return FormaOperativa;
    }

    public void setFormaOperativa(String FormaOperativa) {
        this.FormaOperativa = FormaOperativa;
    }

    public String getMercado() {
        return Mercado;
    }

    public void setMercado(String Mercado) {
        this.Mercado = Mercado;
    }

    public Double getVariacion() {
        return Variacion;
    }

    public void setVariacion(Double Variacion) {
        this.Variacion = Variacion;
    }

    public Double getApertura() {
        return Apertura;
    }

    public void setApertura(Double Apertura) {
        this.Apertura = Apertura;
    }

    public Double getAjuste() {
        return Ajuste;
    }

    public void setAjuste(Double Ajuste) {
        this.Ajuste = Ajuste;
    }

    public Double getMaximo() {
        return Maximo;
    }

    public void setMaximo(Double Maximo) {
        this.Maximo = Maximo;
    }

    public Double getMinimo() {
        return Minimo;
    }

    public void setMinimo(Double Minimo) {
        this.Minimo = Minimo;
    }

    public Double getVolumen() {
        return Volumen;
    }

    public void setVolumen(Double Volumen) {
        this.Volumen = Volumen;
    }

    public Double getCierre() {
        return Cierre;
    }

    public void setCierre(Double Cierre) {
        this.Cierre = Cierre;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String Currency) {
        this.Currency = Currency;
    }

    public String getSecurityType() {
        return SecurityType;
    }

    public void setSecurityType(String SecurityType) {
        this.SecurityType = SecurityType;
    }

    public Double getUltimoOperado() {
        return UltimoOperado;
    }

    public void setUltimoOperado(Double UltimoOperado) {
        this.UltimoOperado = UltimoOperado;
    }

    public String getActivoSubyacente() {
        return ActivoSubyacente;
    }

    public void setActivoSubyacente(String ActivoSubyacente) {
        this.ActivoSubyacente = ActivoSubyacente;
    }

    public Double getPrecioEjercicioOpcion() {
        return PrecioEjercicioOpcion;
    }

    public void setPrecioEjercicioOpcion(Double PrecioEjercicioOpcion) {
        this.PrecioEjercicioOpcion = PrecioEjercicioOpcion;
    }

    public double getLaminaMinimaOpcion() {
        return LaminaMinimaOpcion;
    }

    public void setLaminaMinimaOpcion(double LaminaMinimaOpcion) {
        this.LaminaMinimaOpcion = LaminaMinimaOpcion;
    }

    public String getMaturityDateOpcion() {
        return MaturityDateOpcion;
    }

    public void setMaturityDateOpcion(String MaturityDateOpcion) {
        this.MaturityDateOpcion = MaturityDateOpcion;
    }

    public double getUnidadLoteOpcion() {
        return UnidadLoteOpcion;
    }

    public void setUnidadLoteOpcion(double UnidadLoteOpcion) {
        this.UnidadLoteOpcion = UnidadLoteOpcion;
    }

    public Integer getPutOCallOpcion() {
        return PutOCallOpcion;
    }

    public void setPutOCallOpcion(Integer PutOCallOpcion) {
        this.PutOCallOpcion = PutOCallOpcion;
    }

    public String getSettlementDateLebac() {
        return SettlementDateLebac;
    }

    public void setSettlementDateLebac(String SettlementDateLebac) {
        this.SettlementDateLebac = SettlementDateLebac;
    }

    public Integer getPriceLimitType() {
        return PriceLimitType;
    }

    public void setPriceLimitType(Integer PriceLimitType) {
        this.PriceLimitType = PriceLimitType;
    }

    public Double getLowLimitPrice() {
        return LowLimitPrice;
    }

    public void setLowLimitPrice(Double LowLimitPrice) {
        this.LowLimitPrice = LowLimitPrice;
    }

    public Double getHighLimitPrice() {
        return HighLimitPrice;
    }

    public void setHighLimitPrice(Double HighLimitPrice) {
        this.HighLimitPrice = HighLimitPrice;
    }

    public Double getMinPriceIncrem() {
        return minPriceIncrem;
    }

    public void setMinPriceIncrem(Double minPriceIncrem) {
        this.minPriceIncrem = minPriceIncrem;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String Categoria) {
        this.Categoria = Categoria;
    }

    public SessionID getSessionID() {
        return sessionID;
    }

    public void setSessionID(SessionID sessionID) {
        this.sessionID = sessionID;
    }

    public TreeMap<Integer, EntradaBook> getBids() {
        return Bids;
    }

    public void setBids(TreeMap<Integer, EntradaBook> Bids) {
        this.Bids = Bids;
    }

    public TreeMap<Integer, EntradaBook> getOffers() {
        return Offers;
    }

    public void setOffers(TreeMap<Integer, EntradaBook> Offers) {
        this.Offers = Offers;
    }

    public TreeMap<String, Double> getTrades() {
        return Trades;
    }

    public void setTrades(TreeMap<String, Double> Trades) {
        this.Trades = Trades;

    }

}

class EntradaBook {

    public Double EntryPx;
    public Double EntrySize;
}
