package com.wch.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wch.e3mal.dao.TbContentMapper;
import com.wch.e3mal.pojo.TbContent;
import com.wch.e3mal.pojo.TbContentExample;
import com.wch.e3mal.pojo.TbContentExample.Criteria;
import com.wch.e3mall.common.pojo.EasyUIDataGridResult;
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

	@Override
	public EasyUIDataGridResult findContentByCategoryId(long categoryId, int page, int rows) {
		
		PageHelper.startPage(page, rows);
		
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> contents = contentMapper.selectByExampleWithBLOBs(example);
		
		PageInfo<TbContent> pageInfo = new PageInfo<TbContent>(contents);
		
		EasyUIDataGridResult dataGridResult = new EasyUIDataGridResult();
		dataGridResult.setRows(contents);
		dataGridResult.setTotal(pageInfo.getTotal());
		
		return dataGridResult;
	}

	@Override
	public List<TbContent> getContentListByCid(long categoryId) {
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExample(example);
		return list;
	}

}
