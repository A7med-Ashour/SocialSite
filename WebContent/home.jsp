<%@ page language="java" contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
    <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="windows-1256">
<link rel="stylesheet" type="text/css" href="style/global.css">
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
	<div class = "Your Wall">
		<c:choose>
			<c:when test="${wallPosts == null || fn:length(wallPosts) == 0} ">
				<p>There Are No Posts To Show</p>
			</c:when>
			<c:otherwise>
				<c:forEach var="post" items="${wallPosts}">
					<hr size="5" />
					<div class="post">
						<h5>"${post.ownerName}"</h5>
						<p>create : ${post.createdDate}</p>
						<p>"${post.content}"</p>
						<c:if test="${user.ID == post.ownerID}">
							<a href="posts?action=delete&postID=${post.ID}"><button class="redBG">Delete</button></a>
						</c:if>
					</div>
				</c:forEach>
			</c:otherwise>
		</c:choose>
	</div>
</body>
</html>