<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
     <%@ page import = "java.sql.*, brainxone.*, java.util.*" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Quizes you have created</title>
</head>
<body>
<h1>Quizes you have created</h1>
<%
ServletContext servletContext = getServletContext();
Statement stmt = (Statement) servletContext.getAttribute("Statement");
String userName = (String)session.getAttribute("currentUser");
ArrayList<Event> createEvents = Event.getCreateEvents(userName, stmt);
out.println("<h4> Created Events </h4>");
out.println("<ul>");
for (Event createEvent : createEvents) 
{
	String name = "<a href = \"ShowQuiz.jsp?id=" + createEvent.getQuizID() + "\"> QUIZ " + createEvent.getQuizID()  + "</a>";
	out.println("<li>" + name + "</li>");
}
%>


</body>
</html>