package com.intl.fix4intl.RestOrdersGson;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.io.Serializable;

@Generated("com.robohorse.robopojogenerator")
public class OrderDTO implements Serializable {

	@SerializedName("tipo_instrumento")
	private TipoInstrumentoDTO tipoInstrumento;

	@SerializedName("tipo_instrumento_id")
	private int tipoInstrumentoId;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("cantidad_remanente")
	private int cantidadRemanente;

	@SerializedName("plazo_liquidacion_id")
	private int plazoLiquidacionId;

	@SerializedName("precio")
	private String precio;

	@SerializedName("fecha_concertacion")
	private String fechaConcertacion;

	@SerializedName("total")
	private String total;

	@SerializedName("automatica")
	private int automatica;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("instrumento_moneda_id")
	private int instrumentoMonedaId;

	@SerializedName("instrumento")
	private InstrumentoDTO instrumento;

	@SerializedName("comitente")
	private ComitenteDTO comitente;

	@SerializedName("id")
	private int id;

	@SerializedName("moneda_id")
	private int monedaId;

	@SerializedName("estado_id")
	private int estadoId;

	@SerializedName("comitente_id")
	private int comitenteId;

	@SerializedName("cursada_fix")
	private int cursadaFix;

	@SerializedName("tipo_precio")
	private TipoPrecioDTO tipoPrecio;

	@SerializedName("motivo_rechazo")
	private String motivoRechazo;

	@SerializedName("instrumento_moneda")
	private InstrumentoMonedaDTO instrumentoMoneda;

	@SerializedName("cantidad_operada")
	private int cantidadOperada;

	@SerializedName("dias_liquidacion")
	private String diasLiquidacion;

	@SerializedName("user_id")
	private int userId;

	@SerializedName("response")
	private String response;

	@SerializedName("tipo_operacion_id")
	private int tipoOperacionId;

	@SerializedName("fecha_liquidacion")
	private String fechaLiquidacion;

	@SerializedName("cantidad")
	private int cantidad;

	@SerializedName("instrumento_id")
	private int instrumentoId;

	@SerializedName("order_id")
	private String orderId;

	@SerializedName("tipo_precio_id")
	private int tipoPrecioId;

	@SerializedName("tipo_operacion")
	private TipoOperacionDTO tipoOperacion;

	public void setTipoInstrumento(TipoInstrumentoDTO tipoInstrumento){
		this.tipoInstrumento = tipoInstrumento;
	}

