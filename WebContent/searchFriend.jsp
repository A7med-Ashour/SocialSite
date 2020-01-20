<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<link rel="stylesheet" type="text/css" href="style/global.css">
<title>Find New Friend</title>
</head>
<body>
	<form action="friendship">
		<input type ="hidden" name="action" value="search"/>
		<input type="search" name = "search" Placeholder = "Enter The Name or Email">
		<input type="submit" value="Search"/>
	</form>
	<c:choose>
		<c:when test="${searchResult != null && fn:length(searchResult) == 0 }">
			<p>NO RESULT FOUND !!</p>
		</c:when>
		
		<c:when test="${searchResult != null && fn:length(searchResult) > 0 }">
			<table border="1">
				<tr>
					<th>
						Name
					</th>
					<th>Email</th>
					<th></th>
				</tr>
				
				<c:forEach var="result" items="${searchResult}">
					<tr>
						<td>${result.name}</td>
						<td>${result.email}</td>
						<td>
							<c:choose>
								<c:when  test="${searchResultState[result.ID] == 'ACCEPTED'}">
									<a href="friendship?action=remove,id=${result.ID}" ><button class="redBG">unFriend</button></a>
								</c:when>
								<c:when test="${searchResultState[result.ID] == 'PENDING'}">
									<a href="friendship?action=removeReq,id=${result.ID}" ><button class="yellowBG">UnRequest</button></a>
								</c:when>
								<c:when test="${searchResultState[result.ID] == 'REPLY'}">
									<a href="friendship?action=accept,id=${result.ID}" ><button class="blueBG">Accept</button></a>
									<a href="friendship?action=remove,id=${result.ID}" ><button class="redBG">Refuse</button></a>
								</c:when>
								<c:otherwise>
									<a href="friendship?action=add,id=${result.ID}" ><button class="greenBG">ADD Friend</button></a>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:forEach>
			</table>
			<p>There are (${fn:length(searchResult)}) Results That Match Your Search.</p>
		</c:when>
	</c:choose>
</body>
</html>