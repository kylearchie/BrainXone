<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import = "java.sql.*, brainxone.*, java.util.*, backend.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</title>
	<link rel="stylesheet" href="css/header.css">
	<link rel="stylesheet" href="css/main.css">
</head>
<body>
<%@ include file="header.jsp" %>
	<div class="central-content">
		<div class="content-pane">

		<h1 class="page-title">DONE SUBMITTING!</h1>
<%
String userName = (String) session.getAttribute("currentUser");
Quiz q = (Quiz) session.getAttribute("quiz");
int quizID = q.getQuizID();
ServletContext servletContext = getServletContext();
Statement stmt = (Statement) servletContext.getAttribute("Statement");
Event event = new Event(userName, quizID, stmt);
Event.UpdateCreateAchievements(userName, stmt);
%>
<a href = "index.html">BACK TO HOMEPAGE</a>
</div></div>
</body>
</html>