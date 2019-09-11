/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.intl.fix4intl.Model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author mar
 */
@Entity
@Table(name = "cotizaciones_byma_fix")
public class QuotationsByma implements Serializable {

    
    @Basic(optional = false)
    @Column(name = "data",columnDefinition = "TEXT")
    private String allData;
    
    @Basic(optional = false)
    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
   
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ultimo_operado", precision = 12, scale = 0)
    private Double ultimoOperado;
    
    @Column(name = "fix_message" , columnDefinition = "TEXT")
    private String fixMessage;
    
   @Column(name = "variacion", precision = 12, scale = 0)
    private Double variacion;
    
    @Column(name = "vencimiento" , columnDefinition = "TEXT")
    private String vencimiento;
    
      
    @Column(name = "moneda" , columnDefinition = "TEXT")
    private String moneda;
    
    @Id
    @Basic(optional = false)
    @Column(name = "security_id", nullable = false, length = 255)
    private String symbol;
    
     
    @Column(name = "security_type", nullable = true, length = 255)
    private String securityType;
    
     @Column(name = "trades", nullable = true, length = 255)
    private String trades;


    public QuotationsByma() {
    }
    

    public QuotationsByma(String symbol) {
        this.symbol = symbol;
    }

    public QuotationsByma(String symbol, Date fecha) {
        this.symbol = symbol;
        this.fecha = fecha;
    }

    public String getData() {
        return allData;
    }

    public void setData(String allData) {
        this.allData = allData;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getUltimoOperado() {
        return ultimoOperado;
    }

    
    public void setUltimoOperado(double ultimoOperado) {
        this.ultimoOperado = ultimoOperado;
    }

    public double getVariacion() {
        return variacion;
    }

    public void setVariacion(double variacion) {
        this.variacion = variacion;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getFixMessage() {
        return fixMessage;
    }

    public void setFixMessage(String fixMessage) {
        this.fixMessage = fixMessage;
    }

    public void setAllData(String allData) {
        this.allData = allData;
    }

    public void setUltimoOperado(Double ultimoOperado) {
        this.ultimoOperado = ultimoOperado;
    }

    public void setVariacion(Double variacion) {
        this.variacion = variacion;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public void setSecurityType(String securityType) {
        this.securityType = securityType;
    }

    public void setTrades(String trades) {
        this.trades = trades;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (symbol != null ? symbol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof QuotationsByma)) {
            return false;
        }
        QuotationsByma other = (QuotationsByma) object;
        if ((this.symbol == null && other.symbol != null) || (this.symbol != null && !this.symbol.equals(other.symbol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.intl.fix4intl.Model.QuotationsByma[ symbol=" + symbol + " ]";
    }
 
}
