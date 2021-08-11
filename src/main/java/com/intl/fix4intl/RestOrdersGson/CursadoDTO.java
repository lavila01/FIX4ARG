package com.intl.fix4intl.RestOrdersGson;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.io.Serializable;

@Generated("com.robohorse.robopojogenerator")
public class CursadoDTO implements Serializable {

	@SerializedName("orden_trading_id")
	private int ordenTradingId;

	@SerializedName("orden_mercado_id")
	private int ordenMercadoId;

	public void setOrdenTradingId(int ordenTradingId){
		this.ordenTradingId = ordenTradingId;
	}

	public int getOrdenTradingId(){
		return ordenTradingId;
	}

	public void setOrdenMercadoId(int ordenMercadoId){
		this.ordenMercadoId = ordenMercadoId;
	}

	public int getOrdenMercadoId(){
		return ordenMercadoId;
	}

	@Override
 	public String toString(){
		return 
			"CursadoDTO{" + 
			"orden_trading_id = '" + ordenTradingId + '\'' + 
			",orden_mercado_id = '" + ordenMercadoId + '\'' + 
			"}";
		}
}