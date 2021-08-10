package com.intl.fix4intl.RestOrdersGson;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

@Generated("com.robohorse.robopojogenerator")
public class InstrumentoMonedaDTO implements Serializable {

	@SerializedName("abreviatura")
	private String abreviatura;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("abreviatura_mercado")
	private String abreviaturaMercado;

	@SerializedName("prioridad")
	private int prioridad;

	@SerializedName("multiplicador_contrato")
	private int multiplicadorContrato;

	@SerializedName("abreviatura_mercado_limpio")
	private String abreviaturaMercadoLimpio;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("hilo")
	private int hilo;

	@SerializedName("mercado_id")
	private int mercadoId;

	@SerializedName("opera_web")
	private int operaWeb;

	@SerializedName("id")
	private int id;

	@SerializedName("instrumento_id")
	private int instrumentoId;

	@SerializedName("moneda_id")
	private int monedaId;

	public void setAbreviatura(String abreviatura){
		this.abreviatura = abreviatura;
	}

	public String getAbreviatura(){
		return abreviatura;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setAbreviaturaMercado(String abreviaturaMercado){
		this.abreviaturaMercado = abreviaturaMercado;
	}

	public String getAbreviaturaMercado(){
		return abreviaturaMercado;
	}

	public void setPrioridad(int prioridad){
		this.prioridad = prioridad;
	}

	public int getPrioridad(){
		return prioridad;
	}

	public void setMultiplicadorContrato(int multiplicadorContrato){
		this.multiplicadorContrato = multiplicadorContrato;
	}

	public int getMultiplicadorContrato(){
		return multiplicadorContrato;
	}

	public void setAbreviaturaMercadoLimpio(String abreviaturaMercadoLimpio){
		this.abreviaturaMercadoLimpio = abreviaturaMercadoLimpio;
	}

	public String getAbreviaturaMercadoLimpio(){
		return abreviaturaMercadoLimpio;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setHilo(int hilo){
		this.hilo = hilo;
	}

	public int getHilo(){
		return hilo;
	}

	public void setMercadoId(int mercadoId){
		this.mercadoId = mercadoId;
	}

	public int getMercadoId(){
		return mercadoId;
	}

	public void setOperaWeb(int operaWeb){
		this.operaWeb = operaWeb;
	}

	public int getOperaWeb(){
		return operaWeb;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setInstrumentoId(int instrumentoId){
		this.instrumentoId = instrumentoId;
	}

	public int getInstrumentoId(){
		return instrumentoId;
	}

	public void setMonedaId(int monedaId){
		this.monedaId = monedaId;
	}

	public int getMonedaId(){
		return monedaId;
	}

	@Override
 	public String toString(){
		return 
			"InstrumentoMonedaDTO{" + 
			"abreviatura = '" + abreviatura + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",abreviatura_mercado = '" + abreviaturaMercado + '\'' + 
			",prioridad = '" + prioridad + '\'' + 
			",multiplicador_contrato = '" + multiplicadorContrato + '\'' + 
			",abreviatura_mercado_limpio = '" + abreviaturaMercadoLimpio + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",hilo = '" + hilo + '\'' + 
			",mercado_id = '" + mercadoId + '\'' + 
			",opera_web = '" + operaWeb + '\'' + 
			",id = '" + id + '\'' + 
			",instrumento_id = '" + instrumentoId + '\'' + 
			",moneda_id = '" + monedaId + '\'' + 
			"}";
		}
}