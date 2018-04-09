package com.wch.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wch.e3mall.common.utils.E3MallResult;
import com.wch.e3mall.search.service.SearchItemService;

@Controller
public class SearchItemController {

	@Autowired
	private SearchItemService searchItemService;
	
	@RequestMapping("/index/item/import")
	@ResponseBody
	public E3MallResult importItemIndex() {
		E3MallResult result = searchItemService.importAllItems();
		return result;
	}
	
}
