package com.wch.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wch.e3mall.common.pojo.SearchItem;
import com.wch.e3mall.common.pojo.SearchResult;

@Repository
public class SearchDao {
	
	@Autowired
	private SolrServer solrServer;
	
	public SearchResult search(SolrQuery solrQuery) throws SolrServerException {
		//根据查询条件查询索引库
		QueryResponse response = solrServer.query(solrQuery);
		SolrDocumentList documentList = response.getResults();
		//获取总记录数
		long numFound = documentList.getNumFound();
		SearchResult result = new SearchResult();
		result.setRecordCount((int) numFound);
		
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		
		if (documentList != null && documentList.size() >0) {
			List<SearchItem> itemList = new ArrayList<SearchItem>();
			for (SolrDocument solrDocument : documentList) {
				SearchItem item = new SearchItem();
				item.setId((String) solrDocument.get("id"));
				//item.setTitle((String) solrDocument.get("item_title"));
				item.setSell_point((String) solrDocument.get("item_sell_point"));
				item.setImage((String) solrDocument.get("item_image"));
				item.setPrice( (long) solrDocument.get("item_price"));
				item.setCategory_name((String) solrDocument.get("item_category_name"));
				//取高亮结果
				List<String> list = highlighting.get( solrDocument.get("id")).get("item_title");
				String itemTitle = "";
				if (list != null && list.size() > 0) {
					itemTitle = list.get(0);
				}else{
					itemTitle = (String) solrDocument.get("item_title");
				}
				item.setTitle(itemTitle);
				itemList.add(item);
			}
			result.setItemList(itemList);
		}
		
		return result;
	}

}
