<%@ page language="java" contentType="text/html; charset=UTF-8" 
 pageEncoding="UTF-8"%> 
<!DOCTYPE html> 
<html> 
<head> 
<meta charset="UTF-8" /> 
<title>Information Incorrect</title>
</head>
<body>
<h1>Please Try Again</h1>
<p>Either your user name or password is incorrect. Please try again.</p>
<form action="LoginServlet" method="post">
    <p>User Name: <input type="text" name="name"/></p>
    <p>
    Password: <input type="text" name="password"/>
    <input type="submit" value="Login"/>
    </p>
</form>
<a href = "create_new_account.jsp">Create New Account</a>
</body>
</html>