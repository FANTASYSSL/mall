package com.wch.e3mall.sso.service;

import com.wch.e3mall.common.utils.E3MallResult;

public interface TokenService {

	E3MallResult getUserByToken(String token);
	
}
