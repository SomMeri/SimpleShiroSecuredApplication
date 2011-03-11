<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>All Application Functions</title>
</head>
<body>
<%@page import="org.meri.simpleshirosecuredapplication.actions.Actions"%>

<form action="/simpleshirosecuredapplication/masterservlet" method="get">
<%@include file="/simpleshirosecuredapplication/common/commonformstuff.jsp" %>
All functions available in the application:
    <table class="sample">
        <thead>
        <tr>
            <th>Function Name</th>
            <th>Do It</th>
        </tr>
        </thead>
        <tbody>
		<% for (Actions action : Actions.values()) { %>
        <tr>
            <td><%=action.getName() %> </td>
            <td><button type="submit" name="action" value="<%=action.getName()%>">Do It</button></td>
        </tr>
		<% } %>
        </tbody>
    </table>
</form>

</body>
</html>