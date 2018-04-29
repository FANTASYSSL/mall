<html>
<head>
	<title>student</title>
</head>
<body>
	学生信息：<br>
	学号：${student.id}&nbsp;&nbsp;&nbsp;&nbsp;
	姓名：${student.name}&nbsp;&nbsp;&nbsp;&nbsp;
	年龄：${student.age}&nbsp;&nbsp;&nbsp;&nbsp;
	家庭住址：${student.address}<br>
	学生列表：
	<table border="1">
		<tr>
			<th>序号</th>
			<th>学号</th>
			<th>姓名</th>
			<th>年龄</th>
			<th>家庭住址</th>
		</tr>
		<#--
		<#list stuList as stu>
		<#if stu_index % 2 == 0>
		<tr bgcolor="red">
		<#else>
		<tr bgcolor="green">
		</#if>
			<td>${stu_index}</td>
			<td>${stu.id}</td>
			<td>${stu.name}</td>
			<td>${stu.age}</td>
			<td>${stu.address}</td>
		</tr>
		</#list> -->
		<#list stuList as s>
			<#-- <#if s.id == 1> -->
			<#if stuList?size = 4>
			<tr>
				<td>${s_index}</td>
				<td>${s.id}</td>
				<td>${s.name}</td>
				<td>${s.age}</td>
				<td>${s.address}</td>
			</tr>
			</#if>
		</#list>
	</table>
	<br>
	
	当前日期：${date?string("yyyy-MM-dd")} <br>
	null值的处理：${val!"val为空"}
	判断val的值是否为null：<br>
	<#if val??>
		val不为空
	<#else>
		val为空
	</#if>
	<br>
	<#include "demo.ftl">
</body>
</html>