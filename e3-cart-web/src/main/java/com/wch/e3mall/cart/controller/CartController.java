package com.wch.e3mall.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wch.e3mal.pojo.TbItem;
import com.wch.e3mal.pojo.TbUser;
import com.wch.e3mall.cart.service.CartService;
import com.wch.e3mall.common.utils.CookieUtils;
import com.wch.e3mall.common.utils.E3MallResult;
import com.wch.e3mall.common.utils.JsonUtils;
import com.wch.e3mall.service.ItemService;

@Controller
public class CartController {

	@Value("${COOKIE_CART_KEY}")
	private String COOKIE_CART_KEY;
	
	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private CartService cartService; 
	
	/**
	 * @author: FANTASY
	 * @return: String
	 * @Description: 添加购物车
	 */
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId,@RequestParam(defaultValue="1") Integer num,
			HttpServletRequest request,HttpServletResponse response){
		//判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			E3MallResult result = cartService.addCart(user.getId(), itemId, num);
			return "cartSuccess";
		}
		//**未登录
		// 1、从cookie中查询商品列表。
		List<TbItem> cartList = getCartList(request);
		// 2、判断商品在商品列表中是否存在。
		boolean hasItem = false;
		if (cartList != null && cartList.size() > 0) {
			for (TbItem item : cartList) {
				if (item.getId().longValue() == itemId.longValue()) {
					item.setNum(item.getNum() + num);
					hasItem = true;
					break;
				}
			}
		}
		//不存在
		if (!hasItem) {
			TbItem tbItem = itemService.getItemById(itemId);
			if (tbItem != null) {
				String image = tbItem.getImage();
				if (StringUtils.isNotBlank(image)) {
					String[] split = image.split(",");
					tbItem.setImage(split[0]);
				}
				tbItem.setNum(num);
				cartList.add(tbItem);
			}
		}
		CookieUtils.setCookie(request, response, COOKIE_CART_KEY,
				JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		
		return "cartSuccess";
	}
	
	/**
	 * @author: FANTASY
	 * @return: String
	 * @Description: 展示购物车列表
	 */
	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request,HttpServletResponse response,Model model){
		//从cookie中获取购物车列表
		List<TbItem> cartList = getCartList(request);
		
		//判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			//如果登录，则从cookie中取购物车列表
			//如果不为空，把cookie中的购物车与服务端商品合并
			cartService.mergeCart(user.getId(), cartList);
			//把cookie中的购物车删除
			CookieUtils.deleteCookie(request, response, COOKIE_CART_KEY);
			//从服务端获取购物车商品列表
			cartList = cartService.getCartList(user.getId());
		}
		//把列表传递给页面
		model.addAttribute("cartList", cartList);
		return "cart";
	}
	
	/**
	 * @author: FANTASY
	 * @return: E3MallResult
	 * @Description: 更新购物车数量
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3MallResult updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,
			HttpServletRequest request,HttpServletResponse response){
		//判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			E3MallResult result = cartService.updateCartNum(user.getId(), itemId, num);
			return E3MallResult.ok();
		}
		
		//从cookie中取购物车列表
		List<TbItem> cartList = getCartList(request);
		//遍历列表寻找商品
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().longValue() == itemId.longValue()) {
				tbItem.setNum(num);
				break;
			}
		}
		//把购物车cookie重新写入
		CookieUtils.setCookie(request, response, COOKIE_CART_KEY, JsonUtils.objectToJson(cartList)
				, COOKIE_CART_EXPIRE, true);
		return E3MallResult.ok();
	}
	
	/**
	 * @author: FANTASY
	 * @return: String
	 * @Description: 删除购物车商品
	 */
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId,HttpServletRequest request,
			HttpServletResponse response){
		
		//判断用户是否为登录状态
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			cartService.deleteCartItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		
		//去除购物车列表
		List<TbItem> cartList = getCartList(request);
		//遍历列表寻找商品
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().longValue() == itemId.longValue()) {
				cartList.remove(tbItem);
				break;
			}
		}
		//把购物车cookie重新写入
		CookieUtils.setCookie(request, response, COOKIE_CART_KEY, JsonUtils.objectToJson(cartList)
				, COOKIE_CART_EXPIRE, true);
		//重定向购物车列表
		return "redirect:/cart/cart.html";
	}
	
	/**
	 * @author: FANTASY
	 * @return: List<TbItem>
	 * @Description:从cookie中取购物车列表
	 */
	private List<TbItem> getCartList(HttpServletRequest request){
		
		String json = CookieUtils.getCookieValue(request, COOKIE_CART_KEY, true);
		
		if (StringUtils.isNotBlank(json)) {
			List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
			return list;
		}
		return new ArrayList<>();
	}
	
	
	
}
