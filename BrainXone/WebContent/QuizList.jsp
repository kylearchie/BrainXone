<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import = "java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="css/header.css">
	<link rel="stylesheet" href="css/main.css">
<title>List of Quizzes in your selected category</title>
</head>
<body>

<%@ include file="header.jsp" %>

<h1>List of Quizzes in your selected category</h1>
<%
	HashMap<Integer, String> quizList = (HashMap<Integer, String>) request.getSession().getAttribute("quizList");
	for(int quizID : quizList.keySet()){
    		String quizName = quizList.get(quizID);
    		out.print("<li><a href=\"QuizSummary.jsp?id=" + quizID + "\">" + quizName + "</a></li>");
    }
%>

</div></div>
</body>
</html>