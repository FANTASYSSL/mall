package com.wch.e3mall.search.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;

import com.wch.e3mall.common.pojo.SearchItem;
import com.wch.e3mall.search.mapper.ItemMapper;

public class ItemAddMessageListener implements MessageListener {
	
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;

	@Override
	public void onMessage(Message message) {
		
		try {
			TextMessage textMessage = (TextMessage) message; 
			String text = textMessage.getText();
			Long itemId = Long.valueOf(text);
			//等待事物提交
			Thread.sleep(1000);
			SearchItem searchItem = itemMapper.getItemById(itemId);
			// 创建文档对象
			SolrInputDocument document = new SolrInputDocument();
			// 向文档中添加域
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			
			solrServer.add(document);
			solrServer.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
