<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
    <%@ page import = "java.sql.*, brainxone.*, java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<%


String userName = (String) session.getAttribute("currentUser");

User user = User.retrieveByUserName(userName, stmt);

%>

<title><%=(String)request.getParameter("name")%></title>
</head>
<body>
<h1><%=(String)request.getParameter("name")%></h1>
<% 

if()

%>
</body>
</html>