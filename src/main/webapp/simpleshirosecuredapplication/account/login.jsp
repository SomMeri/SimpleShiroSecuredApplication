<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Please Log In</title>
</head>
<body>
<%@include file="/simpleshirosecuredapplication/common/commonformstuff.jsp" %>
<%
  String errorDescription = (String) request.getAttribute("simpleShiroApplicationLoginFailure");
  if (errorDescription!=null) {
%>
Login attempt was unsuccessful: <%=errorDescription%>
<%
  }
%><form name="loginform" action="" method="post">
    <table align="left" border="0" cellspacing="0" cellpadding="3">
        <tr>
            <td>Username:</td>
            <td><input type="text" name="user" maxlength="30"></td>
        </tr>
        <tr>
            <td>Password:</td>
            <td><input type="password" name="pass" maxlength="30"></td>
        </tr>
        <tr>
            <td colspan="2" align="left"><input type="checkbox" name="remember"><font size="2">Remember Me</font></td>
        </tr>
        <tr>
            <td colspan="2" align="right"><input type="submit" name="submit" value="Login"></td>
        </tr>
    </table>
</form>
</body>
</html>