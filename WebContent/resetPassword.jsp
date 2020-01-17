<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Reset Your Password</title>
</head>
<body>
	<h1>Reset Your Password :</h1>
	<form action="reset" method="post">
		<input type="email" name="email" placeholder="Enter Your Email Address" required="required"/>
		<input type="submit" name="action" value="Reset"/>
	</form>
	<p>${errorMSG}</p>
</body>
</html>