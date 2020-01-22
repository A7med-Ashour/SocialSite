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
	<a href="profile.jsp">Go to Profile</a>
	<a href="friendship?action=show">My Friends</a>
	<a href="logout?action=logout">LogOut</a>
	
	<div class="newPost">
		<form action="posts" method="post">
			<input type="hidden" name="action" value="create"/>
			<textarea name="content" rows="10" cols="50" placeholder ="Create New Post" >
			</textarea>
			<input type="submit" value="Publish"/>
		</form>
		<p class="errorMSG">${errorMSG}</p>
	</div>
</body>
</html>