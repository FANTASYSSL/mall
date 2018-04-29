package com.wch.e3mall.sso.controller;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wch.e3mall.common.utils.E3MallResult;
import com.wch.e3mall.common.utils.JsonUtils;
import com.wch.e3mall.sso.service.TokenService;

@Controller
public class TokenController {

	@Autowired
	private TokenService tokenService;
	
	/*@RequestMapping(value="/user/token/{token}")
	@ResponseBody
	public String getUserByToken(@PathVariable String token,String callback){
		//ajax post跨域请求方式，设置相应值如下
//		response.setHeader("Access-Control-Allow-Origin", "*");
		
		//ajax get jsonp跨域请求
		E3MallResult result = tokenService.getUserByToken(token);
		if (StringUtils.isNotBlank(callback)) {
			return callback + "(" + JsonUtils.objectToJson(result) + ")";
		}
		return JsonUtils.objectToJson(result);
	}*/
	
	@RequestMapping(value="/user/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token,String callback){
		E3MallResult result = tokenService.getUserByToken(token);
		if (StringUtils.isNotBlank(callback)) {
			//Spring4.2版本以上
			MappingJacksonValue jacksonValue = new MappingJacksonValue(result);
			jacksonValue.setJsonpFunction(callback);
			return jacksonValue;
		}
		return result;
	}
	
	
}
