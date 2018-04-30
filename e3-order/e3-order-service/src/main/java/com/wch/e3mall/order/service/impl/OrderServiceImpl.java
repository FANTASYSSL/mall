package com.wch.e3mall.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wch.e3mal.dao.TbOrderItemMapper;
import com.wch.e3mal.dao.TbOrderMapper;
import com.wch.e3mal.dao.TbOrderShippingMapper;
import com.wch.e3mal.pojo.TbOrderItem;
import com.wch.e3mal.pojo.TbOrderShipping;
import com.wch.e3mall.common.redis.JedisClient;
import com.wch.e3mall.common.utils.E3MallResult;
import com.wch.e3mall.order.pojo.OrderInfo;
import com.wch.e3mall.order.service.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	@Autowired
	private JedisClient jedisClient;
	//订单自增id
	@Value("${ORDER_ID_GEN_KEY}")
	private String ORDER_ID_GEN_KEY;
	//订单起始id
	@Value("${ORDER_ID_START}")
	private String ORDER_ID_START;
	//订单详细自增id
	@Value("${ORDER_DETAIL_ID_GEN_KEY}")
	private String ORDER_DETAIL_ID_GEN_KEY;

	@Override
	public E3MallResult createOrder(OrderInfo orderInfo) {
		//生成订单号。使用redis的incr生成。
		if (!jedisClient.exists(ORDER_ID_GEN_KEY)) {
			jedisClient.set(ORDER_ID_GEN_KEY, ORDER_ID_START);
		}
		String orderId = jedisClient.incr(ORDER_ID_GEN_KEY).toString();
		//补全orderInfo的属性
		orderInfo.setOrderId(orderId);
		//1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
		orderInfo.setStatus(1);
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		//插入订单表
		orderMapper.insert(orderInfo);
		//向订单明细表插入数据。
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		if (orderItems != null && orderItems.size() > 0) {
			for (TbOrderItem tbOrderItem : orderItems) {
				//生成明细id
				String orderItemId = jedisClient.incr(ORDER_DETAIL_ID_GEN_KEY).toString();
				//补全pojo的属性
				tbOrderItem.setId(orderItemId);
				tbOrderItem.setOrderId(orderId);
				//向明细表插入数据
				orderItemMapper.insert(tbOrderItem);
			}
		}
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		//向订单物流表插入数据
		orderShippingMapper.insert(orderShipping);
		//返回E3Result，包含订单号
		return E3MallResult.ok(orderId);
	}

	
	
}
