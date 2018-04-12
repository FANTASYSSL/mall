package com.wch.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wch.e3mal.dao.TbContentMapper;
import com.wch.e3mal.pojo.TbContent;
import com.wch.e3mal.pojo.TbContentExample;
import com.wch.e3mal.pojo.TbContentExample.Criteria;
import com.wch.e3mall.common.pojo.EasyUIDataGridResult;
import com.wch.e3mall.common.redis.JedisClient;
import com.wch.e3mall.common.utils.E3MallResult;
import com.wch.e3mall.common.utils.JsonUtils;
import com.wch.e3mall.content.service.ContentService;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;

	//添加广告类容
	@Override
	public E3MallResult addContent(TbContent content) {
		content.setCreated(new Date());
		content.setUpdated(new Date());
		contentMapper.insert(content);
		//缓存同步
		try {
			jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
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

		// 查询redis缓存
		try {
			String jsonList = jedisClient.hget(CONTENT_LIST, categoryId + "");
			if (StringUtils.isNotBlank(jsonList)) {
				List<TbContent> list = JsonUtils.jsonToList(jsonList, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExample(example);
		
		try {
			//添加redis缓存
			if (list != null && list.size() > 0) {
				jedisClient.hset(CONTENT_LIST, categoryId+"", JsonUtils.objectToJson(list));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

}
