package com.wch.e3mall.order.pojo;

import java.io.Serializable;
import java.util.List;

import com.wch.e3mal.pojo.TbOrder;
import com.wch.e3mal.pojo.TbOrderItem;
import com.wch.e3mal.pojo.TbOrderShipping;

public class OrderInfo extends TbOrder implements Serializable {

	private List<TbOrderItem> orderItems;
	private TbOrderShipping orderShipping;

	public List<TbOrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<TbOrderItem> orderItems) {
		this.orderItems = orderItems;
	}

	public TbOrderShipping getOrderShipping() {
		return orderShipping;
	}

	public void setOrderShipping(TbOrderShipping orderShipping) {
		this.orderShipping = orderShipping;
	}

}
