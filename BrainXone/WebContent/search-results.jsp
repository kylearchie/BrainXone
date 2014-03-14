<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
 <%@ page import = "java.sql.*, brainxone.*, java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Search Results</title>
</title>
	<link rel="stylesheet" href="css/header.css">
	<link rel="stylesheet" href="css/main.css">
</head>
<body>
<%@ include file="header.jsp" %>
	<div class="central-content">
		<div class="content-pane">

		<h1 class="page-title">Search Results</h1>
<%
ServletContext servletContext = getServletContext();
Statement stmt = (Statement) servletContext.getAttribute("Statement");
String userName = (String) session.getAttribute("currentUser");
String searchTerm = (String) session.getAttribute("search");
ArrayList<User> users = User.retrieveByPartialUserName(searchTerm, stmt);
out.println(users.size());
out.println("<ul>");
for (User user : users) 
{
	if (!user.isPrivate() || user.getFriends().contains(userName)) {
		String name = "<a href = \"public-profile.jsp?name=" + user.getUserName() + "\">" + user.getUserName() + "</a>";
		out.println("<li>" + name + "</li>");
	}	
}
out.println("</ul>");

%>
</div></div>
</body>
</html>