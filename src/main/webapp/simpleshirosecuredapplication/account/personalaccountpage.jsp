<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
label {
	width: 100px;
	float: left;
	display: block;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Personal Account</title>
</head>
<body>
<form action="/simpleshirosecuredapplication/accountpageservlet" method="get">
<%@page import="org.apache.shiro.SecurityUtils"%>
<%@include file="/simpleshirosecuredapplication/common/commonformstuff.jsp"%>
<%@ page import="org.meri.simpleshirosecuredapplication.model.ModelProvider"%>
<%@ page import="org.meri.simpleshirosecuredapplication.model.UserPersonalData"%>
<%
ModelProvider mp = new ModelProvider();
UserPersonalData loggedUserData = mp.getCurrentUserData();
mp.close();
String loggedUser = (String)SecurityUtils.getSubject().getPrincipal();
String firstname = loggedUserData==null || loggedUserData.getFirstname() == null? "" : loggedUserData.getFirstname();
String lastname = loggedUserData==null || loggedUserData.getLastname() == null? "" : loggedUserData.getLastname();
String about = loggedUserData==null ? null : loggedUserData.getAbout();
%>
Hi <%=loggedUser%>. You can edit all your data here. <br>
<br>
<label>First Name:</label><input type="text" name="firstname" value="<%=firstname%>" size="20" /><br>
<label>Last Name:</label> <input type="text" name="lastname" value="<%=lastname%>" size="20" /><br>
<label>About:</label><textarea rows="10" cols="20" name="about"><%=about%>
</textarea><br>
<label>&nbsp;</label><button type="submit" name="save" value="save">Save</button>
</form>

</body>
</html>