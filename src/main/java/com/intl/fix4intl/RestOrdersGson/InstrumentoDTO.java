package com.intl.fix4intl.RestOrdersGson;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

@Generated("com.robohorse.robopojogenerator")
public class InstrumentoDTO implements Serializable {

	@SerializedName("descripcion")
	private String descripcion;

	@SerializedName("abreviatura_mercado_por_default")
	private String abreviaturaMercadoPorDefault;

	@SerializedName("aforo")
	private String aforo;

	@SerializedName("unidad_cotizacion")
	private int unidadCotizacion;

	@SerializedName("activo_subyacente")
	private String activoSubyacente;

	@SerializedName("abreviatura")
	private String abreviatura;

	@SerializedName("lote")
	private int lote;

	@SerializedName("tipo_instrumento_id")
	private int tipoInstrumentoId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("abreviatura_mercado_default")
	private String abreviaturaMercadoDefault;

	@SerializedName("detalle")
	private String detalle;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("garantia_contrato")
	private String garantiaContrato;

	@SerializedName("opera_web")
	private boolean operaWeb;

	@SerializedName("id")
	private int id;

	@SerializedName("moneda_id")
	private int monedaId;

	public void setDescripcion(String descripcion){
		this.descripcion = descripcion;
	}

	public String getDescripcion(){
		return descripcion;
	}

	public void setAbreviaturaMercadoPorDefault(String abreviaturaMercadoPorDefault){
		this.abreviaturaMercadoPorDefault = abreviaturaMercadoPorDefault;
	}

	public String getAbreviaturaMercadoPorDefault(){
		return abreviaturaMercadoPorDefault;
	}

	public void setAforo(String aforo){
		this.aforo = aforo;
	}

	public String getAforo(){
		return aforo;
	}

	public void setUnidadCotizacion(int unidadCotizacion){
		this.unidadCotizacion = unidadCotizacion;
	}

	public int getUnidadCotizacion(){
		return unidadCotizacion;
	}

	public void setActivoSubyacente(String activoSubyacente){
		this.activoSubyacente = activoSubyacente;
	}

	public String getActivoSubyacente(){
		return activoSubyacente;
	}

	public void setAbreviatura(String abreviatura){
		this.abreviatura = abreviatura;
	}

	public String getAbreviatura(){
		return abreviatura;
	}

	public void setLote(int lote){
		this.lote = lote;
	}

	public int getLote(){
		return lote;
	}

	public void setTipoInstrumentoId(int tipoInstrumentoId){
		this.tipoInstrumentoId = tipoInstrumentoId;
	}

	public int getTipoInstrumentoId(){
		return tipoInstrumentoId;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setAbreviaturaMercadoDefault(String abreviaturaMercadoDefault){
		this.abreviaturaMercadoDefault = abreviaturaMercadoDefault;
	}

	public String getAbreviaturaMercadoDefault(){
		return abreviaturaMercadoDefault;
	}

	public void setDetalle(String detalle){
		this.detalle = detalle;
	}

	public String getDetalle(){
		return detalle;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setGarantiaContrato(String garantiaContrato){
		this.garantiaContrato = garantiaContrato;
	}

	public String getGarantiaContrato(){
		return garantiaContrato;
	}

	public void setOperaWeb(boolean operaWeb){
		this.operaWeb = operaWeb;
	}

	public boolean isOperaWeb(){
		return operaWeb;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
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
			"InstrumentoDTO{" + 
			"descripcion = '" + descripcion + '\'' + 
			",abreviatura_mercado_por_default = '" + abreviaturaMercadoPorDefault + '\'' + 
			",aforo = '" + aforo + '\'' + 
			",unidad_cotizacion = '" + unidadCotizacion + '\'' + 
			",activo_subyacente = '" + activoSubyacente + '\'' + 
			",abreviatura = '" + abreviatura + '\'' + 
			",lote = '" + lote + '\'' + 
			",tipo_instrumento_id = '" + tipoInstrumentoId + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",abreviatura_mercado_default = '" + abreviaturaMercadoDefault + '\'' + 
			",detalle = '" + detalle + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",garantia_contrato = '" + garantiaContrato + '\'' + 
			",opera_web = '" + operaWeb + '\'' + 
			",id = '" + id + '\'' + 
			",moneda_id = '" + monedaId + '\'' + 
			"}";
		}
}