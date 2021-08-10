package com.intl.fix4intl.RestOrdersGson;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

@Generated("com.robohorse.robopojogenerator")
public class MercadosDTO implements Serializable {

	@SerializedName("comitente_id")
	private int comitenteId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("mercado_id")
	private int mercadoId;

	@SerializedName("numero_cuenta")
	private String numeroCuenta;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	public void setComitenteId(int comitenteId){
		this.comitenteId = comitenteId;
	}

	public int getComitenteId(){
		return comitenteId;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setMercadoId(int mercadoId){
		this.mercadoId = mercadoId;
	}

	public int getMercadoId(){
		return mercadoId;
	}

	public void setNumeroCuenta(String numeroCuenta){
		this.numeroCuenta = numeroCuenta;
	}

	public String getNumeroCuenta(){
		return numeroCuenta;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"MercadosDTO{" + 
			"comitente_id = '" + comitenteId + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",mercado_id = '" + mercadoId + '\'' + 
			",numero_cuenta = '" + numeroCuenta + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}