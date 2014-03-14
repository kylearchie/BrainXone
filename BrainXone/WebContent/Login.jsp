<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Login</title>
	<link rel="stylesheet" href="css/main.css">
		<link rel="stylesheet" href="css/login.css">
	
</head>
<body>
	<div class="login-container">
		<div class="logo">
			<img src="images/QuiZoneLogoBlack.svg">
		</div>
		<% if(request.getAttribute("error") != null) { %>
		<p class="error-box">I'm sorry, that information is invalid. please try again.</p>
		<% } %>
		<form action="LoginServlet" method="post">
			<label for="username">Username: </label>
			<input type="text" name="username" /> 
			<br> 
			<label for="password">Password: </label>
			<input type="password" name="password" /> 
				<br> 
				<input class="positive-button button" type="submit" value="Login" />
		</form>
		<a class="move-on-button button" href="create_new_account.jsp">Create New Account</a>
		<form action="GuestServlet" method="post">
			<input class="neutral-button button" type="submit" value="Login as Guest" />
		</form>
	</div>

</body>
</html>