package com.wch.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wch.e3mal.dao.TbItemMapper;
import com.wch.e3mal.pojo.TbItem;
import com.wch.e3mall.cart.service.CartService;
import com.wch.e3mall.common.redis.JedisClient;
import com.wch.e3mall.common.utils.E3MallResult;
import com.wch.e3mall.common.utils.JsonUtils;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisClient jedisClient;
	
	@Autowired
	private TbItemMapper itemMapper;
	
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	
	@Override
	public E3MallResult addCart(long userId, long itemId,int num) {
		//向redis中添加购物车
		//数据类型 hash key:用户id field：商品id value：商品信息
		//判断商品是否存在
		Boolean hexists = jedisClient.hexists(REDIS_CART_PRE + ":"+userId, itemId + "");
		//如果存在商品数量相加
		if (hexists) {
			String json = jedisClient.hget(REDIS_CART_PRE + ":"+userId, itemId + "");
			TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
			//数量相加
			tbItem.setNum(num);
			//写会redis
			jedisClient.hset(REDIS_CART_PRE + ":"+userId, itemId + "", JsonUtils.objectToJson(tbItem));
			return E3MallResult.ok();
		}
		//如果不存在，根据id查询商品信息
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		//数量相加
		item.setNum(num);
		String image = item.getImage();
		if (StringUtils.isNotBlank(image)) {
			item.setImage(item.getImage().split(",")[0]);
		}
		//添加购物车列表，写会redis
		jedisClient.hset(REDIS_CART_PRE + ":"+userId, itemId + "", JsonUtils.objectToJson(item));
		return E3MallResult.ok();
	}

	/**
	 * @author: FANTASY
	 * @Description: 合并cookie中与服务端购物车商品
	 */
	@Override
	public E3MallResult mergeCart(long userId, List<TbItem> itemList) {
		//遍历商品列表
		//把列表添加到购物车。
		//判断购物车中是否有此商品
		//如果有，数量相加
		//如果没有添加新的商品
		for (TbItem tbItem : itemList) {
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		return E3MallResult.ok();
	}

	/**
	 * @author: FANTASY
	 * @Description: 根据用户id获取购物车列表
	 */
	@Override
	public List<TbItem> getCartList(long userId) {
		//根据用户id查询购车列表
		List<String> list = jedisClient.hvals(REDIS_CART_PRE + ":"+userId);
		List<TbItem> itemList = new ArrayList<>();
		for (String json : list) {
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			itemList.add(item);
		}
		
		return itemList;
	}

	/**
	 * @author: FANTASY
	 * @Description: 更新购物车商品数量
	 */
	@Override
	public E3MallResult updateCartNum(long userId, long itemId, int num) {
		//从redis中取商品信息
		String json = jedisClient.hget(REDIS_CART_PRE + ":" +userId, itemId + "");
		if (StringUtils.isBlank(json) || num == 0) {
			return E3MallResult.ok();
		}
		//更新商品数量
		TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
		item.setNum(num);
		//写入redis
		jedisClient.hset(REDIS_CART_PRE + ":" +userId, itemId + "", JsonUtils.objectToJson(item));
		return E3MallResult.ok();
	}

	@Override
	public E3MallResult deleteCartItem(long userId, long itemId) {
		jedisClient.hdel(REDIS_CART_PRE + ":" +userId, itemId + "");
		return E3MallResult.ok();
	}

}
