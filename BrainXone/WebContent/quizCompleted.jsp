<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="backend.*,java.util.*"%>
<%@ page import= "java.sql.Statement" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Insert title here</title>
	<link rel="stylesheet" href="css/header.css">
	<link rel="stylesheet" href="css/main.css">
</head>
<body>
<%@ include file="header.html" %>
	<div class="central-content">
		<div class="content-pane">
			<%
				HttpSession hs = request.getSession();
				ServletContext servletContext = getServletContext();
				Statement stmt = (Statement) servletContext.getAttribute("Statement");
				Integer quizIDObj = (Integer) hs.getAttribute("quizID");
				int quizID = 0;
				if( quizIDObj != null) quizID = quizIDObj;
				Quiz currQuiz = Quiz.getQuizUsingID(quizID, stmt);
				int score = 0;
				Integer scoreObject = (Integer) hs.getAttribute("currentScore");
				if( scoreObject != null ) score = scoreObject;
				long time = 0;
				Long timeObject = (Long) hs.getAttribute("currentTime");
				if( timeObject != null ) time = timeObject;
				hs.removeAttribute("currentScore");
				hs.removeAttribute("currentTime");
				hs.removeAttribute("currentQuestion");
			%>
			<p>You scored <%= score %> point <% if( score != 1 ) out.print("s"); %></p>
			<p>It took you <%= time/1000 %> seconds</p>
		</div>
	</div>
</body>
</html>