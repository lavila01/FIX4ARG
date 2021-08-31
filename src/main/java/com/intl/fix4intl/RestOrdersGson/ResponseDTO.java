package com.intl.fix4intl.RestOrdersGson;

import com.google.gson.annotations.SerializedName;

import javax.annotation.Generated;
import java.io.Serializable;

@Generated("com.robohorse.robopojogenerator")
public class ResponseDTO implements Serializable {

    //	new = APR
//  rejected = REC
//	cancelled = CAN
    public static final String NEW = "APR";
    public static final String REJECTED = "REC";
    public static final String CANCELED = "CAN";

    @SerializedName("orden_trading_id")
    private int ordenTradingId;

    @SerializedName("ClOrdID")
    private long clOrdID;

    @SerializedName("OrderID")
    private String orderID;

	@SerializedName("Text")
	private String text;

	@SerializedName("Status")
	private String status;

	public void setOrdenTradingId(int ordenTradingId){
		this.ordenTradingId = ordenTradingId;
	}

	public int getOrdenTradingId(){
		return ordenTradingId;
	}

	public void setClOrdID(long clOrdID){
		this.clOrdID = clOrdID;
	}

	public long getClOrdID(){
		return clOrdID;
	}

	public void setOrderID(String orderID){
		this.orderID = orderID;
	}

	public String getOrderID(){
		return orderID;
	}

	public void setText(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"ResponseDTO{" + 
			"orden_trading_id = '" + ordenTradingId + '\'' + 
			",clOrdID = '" + clOrdID + '\'' + 
			",orderID = '" + orderID + '\'' + 
			",text = '" + text + '\'' + 
			",status = '" + status + '\'' + 
			"}";
		}
}