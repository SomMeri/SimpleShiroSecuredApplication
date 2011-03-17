<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Repairmen Page</title>
</head>
<body>
<%@page import="org.meri.simpleshirosecuredapplication.actions.Actions"%>

<form action="/simpleshirosecuredapplication/masterservlet" method="get">
<%@include file="/simpleshirosecuredapplication/common/commonformstuff.jsp" %>
<h2>Repairmen Page</h2>
This page is meant for repairmen only. If you are not one, please go away. Available functions:
    <table class="sample">
        <thead>
        <tr>
            <th>Function Name</th>
            <th>Do It</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>Repair refrigerator: </td>
            <td><button type="submit" name="action" value="<%=Actions.REPAIR_REFRIGERATOR.getName()%>">Do It</button></td>
        </tr>
        <tr>
            <td>Repair fridge: </td>
            <td><button type="submit" name="action" value="<%=Actions.REPAIR_FRIDGE.getName()%>">Do It</button></td>
        </tr>
        <tr>
            <td>Repair door: </td>
            <td><button type="submit" name="action" value="<%=Actions.REPAIR_DOOR.getName()%>">Do It</button></td>
        </tr>
        </tbody>
    </table>
</form>
</body>
</html>