package com.wch.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wch.e3mall.common.pojo.EasyUITreeNode;
import com.wch.e3mall.common.utils.E3MallResult;
import com.wch.e3mall.content.service.ContentCategoryService;

@Controller
public class ContentCatController {
	
	@Autowired
	private ContentCategoryService contentCategoryService;
	
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(
			@RequestParam(value="id",defaultValue="0") Long parentId) {
 		List<EasyUITreeNode> list = contentCategoryService.getContentCatList(parentId);
		return list;
	}
	
	@RequestMapping(value="/content/category/create",method=RequestMethod.POST)
	@ResponseBody
	public E3MallResult createCategory(Long parentId,String name){
		E3MallResult result = contentCategoryService.addContentCategory(parentId, name);
		return result;
	}
	

}
