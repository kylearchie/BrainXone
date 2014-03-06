<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
 <%@ page import = "java.sql.*, brainxone.*, java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Insert title here</title>
</head>
<body>
<%

String searchTerm = (String) session.getAttribute("search");
ServletContext servletContext = getServletContext();
Statement stmt = (Statement) servletContext.getAttribute("Statement");
ArrayList<User> users = User.retrieveByPartialUserName(searchTerm, stmt);
out.println(users.size());
out.println("<ul>");
for (User user : users) 
{
	
	String name = "<a href = \"public-profile.jsp?name=" + user.getUserName() + "\">" + user.getUserName() + "</a>";
	out.println("<li>" + name + "</li>");
}
out.println("</ul>");

%>
</body>
</html>