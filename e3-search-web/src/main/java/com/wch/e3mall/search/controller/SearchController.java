package com.wch.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.wch.e3mall.common.pojo.SearchResult;
import com.wch.e3mall.search.service.SearchService;

@Controller
public class SearchController {
	
	@Autowired
	private SearchService  searchService;
	
	@Value("${SEARCH_RESULT_ROWS}")
	private Integer SEARCH_RESULT_ROWS;
	
	@RequestMapping(value="/search")
	public String search(String keyword,
			@RequestParam(defaultValue="1") Integer page,Model model) throws Exception{
		
//		手动解决get提交乱码
//		keyword = new String(keyword.getBytes("ISO8859-1"), "UTF-8");
		
		SearchResult result = searchService.search(keyword, page, SEARCH_RESULT_ROWS);
		model.addAttribute("query",keyword);
		model.addAttribute("totalPages",result.getTotalPages());
		model.addAttribute("page",page);
		model.addAttribute("recordCount",result.getRecordCount());
		model.addAttribute("itemList", result.getItemList());
		return "search";
	}
	

}
