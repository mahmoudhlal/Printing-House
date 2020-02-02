package com.mahmoud.printinghouse.models.SimpleResponse;

import com.google.gson.annotations.SerializedName;

public class SimpleResponse{

	@SerializedName("msg")
	private String msg;

	@SerializedName("data")
	private String data;

	@SerializedName("status")
	private boolean status;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setData(String data){
		this.data = data;
	}

	public String getData(){
		return data;
	}

	public void setStatus(boolean status){
		this.status = status;
	}

	public boolean isStatus(){
		return status;
	}
}