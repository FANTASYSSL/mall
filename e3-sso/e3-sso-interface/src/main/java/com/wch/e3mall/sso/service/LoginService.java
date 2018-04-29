package com.wch.e3mall.sso.service;

import com.wch.e3mall.common.utils.E3MallResult;

public interface LoginService {

	E3MallResult userLogin(String username,String password);
	
}
