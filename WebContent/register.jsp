<%@ page language="java" contentType="text/html; charset=windows-1256"
    pageEncoding="windows-1256"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="windows-1256">
<title>sign up</title>
<link rel = "stylesheet" type = "text/css" href = "style/registerStyle.css" />
</head>
<body>
	<h1> Enter your Information To Join </h1>
	<p>${errorMSG}</p>
	<form action="register" method = "post">
	
		<input type="hidden" name = "action" value="join"/>
		<input type="email" class = '${messages["email"]}' name="email" placeholder="Email" value="${user.email}"/> 	 
		<p>Email Must Be like ( abc@efg.com ) and Not be Used Before </p>
		
		<input type="password"  class = '${messages["password"]}' name="password" placeholder="password" value="${user.password}"/>
		<p>Password Must be Alphanumeric between 8 and 20 characters and may has @ or -</p>
		 
		<input type="text"  class = '${messages["name"]}' name="name" placeholder="Name" value="${user.name}"/> 
	  	<p>Name Must be only characters between 3 and 10 characters</p>
	    
		<input type="text"   class = '${messages["phone"]}' name="phone" placeholder="phone" value="${user.phone}"/>           
		<p>Please Enter Valid Egyption Phone eg : +201012345678</p>
		
		<label style="text-align : left">Gender : </label>
		<span>Male</span>
		<input type="radio" name="gender" value="male"  <c:if test="${user.male}">checked = "checked"</c:if>/>
		<span>Female</span>
		<input type="radio" name="gender" value="female" 
		 <c:if test="${user.male == null || !user.male }">checked = "checked"</c:if>/>
		
		<input type="submit" value="Join" class = "btn"> 
	</form>
</body>
</html>