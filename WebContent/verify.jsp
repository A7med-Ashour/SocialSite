<%@ page language="java" contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="windows-1256">
<title>Verify Account</title>
</head>
<body>
	<form action="verify" method="post">
		<input type="email" name="email" value="${user.email}" readonly="readonly"/>
		<input type="text" name="code" placeholder="Enter Verification Code"/>
		<input type="submit" name="action" value="Verify"/>
		<input type="submit" name="action" value="Resend"/>
	</form>
	<c:if test="${errorMSG == null}">
		<p>"PLEASE VERIFY YOUR ACCOUNT DURING 24 HOURS OR YOUR ACCOUNT WILL BE DELETED."</p>
	</c:if>
	<p>${errorMSG}</p>
</body>
</html>