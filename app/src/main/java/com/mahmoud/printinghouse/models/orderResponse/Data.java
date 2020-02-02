package com.mahmoud.printinghouse.models.orderResponse;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("paginate")
	private Paginate paginate;

	@SerializedName("orders")
	private List<OrdersItem> orders;

	public void setPaginate(Paginate paginate){
		this.paginate = paginate;
	}

	public Paginate getPaginate(){
		return paginate;
	}

	public void setOrders(List<OrdersItem> orders){
		this.orders = orders;
	}

	public List<OrdersItem> getOrders(){
		return orders;
	}
}