	public TipoInstrumentoDTO getTipoInstrumento(){
		return tipoInstrumento;
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

	public void setCantidadRemanente(int cantidadRemanente){
		this.cantidadRemanente = cantidadRemanente;
	}

	public int getCantidadRemanente(){
		return cantidadRemanente;
	}

	public void setPlazoLiquidacionId(int plazoLiquidacionId){
		this.plazoLiquidacionId = plazoLiquidacionId;
	}

	public int getPlazoLiquidacionId(){
		return plazoLiquidacionId;
	}

	public void setPrecio(String precio){
		this.precio = precio;
	}

	public String getPrecio(){
		return precio;
	}

	public void setFechaConcertacion(String fechaConcertacion){
		this.fechaConcertacion = fechaConcertacion;
	}

	public String getFechaConcertacion(){
		return fechaConcertacion;
	}

	public void setTotal(String total){
		this.total = total;
	}

	public String getTotal(){
		return total;
	}

	public void setAutomatica(int automatica){
		this.automatica = automatica;
	}

	public int getAutomatica(){
		return automatica;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setInstrumentoMonedaId(int instrumentoMonedaId){
		this.instrumentoMonedaId = instrumentoMonedaId;
	}

	public int getInstrumentoMonedaId(){
		return instrumentoMonedaId;
	}

	public void setInstrumento(InstrumentoDTO instrumento){
		this.instrumento = instrumento;
	}

	public InstrumentoDTO getInstrumento(){
		return instrumento;
	}

	public void setComitente(ComitenteDTO comitente){
		this.comitente = comitente;
	}

	public ComitenteDTO getComitente(){
		return comitente;
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

	public void setEstadoId(int estadoId){
		this.estadoId = estadoId;
	}

	public int getEstadoId(){
		return estadoId;
	}

	public void setComitenteId(int comitenteId){
		this.comitenteId = comitenteId;
	}

	public int getComitenteId(){
		return comitenteId;
	}

	public void setCursadaFix(int cursadaFix){
		this.cursadaFix = cursadaFix;
	}

	public int getCursadaFix(){
		return cursadaFix;
	}

	public void setTipoPrecio(TipoPrecioDTO tipoPrecio){
		this.tipoPrecio = tipoPrecio;
	}

	public TipoPrecioDTO getTipoPrecio(){
		return tipoPrecio;
	}

	public void setMotivoRechazo(String motivoRechazo){
		this.motivoRechazo = motivoRechazo;
	}

	public String getMotivoRechazo(){
		return motivoRechazo;
	}

	public void setInstrumentoMoneda(InstrumentoMonedaDTO instrumentoMoneda){
		this.instrumentoMoneda = instrumentoMoneda;
	}

	public InstrumentoMonedaDTO getInstrumentoMoneda(){
		return instrumentoMoneda;
	}

	public void setCantidadOperada(int cantidadOperada) {
		this.cantidadOperada = cantidadOperada;
	}

	public int getCantidadOperada() {
		return cantidadOperada;
	}

	public String getDiasLiquidacion() {
		return diasLiquidacion;
	}

	public void setDiasLiquidacion(String diasLiquidacion) {
		this.diasLiquidacion = diasLiquidacion;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserId() {
		return userId;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getResponse(){
		return response;
	}

	public void setTipoOperacionId(int tipoOperacionId){
		this.tipoOperacionId = tipoOperacionId;
	}

	public int getTipoOperacionId(){
		return tipoOperacionId;
	}

	public void setFechaLiquidacion(String fechaLiquidacion){
		this.fechaLiquidacion = fechaLiquidacion;
	}

	public String getFechaLiquidacion(){
		return fechaLiquidacion;
	}

	public void setCantidad(int cantidad){
		this.cantidad = cantidad;
	}

	public int getCantidad(){
		return cantidad;
	}

	public void setInstrumentoId(int instrumentoId){
		this.instrumentoId = instrumentoId;
	}

	public int getInstrumentoId(){
		return instrumentoId;
	}

	public void setOrderId(String orderId){
		this.orderId = orderId;
	}

	public String getOrderId(){
		return orderId;
	}

	public void setTipoPrecioId(int tipoPrecioId){
		this.tipoPrecioId = tipoPrecioId;
	}

	public int getTipoPrecioId(){
		return tipoPrecioId;
	}

	public void setTipoOperacion(TipoOperacionDTO tipoOperacion){
		this.tipoOperacion = tipoOperacion;
	}

	public TipoOperacionDTO getTipoOperacion(){
		return tipoOperacion;
	}

	@Override
 	public String toString(){
		return 
			"OrderDTO{" + 
			"tipo_instrumento = '" + tipoInstrumento + '\'' + 
			",tipo_instrumento_id = '" + tipoInstrumentoId + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",cantidad_remanente = '" + cantidadRemanente + '\'' + 
			",plazo_liquidacion_id = '" + plazoLiquidacionId + '\'' + 
			",precio = '" + precio + '\'' + 
			",fecha_concertacion = '" + fechaConcertacion + '\'' + 
			",total = '" + total + '\'' + 
			",automatica = '" + automatica + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",instrumento_moneda_id = '" + instrumentoMonedaId + '\'' + 
			",instrumento = '" + instrumento + '\'' + 
			",comitente = '" + comitente + '\'' + 
			",id = '" + id + '\'' + 
			",moneda_id = '" + monedaId + '\'' + 
			",estado_id = '" + estadoId + '\'' + 
			",comitente_id = '" + comitenteId + '\'' + 
			",cursada_fix = '" + cursadaFix + '\'' + 
			",tipo_precio = '" + tipoPrecio + '\'' + 
			",motivo_rechazo = '" + motivoRechazo + '\'' + 
			",instrumento_moneda = '" + instrumentoMoneda + '\'' + 
			",cantidad_operada = '" + cantidadOperada + '\'' + 
			",user_id = '" + userId + '\'' + 
			",response = '" + response + '\'' + 
			",tipo_operacion_id = '" + tipoOperacionId + '\'' + 
			",fecha_liquidacion = '" + fechaLiquidacion + '\'' + 
			",cantidad = '" + cantidad + '\'' + 
			",instrumento_id = '" + instrumentoId + '\'' + 
			",order_id = '" + orderId + '\'' + 
			",tipo_precio_id = '" + tipoPrecioId + '\'' + 
			",tipo_operacion = '" + tipoOperacion + '\'' + 
			"}";
		}
}