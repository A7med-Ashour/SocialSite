<%@ page language="java" contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="windows-1256">
<link rel="stylesheet" type="text/css" href="style/global.css">
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
	
	<div>
		<h2>YOUR POSTS : </h2>
		<div>
			<c:forEach var="post" items="${user.posts}">
			<hr size="5" />
				<div class="post">
					<h5>"${user.name}"</h5>
					<p>create : ${post.createdDate}</p>
					<p>"${post.content}"</p>
					<a href="posts?action=delete&postID=${post.ID}"><button class="redBG">Delete</button></a>
				</div>
			</c:forEach>
		</div>
	</div>
</body>
</html>