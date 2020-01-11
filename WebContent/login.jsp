<%@ page language="java" contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="windows-1256">
<title>Insert title here</title>
</head>
<body>
	<form action="login" method="post">
		<input type="hidden" name="action" value="login"/>
		<input type="email" name="email" placeholder= "Email" value="${user.email}" required/>
		<input type="password" name="password" placeholder="Password" value="${user.password}" required/>
		<input type="submit" value="Login"/>
		<input type="checkbox" name="rememberMe" value="on" <c:if test="${rememberMe == 'on'}">checked = "checked"</c:if>/> 
		<label>Remember Me</label>
	</form>
	<p>${errorMSG}</p>
	<a href="register.jsp">create new account</a>
</body>
</html>