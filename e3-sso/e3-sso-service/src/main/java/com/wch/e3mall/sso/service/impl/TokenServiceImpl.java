package com.wch.e3mall.sso.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.wch.e3mal.pojo.TbUser;
import com.wch.e3mall.common.redis.JedisClient;
import com.wch.e3mall.common.utils.E3MallResult;
import com.wch.e3mall.common.utils.JsonUtils;
import com.wch.e3mall.sso.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {
	
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public E3MallResult getUserByToken(String token) {
		String json = jedisClient.get("SESSION:"+token);
		if (StringUtils.isBlank(json)) {
			return E3MallResult.build(201, "登录已过期！");
		}
		jedisClient.expire("SESSION:"+token, SESSION_EXPIRE);
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		return E3MallResult.ok(user);
	}

}
