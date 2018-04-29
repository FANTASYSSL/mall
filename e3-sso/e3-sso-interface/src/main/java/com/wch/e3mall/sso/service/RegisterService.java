package com.wch.e3mall.sso.service;

import com.wch.e3mal.pojo.TbUser;
import com.wch.e3mall.common.utils.E3MallResult;

public interface RegisterService {

	E3MallResult checkData(String param,int type);
	E3MallResult register(TbUser user);
}
