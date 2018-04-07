package com.wch.e3mall.content.service;

import java.util.List;

import com.wch.e3mal.pojo.TbContent;
import com.wch.e3mall.common.pojo.EasyUIDataGridResult;
import com.wch.e3mall.common.utils.E3MallResult;

public interface ContentService {
	
	public E3MallResult addContent(TbContent content);
	public List<TbContent> getContentListByCid(long categoryId);
	
	public EasyUIDataGridResult findContentByCategoryId(long categoryId,int page,int rows);
	

}
