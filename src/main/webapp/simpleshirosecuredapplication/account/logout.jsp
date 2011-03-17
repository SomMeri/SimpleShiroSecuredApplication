<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Logout</title>
</head>
<body>
<%@include file="/simpleshirosecuredapplication/common/commonformstuff.jsp" %>
<%@ page import="org.apache.shiro.SecurityUtils" %>
<% SecurityUtils.getSubject().logout();%>
<p>You have successfully logged out. 
</body>
</html>