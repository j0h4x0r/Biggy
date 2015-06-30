<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ page isELIgnored="false" %>
<%@ page import="indexHandler.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Biggy - 社交网络内容搜索</title>
</head>
<body background="background.jpg">
<h1 align="center">欢迎来到Biggy！</h1>
	Biggy是一个社交网络内容搜索引擎，目前仅支持人人网。<br />
	要想使用Biggy，你必须使用人人账号进行登录<br />
	<br />
	<br />
	<form action="http://127.0.0.1:8080/Biggy/Login" method="post" >
	<center>
		<label>用户名</label><input name="email" type="text" style="width:200px; height:20px"/><br /><br />
		<label>密&nbsp;&nbsp;&nbsp;&nbsp;码</label><input name="password" type="password" style="width:200px; height:20px"/><br /> <br />
		<input type="submit" value="登录" style="width:100px; height:30px"/><br />
	</center>
	</form>
</body>
</html>