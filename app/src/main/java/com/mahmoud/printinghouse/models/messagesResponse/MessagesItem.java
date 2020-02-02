package com.mahmoud.printinghouse.models.messagesResponse;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.google.gson.annotations.SerializedName;
import com.mahmoud.printinghouse.models.orderResponse.OrdersItem;


public class MessagesItem{

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("message")
	private String message;

	@SerializedName("user")
	private User user;

	@SerializedName("order")
	private Order order;

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

	public void setMessage(String message){
		this.message = message;
	}

	public String getMessage(){
		return message;
	}

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
		return user;
	}

	public void setOrder(Order order){
		this.order = order;
	}

	public Order getOrder(){
		return order;
	}


	public static final DiffUtil.ItemCallback<MessagesItem> DIFF_CALLBACK =
			new DiffUtil.ItemCallback<MessagesItem>() {
				@Override
				public boolean areItemsTheSame(MessagesItem oldItem, MessagesItem newItem) {
					return oldItem.getId() == newItem.getId();
				}

				@Override
				public boolean areContentsTheSame(MessagesItem oldItem, @NonNull MessagesItem newItem) {
					return oldItem.equals(newItem);
				}
			};


	@Override
	public boolean equals(@Nullable Object obj) {
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		return createdAt.equals(((MessagesItem) obj).createdAt)
				&& id==((MessagesItem) obj).id
				&& message.equals(((MessagesItem) obj).message)
				&& order.equals(((MessagesItem) obj).order)
				&& user.equals(((MessagesItem) obj).user);
	}


}