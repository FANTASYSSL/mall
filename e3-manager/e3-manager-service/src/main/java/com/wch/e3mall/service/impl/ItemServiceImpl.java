package com.wch.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wch.e3mal.dao.TbItemDescMapper;
import com.wch.e3mal.dao.TbItemMapper;
import com.wch.e3mal.pojo.TbItem;
import com.wch.e3mal.pojo.TbItemDesc;
import com.wch.e3mal.pojo.TbItemExample;
import com.wch.e3mall.common.pojo.EasyUIDataGridResult;
import com.wch.e3mall.common.redis.JedisClient;
import com.wch.e3mall.common.utils.E3MallResult;
import com.wch.e3mall.common.utils.IDUtils;
import com.wch.e3mall.common.utils.JsonUtils;
import com.wch.e3mall.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;
	@Autowired
	private JmsTemplate jmsTemplate;
	@Resource
	private Destination destinationTopic;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	
	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;
	
	/**
	 * @author: FANTASY
	 * @Description: 根据商品id查询商品基本信息
	 */
	@Override
	public TbItem getItemById(long id) {
		//查询缓存
		try {
			String itemJson = jedisClient.get(REDIS_ITEM_PRE+":"+id+":BASE");
			if (StringUtils.isNotBlank(itemJson)) {
				TbItem tbItem = JsonUtils.jsonToPojo(itemJson,TbItem.class);
				return tbItem;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//缓存中没有，查询数据库
		//根据主键查询
		TbItem item = itemMapper.selectByPrimaryKey(id);
		
		if (item != null) {
			try {
				//把结果添加到缓存
				jedisClient.set(REDIS_ITEM_PRE+":"+id+":BASE", JsonUtils.objectToJson(item));
				//设置过期时间
				jedisClient.expire(REDIS_ITEM_PRE+":"+id+":BASE", ITEM_CACHE_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//返回结果
		return item;
	}

	/**
	 * @author: FANTASY
	 * @Description: 商品管理列表分页查询
	 */
	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		// 设置分页信息
		PageHelper.startPage(page, rows);
		// 执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		// 区分也结果
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		result.setTotal(pageInfo.getTotal());
		return result;
	}

	/**
	 * @author: FANTASY
	 * @Description: 添加商品
	 */
	@Override
	public E3MallResult addItem(TbItem item, String desc) {
		// 1、生成商品id 
		final long itemId = IDUtils.genItemId();
		// 2、补全TbItem对象的属性
		item.setId(itemId);
		//商品状态，1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		// 3、向商品表插入数据
		itemMapper.insert(item);
		// 4、创建一个TbItemDesc对象
		TbItemDesc itemDesc = new TbItemDesc();
		// 5、补全TbItemDesc的属性
		itemDesc.setItemId(itemId);
		itemDesc.setCreated(new Date());
		itemDesc.setUpdated(new Date());
		// 6、向商品描述表插入数据'
		itemDescMapper.insert(itemDesc);
		
		//同步索引库
		jmsTemplate.send(destinationTopic, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage(itemId+"");
				return message;
			}
		});
		
		// 7、E3Result.ok()
		return E3MallResult.ok();
	}

	/**
	 * @author: FANTASY
	 * @Description: 根据商品id查询商品描述
	 */
	@Override
	public TbItemDesc getItemDescById(long itemId) {
		//查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE+":"+itemId+":DESC");
			if (StringUtils.isNotBlank(json)) {
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return itemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//缓存中没有，查询数据库
		//根据主键查询
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		//把结果添加到缓存
		if (itemDesc != null) {
			try {
				jedisClient.set(REDIS_ITEM_PRE+":"+itemId+":DESC", JsonUtils.objectToJson(itemDesc));
				jedisClient.expire(REDIS_ITEM_PRE+":"+itemId+":DESC", ITEM_CACHE_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//返回结果
		return itemDesc;
	}
	
	
	
	
	

}
