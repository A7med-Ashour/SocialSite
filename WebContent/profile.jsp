<%@ page language="java" contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="windows-1256">
<title>profile </title>
</head>
<body>
	<p>Welcome ${user.name} to Profile Page</p>
	
	<h1> your Information Are </h1>
	<p>ID		 	: ${user.ID}       </p>
	<p>Email 		: ${user.email}    </p>
	<p>Name 		: ${user.name} 	   </p>
	<p>phone    	: ${user.phone}    </p>
	<p>DateOfBirth  : ${user.DOB} </p>
	<c:choose>
		<c:when test="${user.male}">
			<p>Gender : Male</p>
		</c:when>
		<c:otherwise>
			<p>Gender : Female</p>
		</c:otherwise>
	</c:choose>
	<a href="logout?action=logout">LogOut</a>
	
</body>
</html>