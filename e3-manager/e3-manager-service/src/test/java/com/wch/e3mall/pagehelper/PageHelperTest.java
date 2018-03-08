package com.wch.e3mall.pagehelper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wch.e3mal.dao.TbItemMapper;
import com.wch.e3mal.pojo.TbItem;
import com.wch.e3mal.pojo.TbItemExample;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/applicationContext-dao.xml"})
public class PageHelperTest {

	@Autowired
	private TbItemMapper itemMapper;
	
	@Test
	public void testPageHelper(){
		
		PageHelper.startPage(1, 10);
		
		TbItemExample example = new TbItemExample();
		List<TbItem> items = itemMapper.selectByExample(example);
		
		PageInfo<TbItem> pageInfo = new PageInfo<>(items);
		
		System.err.println(pageInfo.getTotal());
		System.err.println(pageInfo.getPages());
		System.err.println(items.size());
		
	}
	
}
