package com.wch.e3mall.content.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wch.e3mal.dao.TbContentMapper;
import com.wch.e3mal.pojo.TbContent;
import com.wch.e3mall.common.utils.E3MallResult;
import com.wch.e3mall.content.service.ContentService;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	
	@Override
	public E3MallResult addContent(TbContent content) {
		content.setCreated(new Date());
		content.setUpdated(new Date());
		contentMapper.insert(content);
		return E3MallResult.ok();
	}

}
