package com.intl.fix4intl.RestOrdersGson;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

@Generated("com.robohorse.robopojogenerator")
public class TipoInstrumentoDTO implements Serializable {

	@SerializedName("descripcion")
	private String descripcion;

	@SerializedName("requiere_plazo_liquidacion")
	private int requierePlazoLiquidacion;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("opera_web")
	private int operaWeb;

	@SerializedName("id")
	private int id;

	public void setDescripcion(String descripcion){
		this.descripcion = descripcion;
	}

	public String getDescripcion(){
		return descripcion;
	}

	public void setRequierePlazoLiquidacion(int requierePlazoLiquidacion){
		this.requierePlazoLiquidacion = requierePlazoLiquidacion;
	}

	public int getRequierePlazoLiquidacion(){
		return requierePlazoLiquidacion;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
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

	@Override
 	public String toString(){
		return 
			"TipoInstrumentoDTO{" + 
			"descripcion = '" + descripcion + '\'' + 
			",requiere_plazo_liquidacion = '" + requierePlazoLiquidacion + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",opera_web = '" + operaWeb + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}