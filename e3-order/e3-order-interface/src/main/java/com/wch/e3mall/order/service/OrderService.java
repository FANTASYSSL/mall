package com.wch.e3mall.order.service;

import com.wch.e3mall.common.utils.E3MallResult;
import com.wch.e3mall.order.pojo.OrderInfo;

public interface OrderService {
	E3MallResult createOrder(OrderInfo orderInfo);
}
