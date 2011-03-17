<html>
<body>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<h2>Welcome</h2>
Simple web application for trying out Shiro security framework. There is no security yet. <br>
<a href="<c:url value="/simpleshirosecuredapplication/account/login.jsp"/>">login</a><br>
<a href="<c:url value="/simpleshirosecuredapplication/account/allapplicationfunctions.jsp"/>">all application functions</a><br>
<a href="<c:url value="/simpleshirosecuredapplication/account/personalaccountpage.jsp"/>">personal account page</a><br>
<a href="<c:url value="/simpleshirosecuredapplication/adminarea/administratorspage.jsp"/>">administrators page</a><br>
<a href="<c:url value="/simpleshirosecuredapplication/repairmen/repairmen.jsp"/>">repairmen page</a><br>
<a href="<c:url value="/simpleshirosecuredapplication/sales/sales.jsp"/>">sales page</a><br>
<a href="<c:url value="/simpleshirosecuredapplication/scientists/scientists.jsp"/>">scientists page</a><br>
<a href="<c:url value="/simpleshirosecuredapplication/account/logout.jsp"/>">logout</a><br>
</body>
</html>
