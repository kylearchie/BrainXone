<%@ page language="java" contentType="text/html; charset=UTF-8" 
 pageEncoding="UTF-8"%> 
<!DOCTYPE html> 
<html> 
<head> 
<meta charset="UTF-8" /> 
<title>Welcome <%= request.getParameter("username") %></title>
</head>
<body>
<h1>Welcome <%= request.getParameter("username") %></h1>
<%      
        String userName = request.getParameter("username");
		session.setAttribute("currentUser", userName);
%>
<p>Use the search box below to find other users!</p>
<form action="findServlet" method="post">
<input type="text" name="searchTerm"/>
<input type="submit" value="Search"/>
</form>
</body>
</html>