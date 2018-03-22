package com.wch.e3mall.content.service;

import java.util.List;

import com.wch.e3mall.common.pojo.EasyUITreeNode;
import com.wch.e3mall.common.utils.E3MallResult;

public interface ContentCategoryService {

	public List<EasyUITreeNode> getContentCatList(long parentId);
	public E3MallResult addContentCategory(long parentId,String name);
	
}
