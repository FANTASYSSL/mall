package com.wch.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wch.e3mal.dao.TbItemCatMapper;
import com.wch.e3mal.pojo.TbItemCat;
import com.wch.e3mal.pojo.TbItemCatExample;
import com.wch.e3mal.pojo.TbItemCatExample.Criteria;
import com.wch.e3mall.common.pojo.EasyUITreeNode;
import com.wch.e3mall.service.ItemCatService;

@Service
public class ItemCatServiceImpl implements ItemCatService {
	
	@Autowired
	private TbItemCatMapper itemCatMapper;

	public List<EasyUITreeNode> getCatList( long parentId) {
		
		// 1、根据parentId查询节点列表
		TbItemCatExample example = new TbItemCatExample();
		//设置查询条件
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		// 2、转换成EasyUITreeNode列表。
		List<EasyUITreeNode> resultList = new ArrayList<>();
		for (TbItemCat itemCat : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(itemCat.getId());
			node.setText(itemCat.getName());
			node.setState(itemCat.getIsParent()?"closed":"open");
			resultList.add(node);
		}
		return resultList;
	}
	
	

}
