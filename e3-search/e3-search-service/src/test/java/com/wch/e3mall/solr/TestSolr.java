package com.wch.e3mall.solr;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolr {

	@Test
	public void testSolrCloud() throws Exception {
		String zkHost = "192.168.9.13:2181,192.168.9.13:2182,192.168.9.13:2183";
		CloudSolrServer solrServer = new CloudSolrServer(zkHost);
		solrServer.setDefaultCollection("collection1");
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "123");
		doc.addField("item_title", "电脑");
		doc.addField("item_price", 123456);
		doc.addField("item_category_name", "dianzi");
		solrServer.add(doc);
		solrServer.commit();
	}
	
	@Test
	public void testQueryForCloud() throws Exception {
		String zkHost = "192.168.9.13:2181,192.168.9.13:2182,192.168.9.13:2183";
		CloudSolrServer solrServer = new CloudSolrServer(zkHost);
		solrServer.setDefaultCollection("collection1");
		SolrQuery params = new SolrQuery();
		params.setQuery("id:123");
//		params.set("fq", "");
//		params.set("sort", "");
		params.setStart(0);
		params.setRows(5);
//		params.set("fl", "");
//		params.set("df", "");
//		params.setHighlight(true);
//		params.setHighlightSimplePre("");
//		params.setHighlightSimplePost("");
		
		QueryResponse response = solrServer.query(params);
		SolrDocumentList results = response.getResults();
		long numFound = results.getNumFound();
		System.out.println(numFound);
		System.out.println(results.getMaxScore());
		
		for (SolrDocument sd : results) {
			System.out.println(sd.get("id"));
			System.out.println(sd.get("item_title"));
			System.out.println(sd.get("item_price"));
			System.out.println(sd.get("item_category_name"));
		}
		
	}
	
}
