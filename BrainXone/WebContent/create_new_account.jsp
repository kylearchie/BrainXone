<%@ page language="java" contentType="text/html; charset=UTF-8" 
 pageEncoding="UTF-8"%> 
<!DOCTYPE html> 
<html> 
<head> 
<meta charset="UTF-8" /> 
<title>Create Account</title>
</head>
<body>
<h1>Create New Account</h1>
<p>Please enter proposed name and password.</p>
<form action="CreateNewAccountServlet" method="post">
    <p>User Name: <input type="text" name="username"/></p>
    <p>
    Password: <input type="text" name="password"/>
    <input type="submit" value="Login"/>
    </p>
</form>
</body>
</html>