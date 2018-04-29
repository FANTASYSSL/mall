package com.wch.e3mall.cart.service;

import java.util.List;

import com.wch.e3mal.pojo.TbItem;
import com.wch.e3mall.common.utils.E3MallResult;

public interface CartService {

	E3MallResult addCart(long userId,long itemId,int num);
	E3MallResult mergeCart(long userId,List<TbItem> itemList);
	List<TbItem> getCartList(long userId);
	E3MallResult updateCartNum(long userId,long itemId,int num);
	E3MallResult deleteCartItem(long userId,long itemId);
}
