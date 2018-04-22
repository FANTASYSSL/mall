package com.wch.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wch.e3mall.common.pojo.SearchResult;
import com.wch.e3mall.search.dao.SearchDao;
import com.wch.e3mall.search.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDao searchDao;

	/**
	 * @author: FANTASY
	 * @Description: 商品搜索
	 */
	@Override
	public SearchResult search(String keywords, int page, int rows) throws Exception {
		//创建一个SolrQuery对象
		SolrQuery solrQuery = new SolrQuery();
		//设置查询条件
//		solrQuery.set("q", keywords);
		solrQuery.setQuery(keywords);
		//设置分页条件
		solrQuery.setStart((page-1)*rows);
		//设置rows
		solrQuery.setRows(rows);
		//设置默认搜索域
		solrQuery.set("df", "item_keywords");
		//设置高亮显示
		solrQuery.setHighlight(true);
		solrQuery.setHighlightSimplePre("<em style=\"red\">");
		solrQuery.setHighlightSimplePost("</em>");
		//执行查询
		SearchResult result = searchDao.search(solrQuery);
		//计算总页数
		int recourdCount = result.getRecordCount();
		int totalPages = (recourdCount + rows -1)/rows;
		result.setTotalPages(totalPages);
		//设置到返回结果
		return result;
	}
	
}
