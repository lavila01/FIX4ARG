package com.intl.fix4intl.RestOrdersGson;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

@Generated("com.robohorse.robopojogenerator")
public class TipoPrecioDTO implements Serializable {

	@SerializedName("descripcion")
	private String descripcion;

	@SerializedName("codigo_mercado")
	private String codigoMercado;

	@SerializedName("id")
	private int id;

	public void setDescripcion(String descripcion){
		this.descripcion = descripcion;
	}

	public String getDescripcion(){
		return descripcion;
	}

	public void setCodigoMercado(String codigoMercado){
		this.codigoMercado = codigoMercado;
	}

	public String getCodigoMercado(){
		return codigoMercado;
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
			"TipoPrecioDTO{" + 
			"descripcion = '" + descripcion + '\'' + 
			",codigo_mercado = '" + codigoMercado + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}