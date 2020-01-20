<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>My Friends</title>
</head>
<body>
	<h1>Your Friends Are : </h1>
	<table border="1">
		<tr>
			<th>Name : </th>
			<th>Email :  </th>
			<th>phone : </th>
			<th></th>
		</tr>
		<c:forEach var="friend" items="${user.friends}">
			<tr>
				<td>${friend.name} </td>
				<td>${friend.email}</td>
				<td>${friend.phone}</td>
				<td><a href="friendship?action=remove,id=${friend.ID}" ><button class="redBG">unFriend</button></a></td>
			</tr>
		</c:forEach>
	</table>
	<c:choose>
		<c:when test="${fn:length(user.friends) == 0 }"><p>You Don't Have Any Friends Yet .</p></c:when>
		<c:otherwise><p>You Have (${fn:length(user.friends)})</p></c:otherwise>
	</c:choose>
	<a href="searchFriend.jsp">Add New Friend</a>
</body>
</html>