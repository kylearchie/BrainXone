<%@ page language="java" contentType="text/html; charset=UTF-8" 
 pageEncoding="UTF-8"%> 
<!DOCTYPE html> 
<html> 
<head> 
<meta charset="UTF-8" /> 
<title>Create Account</title>
<link rel="stylesheet" href="css/main.css">
		<link rel="stylesheet" href="css/login.css">
	
</head>
<body>
	<div class="login-container">
		<h1 class="page-title">Create New Account</h1>
		<% if(request.getAttribute("error") != null) { %>
			<p class="error-box">I'm sorry, but the username <%= request.getParameter("username") %> is already taken. Please try again.</p>
		<% } %>
		<p>Please enter your new username and password.</p>
		<form action="CreateNewAccountServlet" method="post">
			<p>
				<label for="username">User Name: </label>
				<input type="text" name="username" />
			</p>
			<p>
				<label for="password">Password: </label>
				<input type="password" name="password" />
			</p>
			<p>
				<input class="positive-button button" type="submit" value="Register" />
			</p>
		</form>
		<a class="move-on-button button" href="Login.jsp">Wait, I have an account!</a>
	</div>
</body>
</html>