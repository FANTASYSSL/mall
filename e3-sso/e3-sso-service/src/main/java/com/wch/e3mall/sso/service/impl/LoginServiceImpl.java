package com.wch.e3mall.sso.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.wch.e3mal.dao.TbUserMapper;
import com.wch.e3mal.pojo.TbUser;
import com.wch.e3mal.pojo.TbUserExample;
import com.wch.e3mal.pojo.TbUserExample.Criteria;
import com.wch.e3mall.common.redis.JedisClient;
import com.wch.e3mall.common.utils.E3MallResult;
import com.wch.e3mall.common.utils.JsonUtils;
import com.wch.e3mall.sso.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService {
	
	@Autowired
	private TbUserMapper userMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;
	
	@Override
	public E3MallResult userLogin(String username, String password) {
		
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		
		List<TbUser> users = userMapper.selectByExample(example);
		
		if (users == null || users.size() == 0) {
			return E3MallResult.build(400, "用户名或密码错误！");
		}
		TbUser user = users.get(0);
		if (!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())) {
			return E3MallResult.build(400, "用户名或密码错误！");
		}
		
		String token = UUID.randomUUID().toString();
		user.setPassword(null);
		jedisClient.set("SESSION:"+token, JsonUtils.objectToJson(user));
		jedisClient.expire("SESSION:"+token,SESSION_EXPIRE);
		
		return E3MallResult.ok(token);
	}

	
	
	
}
