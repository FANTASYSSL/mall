package com.wch.e3mall.listener;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.wch.e3mal.pojo.TbItem;
import com.wch.e3mal.pojo.TbItemDesc;
import com.wch.e3mall.item.pojo.Item;
import com.wch.e3mall.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Copyright © 2018WCH. All rights reserved.
 * @author: FANTASY
 * @date: 2018年4月23日 下午8:59:58
 * @Description: 页面静态化
 */
public class HtmlGenListener implements MessageListener {
	
	@Autowired
	private ItemService itemService;
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	@Value("${HTML_GEN_PATH}")
	private String HTML_GEN_PATH;
	
	@Override
	public void onMessage(Message message) {
		try {
			TextMessage textMessage = (TextMessage) message;
			String text = textMessage.getText();
			Long itemId = Long.valueOf(text);
			
			Thread.sleep(100);
			
			TbItem tbItem = itemService.getItemById(itemId);
			Item item = new Item(tbItem);
			TbItemDesc itemDesc = itemService.getItemDescById(itemId);
			Map<String, Object>	map = new HashMap<>();
			
			map.put("item", item);
			map.put("itemDesc", itemDesc);
			
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			Writer out = new FileWriter(HTML_GEN_PATH + itemId + ".html" );
			template.process(map, out);
			
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
