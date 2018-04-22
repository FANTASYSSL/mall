package com.wch.e3mall.service;

import com.wch.e3mal.pojo.TbItem;
import com.wch.e3mal.pojo.TbItemDesc;
import com.wch.e3mall.common.pojo.EasyUIDataGridResult;
import com.wch.e3mall.common.utils.E3MallResult;

public interface ItemService {
	
	public TbItem getItemById(long itemId);
	public EasyUIDataGridResult getItemList(int page,int rows);
	public E3MallResult addItem(TbItem item,String desc);
	public TbItemDesc getItemDescById(long itemId); 
	
	
}
