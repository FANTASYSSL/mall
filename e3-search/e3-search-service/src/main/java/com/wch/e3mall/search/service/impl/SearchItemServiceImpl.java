package com.wch.e3mall.search.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wch.e3mall.common.utils.E3MallResult;
import com.wch.e3mall.search.mapper.ItemMapper;
import com.wch.e3mall.search.service.SearchItemService;

@Service
public class SearchItemServiceImpl implements SearchItemService {
	
	@Autowired
	private ItemMapper itemMapper;

	@Override
	public E3MallResult importAllItems() {
		
		return null;
	}

}
