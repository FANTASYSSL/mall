package com.wch.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.wch.e3mal.pojo.TbUser;
import com.wch.e3mall.common.utils.CookieUtils;
import com.wch.e3mall.common.utils.E3MallResult;
import com.wch.e3mall.sso.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor {
	
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	@Autowired
	private TokenService tokenService;

	/**
	 * @author: FANTASY
	 * @Description: 执行handle之前
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//
		//1.从cookie中去token
		String token = CookieUtils.getCookieValue(request, TOKEN_KEY);
		//2.没有token，未登录，直接方行
		if (StringUtils.isBlank(token)) {
			return true;
		}
		//3.渠道token，需要调用sso服务，根据token取用户信息
		E3MallResult e3MallResult = tokenService.getUserByToken(token);
		//4.没有取到用户信息，登录过期，直接放行
		if (e3MallResult.getStatus() != 200) {
			return true;
		}
		//5.取到用户信息， 登录状态
		TbUser user = (TbUser) e3MallResult.getData();
		//6.把用户信息放入request域中，在controller判断request是否包含user信息。放行
		request.setAttribute("user", user);
		return true;
	}

	/**
	 * @author: FANTASY
	 * @Description: handle执行之后，返回modelAndView之前
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	/**
	 * @author: FANTASY
	 * @Description: handle完成执行，返回modelAndView 之后
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
