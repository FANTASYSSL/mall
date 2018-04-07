package com.wch.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wch.e3mal.pojo.TbContent;
import com.wch.e3mall.common.pojo.EasyUIDataGridResult;
import com.wch.e3mall.common.utils.E3MallResult;
import com.wch.e3mall.content.service.ContentService;

@Controller
public class ContentController {
	
	@Autowired
	private ContentService contentService;
	
	@RequestMapping(value="/content/save",method=RequestMethod.POST)
	@ResponseBody
	public E3MallResult addContent(TbContent content){
		E3MallResult result = contentService.addContent(content);
		return result;
	}
	
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult getContentsByCategoryId(Integer categoryId,Integer page,Integer rows){
		EasyUIDataGridResult result = contentService.findContentByCategoryId(categoryId, page, rows);
		return result;
	}
	

}
