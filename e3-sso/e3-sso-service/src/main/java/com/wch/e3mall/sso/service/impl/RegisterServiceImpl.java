package com.wch.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.wch.e3mal.dao.TbUserMapper;
import com.wch.e3mal.pojo.TbUser;
import com.wch.e3mal.pojo.TbUserExample;
import com.wch.e3mal.pojo.TbUserExample.Criteria;
import com.wch.e3mall.common.utils.E3MallResult;
import com.wch.e3mall.sso.service.RegisterService;

@Service
public class RegisterServiceImpl implements RegisterService {
	
	@Autowired
	private TbUserMapper userMapper;

	@Override
	public E3MallResult checkData(String param, int type) {
		
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		if (type == 1) {
			criteria.andUsernameEqualTo(param);
		}else if (type == 2) {
			criteria.andPhoneEqualTo(param);
		}else if (type == 3) {
			criteria.andEmailEqualTo(param);
		}else {
			return E3MallResult.build(400, "数据类型错误！！");
		}
		
		List<TbUser> users = userMapper.selectByExample(example);
		
		if (users != null && users.size() > 0) {
			return E3MallResult.ok(false);
		}
		
		return E3MallResult.ok(true);
	}

	@Override
	public E3MallResult register(TbUser user) {
		if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())
				|| StringUtils.isBlank(user.getPhone())) {
			return E3MallResult.build(400, "用户数据不完整，注册失败！");
		}
		
		E3MallResult result = checkData(user.getUsername(), 1);
		if (!(boolean) result.getData()) {
			return E3MallResult.build(400, "此用户名已被占用！");
		}
		result = checkData(user.getPhone(), 2);
		if (!(boolean) result.getData()) {
			return E3MallResult.build(400, "此手机号已被占用！");
		}
		/*result = checkData(user.getEmail(), 3);
		if (!(boolean) result.getData()) {
			return E3MallResult.build(400, "此邮箱已被占用！");
		}*/
		user.setCreated(new Date());
		user.setUpdated(new Date());
		String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Password);
		//把用户数据插入到数据库中
		userMapper.insert(user);
		//返回添加成功
		return E3MallResult.ok();
	}

}
