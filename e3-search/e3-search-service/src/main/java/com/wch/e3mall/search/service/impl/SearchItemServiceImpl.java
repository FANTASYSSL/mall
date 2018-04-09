package com.wch.e3mall.search.service.impl;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wch.e3mall.common.pojo.SearchItem;
import com.wch.e3mall.common.utils.E3MallResult;
import com.wch.e3mall.search.mapper.ItemMapper;
import com.wch.e3mall.search.service.SearchItemService;

@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private SolrServer solrServer;

	@Override
	public E3MallResult importAllItems() {
		List<SearchItem> itemList = itemMapper.getItemList();

		try {
			if (itemList != null && itemList.size() > 0) {
				for (SearchItem searchItem : itemList) {
					// 创建文档对象
					SolrInputDocument document = new SolrInputDocument();
					// 向文档中添加域
					document.addField("id", searchItem.getId());
					document.addField("item_title", searchItem.getTitle());
					document.addField("item_sell_point", searchItem.getSell_point());
					document.addField("item_price", searchItem.getPrice());
					document.addField("item_image", searchItem.getImage());
					document.addField("item_category_name", searchItem.getCategory_name());
					// 写入索引库
					solrServer.add(document);
				}
				//一定要 注意commit，不然无法导入索引库
				solrServer.commit();
			}
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
			return E3MallResult.build(500, "导入索引失败！");
		}
		return E3MallResult.ok();
	}

}
