package com.wch.e3mall;

import static org.junit.Assert.*;import java.awt.List;
import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TestFreemarker {

	
	@Test
	public void test01() throws Exception {
		Configuration configuration = new Configuration(Configuration.getVersion());
		configuration.setDirectoryForTemplateLoading(
				new File("E:/ChuanZhi/mall/e3-item-web/src/main/webapp/WEB-INF/ftl"));
		configuration.setDefaultEncoding("utf-8");
		Template template = configuration.getTemplate("demo.ftl");
		Map map = new HashMap<>();
		map.put("hello", "模板测试hello");
		Writer out = new FileWriter(new File("E:/ChuanZhi/freemarkerstatic/hello.html"));
		template.process(map, out);
		out.close();
	}
	
	@Test
	public void test2() throws Exception {
		Configuration configuration = new Configuration(Configuration.getVersion());
		configuration.setDirectoryForTemplateLoading(
				new File("E:/ChuanZhi/mall/e3-item-web/src/main/webapp/WEB-INF/ftl"));
		configuration.setDefaultEncoding("utf-8");
		Template template = configuration.getTemplate("list.ftl");
		
		Map<String, Object> map = new HashMap<>();
		ArrayList<Student> list = new ArrayList<Student>();
		list.add(new Student(1,"张三",21,"北京"));
		list.add(new Student(2,"张三",22,"上海"));
		list.add(new Student(3,"张三",23,"武汉"));
		list.add(new Student(4,"张三",24,"深圳"));
		map.put("stuList", list);
		map.put("student", new Student(0,"zs",21,"xxx"));
		map.put("date", new Date());
		map.put("hello", "hello baby");
		
		Writer out = new FileWriter(new File("E:/ChuanZhi/freemarkerstatic/hello.html"));
		template.process(map, out);
		out.close();
		
	}
	
	
	
}
