package com.mahmoud.printinghouse.models.messagesResponse;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class Data{

	@SerializedName("messages")
	private List<MessagesItem> messages;

	@SerializedName("paginate")
	private Paginate paginate;

	public void setMessages(List<MessagesItem> messages){
		this.messages = messages;
	}

	public List<MessagesItem> getMessages(){
		return messages;
	}

	public void setPaginate(Paginate paginate){
		this.paginate = paginate;
	}

	public Paginate getPaginate(){
		return paginate;
	}
}