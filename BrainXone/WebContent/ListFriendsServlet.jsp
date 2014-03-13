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
ServletContext servletContext = getServletContext();
Statement stmt = (Statement) servletContext.getAttribute("Statement");
String userName = (String) session.getAttribute("currentUser");
User user = User.retrieveByUserName(userName, stmt);
ArrayList<String> friends = user.getFriends();
out.println("<ul>");
for (String friend : friends) 
{
	out.println("<form action=\"ChallengeServlet\" method=\"post\">");
	out.println("<input type=\"hidden\" name=\"friendUser\" value=\"" + friend + "\"/>");
	out.println("<input type=\"hidden\" name=\"type\" value=\"challenge\"/>");
	out.println("<input type=\"hidden\" name=\"quizID\" value=" + request.getParameter("quizID") + ">");
	out.println("Your message: <input type=\"text\" name=\"message\"/>");
    out.println("<input type=\"submit\" value=\"Challenge " + friend + "\"/>");   
    out.println("</form>");	
}
out.println("</ul>");

%>
</body>
</html>