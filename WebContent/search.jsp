<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page import="java.util.*" %>
<%@ page import="searchApp.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Biggy - 社交网络内容搜索</title>
</head>
<body background="background.jpg" background-repeat:repeat-y>
	<h1 align="center"><font face="华文新魏">Biggy人人网日志搜索</font></h1>
	
	<form id=searchForm action=SearchController>
	<table align="center">
		<tbody>
			<tr>
				<td colspan="3">
					<input name=searchWord id=searchWord type="text" size="100" />
					<input id=doSearch type="submit" value="Biggy搜索" />
				</td>
			</tr>
		</tbody>
	</table>
	</form>
	
	<table class="result">
		<tbody>
		<%
		@SuppressWarnings(value = {"unchecked"})
		List<SearchResultBean> searchResult = (List<SearchResultBean>)request.getAttribute("searchResult");
		int resultCount = 0;
		if(searchResult != null) {
			resultCount = searchResult.size();
			if(resultCount == 0) {
				%>
				<tr><td>没有找到匹配的结果！</td></tr>
				<%
			}
			for(int i = 0; i < resultCount; i++) {
				SearchResultBean resultBean = (SearchResultBean)searchResult.get(i);
				String title = resultBean.getTitle();
				String url = resultBean.getUrl();
				String sum = resultBean.getSum();
				%>
				<tr>
					<td class="title"><h3><a href="<%=url %>"><%=title %></a></h3></td>
				</tr>
				<tr>
					<td class="sum"><%=sum %></td>
				</tr>
				<tr><td><hr /></td></tr>
				<%
			}
		} 

		%>
		</tbody>
	</table>
</body>
</html>