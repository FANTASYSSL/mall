package com.wch.e3mall.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestCloudSolr {
	
	public static void main(String[] args) throws SolrServerException {
		ApplicationContext applicationContext = 
				new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml","classpath:spring/applicationContext-solr.xml");
		SolrServer solrServer = applicationContext.getBean(SolrServer.class);
		SolrQuery params = new SolrQuery();
		params.set("q", "id:123");
		QueryResponse response = solrServer.query(params);
		SolrDocumentList results = response.getResults();
		System.out.println(results.getNumFound());
		for (SolrDocument sd : results) {
			System.out.println(sd.get("id"));
			System.out.println(sd.get("item_title"));
			System.out.println(sd.get("item_price"));
			System.out.println(sd.get("item_category_name"));
		}
	}
}
