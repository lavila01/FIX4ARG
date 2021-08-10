package com.intl.fix4intl.RestOrdersGson;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

@Generated("com.robohorse.robopojogenerator")
public class ComitenteDTO implements Serializable {

	@SerializedName("descripcion")
	private String descripcion;

	@SerializedName("oficial_nombre")
	private String oficialNombre;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("valida_riesgo")
	private int validaRiesgo;

	@SerializedName("oficial_telefono")
	private String oficialTelefono;

	@SerializedName("oficial_email")
	private String oficialEmail;

	@SerializedName("mercados")
	private List<MercadosDTO> mercados;

	@SerializedName("emails")
	private String emails;

	@SerializedName("cuenta_zeni")
	private String cuentaZeni;

	@SerializedName("cuit")
	private String cuit;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("cuenta_cliente_id")
	private String cuentaClienteId;

	@SerializedName("numero_cuenta")
	private String numeroCuenta;

	@SerializedName("grupo_economico")
	private String grupoEconomico;

	@SerializedName("id")
	private int id;

	public void setDescripcion(String descripcion){
		this.descripcion = descripcion;
	}

	public String getDescripcion(){
		return descripcion;
	}

	public void setOficialNombre(String oficialNombre){
		this.oficialNombre = oficialNombre;
	}

	public String getOficialNombre(){
		return oficialNombre;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setValidaRiesgo(int validaRiesgo){
		this.validaRiesgo = validaRiesgo;
	}

	public int getValidaRiesgo(){
		return validaRiesgo;
	}

	public void setOficialTelefono(String oficialTelefono){
		this.oficialTelefono = oficialTelefono;
	}

	public String getOficialTelefono(){
		return oficialTelefono;
	}

	public void setOficialEmail(String oficialEmail){
		this.oficialEmail = oficialEmail;
	}

	public String getOficialEmail(){
		return oficialEmail;
	}

	public void setMercados(List<MercadosDTO> mercados){
		this.mercados = mercados;
	}

	public List<MercadosDTO> getMercados(){
		return mercados;
	}

	public void setEmails(String emails){
		this.emails = emails;
	}

	public String getEmails(){
		return emails;
	}

	public void setCuentaZeni(String cuentaZeni){
		this.cuentaZeni = cuentaZeni;
	}

	public String getCuentaZeni(){
		return cuentaZeni;
	}

	public void setCuit(String cuit){
		this.cuit = cuit;
	}

	public String getCuit(){
		return cuit;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setCuentaClienteId(String cuentaClienteId){
		this.cuentaClienteId = cuentaClienteId;
	}

	public String getCuentaClienteId(){
		return cuentaClienteId;
	}

	public void setNumeroCuenta(String numeroCuenta){
		this.numeroCuenta = numeroCuenta;
	}

	public String getNumeroCuenta(){
		return numeroCuenta;
	}

	public void setGrupoEconomico(String grupoEconomico){
		this.grupoEconomico = grupoEconomico;
	}

	public String getGrupoEconomico(){
		return grupoEconomico;
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
			"ComitenteDTO{" + 
			"descripcion = '" + descripcion + '\'' + 
			",oficial_nombre = '" + oficialNombre + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",valida_riesgo = '" + validaRiesgo + '\'' + 
			",oficial_telefono = '" + oficialTelefono + '\'' + 
			",oficial_email = '" + oficialEmail + '\'' + 
			",mercados = '" + mercados + '\'' + 
			",emails = '" + emails + '\'' + 
			",cuenta_zeni = '" + cuentaZeni + '\'' + 
			",cuit = '" + cuit + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",cuenta_cliente_id = '" + cuentaClienteId + '\'' + 
			",numero_cuenta = '" + numeroCuenta + '\'' + 
			",grupo_economico = '" + grupoEconomico + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}