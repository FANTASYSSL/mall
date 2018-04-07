package com.wch.e3mall.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.wch.e3mal.pojo.TbContent;
import com.wch.e3mall.content.service.ContentService;

@Controller
public class IndexController {
	
	@Autowired
	private ContentService contentService;
	
	@Value("${CONTENT_LUNBO_ID}")
	private long CONTENT_LUNBO_ID;
	
	@RequestMapping("index")
	public String showIndex(Model model) {
		List<TbContent> list = contentService.getContentListByCid(CONTENT_LUNBO_ID);
		model.addAttribute("ad1List", list);
		return "index";
	}
	
	
}
