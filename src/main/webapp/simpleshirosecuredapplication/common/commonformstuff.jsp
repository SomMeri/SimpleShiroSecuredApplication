<%@page import="org.meri.simpleshirosecuredapplication.intrusiondetection.integration.AppSensorIntegrationThreadContext"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<a href="<c:url value="/simpleshirosecuredapplication/index.jsp"/>">Go Home</a>
<a href="<c:url value="/simpleshirosecuredapplication/account/logout.jsp"/>">logout</a><br>
<%
    String reqUrl = request.getServletPath().toString();
	String actionMessage = (String) request.getAttribute("actionResultMessage");
	if (actionMessage!=null) {
%>
<li><%=actionMessage %></li><br>
<%} %>

<%
	String securityMessage = (String) request.getAttribute("securityWarning");
	if (securityMessage!=null) {
%>
<div style="color:red"><%=securityMessage %></div><br>
<%} %>

<input type="hidden" name="originalPage" value="<%=reqUrl%>">
