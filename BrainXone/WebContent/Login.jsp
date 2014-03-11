<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Login</title>
</head>
<body>
<h1>Welcome to QuiXone</h1>
<p>Please log in.</p>
<form action="LoginServlet" method="post">
 Username: <input type="text" name="username"/>
 <br>
 Password: <input type="text" name="password"/>
 <br>
 <input type="submit" value="Login"/>
</form>
<a href = "create_new_account.jsp">Create New Account</a>
<form action="GuestServlet" method="post">
 <input type="submit" value="Login as Guest"/>
</form>


</body>
</html>