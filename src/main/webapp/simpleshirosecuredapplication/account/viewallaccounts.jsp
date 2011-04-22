<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View All Accounts</title>
</head>
<body>
<%@include file="/simpleshirosecuredapplication/common/commonformstuff.jsp" %>
<%@page import="org.apache.shiro.SecurityUtils"%>
<%@page import="org.meri.simpleshirosecuredapplication.model.ModelProvider"%>
<%@page import="org.meri.simpleshirosecuredapplication.model.UserPersonalData"%>
<%@page import="java.util.List"%>
All users with filled account data:
<%
ModelProvider mp = new ModelProvider();
List<UserPersonalData> usersData = mp.getAllUsersData();
pageContext.setAttribute("allusers", usersData);
mp.close();
%>
<ul>
<%
for (UserPersonalData data : usersData) {
%>
<li><label><%=data.getUserName()%>:</label> <%=data.getFirstname()%> <%=data.getLastname()%><br>
<textarea readonly="readonly"><%=data.getAbout()%></textarea>
</li>
<% } %>
</ul>
</body>
</html>