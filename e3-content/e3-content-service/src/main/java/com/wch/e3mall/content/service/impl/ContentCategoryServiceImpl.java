package com.wch.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wch.e3mal.dao.TbContentCategoryMapper;
import com.wch.e3mal.pojo.TbContentCategory;
import com.wch.e3mal.pojo.TbContentCategoryExample;
import com.wch.e3mal.pojo.TbContentCategoryExample.Criteria;
import com.wch.e3mall.common.pojo.EasyUITreeNode;
import com.wch.e3mall.common.utils.E3MallResult;
import com.wch.e3mall.content.service.ContentCategoryService;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	@Override
	public List<EasyUITreeNode> getContentCatList(long parentId) {
		// 根据parentid查询子节点列表
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbContentCategory> catList = contentCategoryMapper.selectByExample(example);
		//转换成EasyUITreeNode的列表
		if (null != catList && catList.size()>0) {
			List<EasyUITreeNode> nodes = new ArrayList<>();
			for (TbContentCategory category : catList) {
				EasyUITreeNode node = new EasyUITreeNode();
				node.setId(category.getId());
				node.setText(category.getName());
				node.setState(category.getIsParent()?"closed":"open");
				nodes.add(node);
			}
			return nodes;
		}
		return null;
	}

	@Override
	public E3MallResult addContentCategory(long parentId, String name) {
		// 1、接收两个参数：parentId、name
		// 2、向tb_content_category表中插入数据。
		// a)创建一个TbContentCategory对象
		// b)补全TbContentCategory对象的属性
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setIsParent(false);
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		//排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
		contentCategory.setSortOrder(1);
		//状态。可选值:1(正常),2(删除)
		contentCategory.setStatus(1);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		// c)向tb_content_category表中插入数据
		contentCategoryMapper.insertSelective(contentCategory);
		// 3、判断父节点的isparent是否为true，不是true需要改为true。
		TbContentCategory parentNode = contentCategoryMapper.selectByPrimaryKey(parentId);
		if (!parentNode.getIsParent()) {
			parentNode.setIsParent(true);
			contentCategoryMapper.updateByPrimaryKeySelective(parentNode);
		}
		// 4、需要主键返回。
		// 5、返回E3Result，其中包装TbContentCategory对象
		return new E3MallResult(contentCategory);
	}

}
