<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import = "java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>List of Quizzes in your selected category</title>
</head>
<body>
<h1>List of Quizzes in your selected category</h1>
<%
	HashMap<Integer, String> quizList = (HashMap<Integer, String>) request.getSession().getAttribute("quizList");
	for(int quizID : quizList.keySet()){
    		String description = quizList.get(quizID);
    		out.print("<li><a href=\"ShowQuiz.jsp?id=" + quizID + "\">" + description + "</a></li>");
    }
%>

</body>
</html>