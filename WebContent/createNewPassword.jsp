<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Create New Password</title>
</head>
<body>
	<h1>Create New Password</h1>
	<form action="reset" method = "post">
		<input type="hidden" name="action" value="newPassword"/>
		<input type="hidden" name="token" value="${param.token}"/>
		<input type = "password" name = "password" placeHolder="Enter Your New Password " value="${password}"/>
		<input type = "password" name = "repeat"     placeHolder="Comfirm Your New Password " value="${repeat}"/>
		<input type = "submit" value="Create New Password"/>
	</form>
	<p>${errorMSG}</p>
</body>
</html>