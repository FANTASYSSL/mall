package com.wch.e3mall.service;

import java.util.List;

import com.wch.e3mall.common.pojo.EasyUITreeNode;

public interface ItemCatService {
	
	public List<EasyUITreeNode> getCatList(long parentId);
	
	
}
