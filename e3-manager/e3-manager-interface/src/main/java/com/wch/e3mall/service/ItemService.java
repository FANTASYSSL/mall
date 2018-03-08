package com.wch.e3mall.service;

import com.wch.e3mal.pojo.TbItem;
import com.wch.e3mall.common.pojo.EasyUIDataGridResult;

public interface ItemService {
	
	public TbItem getItemById(long itemId);
	public EasyUIDataGridResult getItemList(int page,int rows);
	
	
}
