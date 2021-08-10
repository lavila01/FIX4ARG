package com.intl.fix4intl.RestOrdersGson;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

@Generated("com.robohorse.robopojogenerator")
public class TipoOperacionDTO implements Serializable {

	@SerializedName("descripcion")
	private String descripcion;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("multiplicador")
	private int multiplicador;

	@SerializedName("codigo_mercado")
	private String codigoMercado;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	public void setDescripcion(String descripcion){
		this.descripcion = descripcion;
	}

	public String getDescripcion(){
		return descripcion;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setMultiplicador(int multiplicador){
		this.multiplicador = multiplicador;
	}

	public int getMultiplicador(){
		return multiplicador;
	}

	public void setCodigoMercado(String codigoMercado){
		this.codigoMercado = codigoMercado;
	}

	public String getCodigoMercado(){
		return codigoMercado;
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
			"TipoOperacionDTO{" + 
			"descripcion = '" + descripcion + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",multiplicador = '" + multiplicador + '\'' + 
			",codigo_mercado = '" + codigoMercado + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}