<%@ page language="java" contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="windows-1256">
<title>Home</title>
</head>
<body>
	<p>Welcome ${user.name} to Home Page</p>
	
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
	<c:if test="${user.male}"></c:if>
	<a href="profile.jsp">Go to Profile</a>
	
	
</body>
</html>