<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css" href="style/global.css">
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
				<td><a href="friendship?action=remove&id=${friend.ID}" ><button class="redBG">unFriend</button></a></td>
			</tr>
		</c:forEach>
	</table>
	<c:choose>
		<c:when test="${fn:length(user.friends) == 0 }"><p>You Don't Have Any Friends Yet .</p></c:when>
		<c:otherwise><p>You Have (${fn:length(user.friends)}) Friends . </p></c:otherwise>
	</c:choose>
	<a href="searchFriend.jsp">Add New Friend</a>
	
	<div>
		<h2>Requests That You Received</h2>
		<table border="1">
			<tr>
			<th>Name : </th>
			<th>Email :  </th>
			<th></th>
			</tr>
			<c:forEach var="sender" items="${receivedRequests}">
				<tr>
					<td>Name : ${sender.name} </td>
					<td>Email : ${sender.email} </td>
					<td>
					<a href="friendship?action=accept&id=${sender.ID}" ><button class="blueBG">Accept</button></a>
					<a href="friendship?action=remove&id=${sender.ID}" ><button class="yellowBG">Refuse</button></a>
					</td>
				</tr>
			</c:forEach>
		</table>
		<c:choose>
		<c:when test="${fn:length(receivedRequests) == 0 }"><p>You Didn't Receive Any Request .</p></c:when>
		<c:otherwise><p>You Have (${fn:length(receivedRequests)}) Requests .</p></c:otherwise>
	</c:choose>
	</div>
	
	
	<div>
		<h2>Requests That Still Pending</h2>
		<table border="1">
			<tr>
			<th>Name : </th>
			<th>Email :  </th>
			<th></th>
			</tr>
			<c:forEach var="receiver" items="${sentRequests}">
				<tr>
					<td>Name : ${receiver.name}</td>
					<td>Email : ${receiver.email}</td>
					<td><a href="friendship?action=remove&id=${receiver.ID}" ><button class="redBG">UnRequest</button></a></td>
				</tr>
			</c:forEach>
		</table>
		<c:choose>
		<c:when test="${fn:length(sentRequests) == 0 }"><p>You Didn't Have Any Pending Request .</p></c:when>
		<c:otherwise><p>You Have (${fn:length(sentRequests)}) Pending Requests .</p></c:otherwise>
	</c:choose>
	</div>
	
</body>
</html>