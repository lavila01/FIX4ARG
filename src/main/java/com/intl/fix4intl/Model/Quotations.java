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
@Table(name = "cotizaciones_rofex_fix")
public class Quotations implements Serializable {

    
    @Basic(optional = false)
    @Column(name = "data",columnDefinition = "TEXT")
    private String allData;
    
    @Basic(optional = false)
    @Column(name = "fecha", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    
    @Column(name = "categoria", length = 255)
    private String categoria;
    
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ultimo_operado", precision = 12, scale = 0)
    private Double ultimoOperado;
    
    @Column(name = "fix_message" , columnDefinition = "TEXT")
    private String fixMessage;
    
    @Column(name = "variacion", precision = 12, scale = 0)
    private Double variacion;
    
    @Id
    @Basic(optional = false)
    @Column(name = "symbol", nullable = false, length = 255)
    private String symbol;

    public Quotations() {
    }
    

    public Quotations(String symbol) {
        this.symbol = symbol;
    }

    public Quotations(String symbol, Date fecha) {
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (symbol != null ? symbol.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Quotations)) {
            return false;
        }
        Quotations other = (Quotations) object;
        if ((this.symbol == null && other.symbol != null) || (this.symbol != null && !this.symbol.equals(other.symbol))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.intl.fix4intl.Model.Quotations[ symbol=" + symbol + " ]";
    }
 
}
