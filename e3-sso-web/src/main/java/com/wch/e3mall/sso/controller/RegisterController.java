package com.wch.e3mall.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.wch.e3mal.pojo.TbUser;
import com.wch.e3mall.common.utils.E3MallResult;
import com.wch.e3mall.sso.service.RegisterService;

@Controller
public class RegisterController {
	
	@Autowired
	private RegisterService registerService;
	
	@RequestMapping("/page/register")
	public String showRegister(){
		return "register";
	}
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3MallResult checkData(@PathVariable String param,@PathVariable Integer type){
		E3MallResult result = registerService.checkData(param, type);
		return result;
	}
	
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public E3MallResult register(TbUser user){
		E3MallResult result = registerService.register(user);
		return result;
	}
	

}
