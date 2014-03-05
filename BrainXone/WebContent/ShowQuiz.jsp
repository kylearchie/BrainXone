<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import = "java.sql.*" %>
<%@ page import = "backend.*" %>
<%@ page import = "java.util.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>QUIZ</title>
</head>
<body>
<h1>You have selected a quiz!</h1>
<h2>FOLLOWING ARE THE Qs and Ans:</h2>
<br>
<%
	int quizID = Integer.parseInt(request.getParameter("id"));
	
	Quiz q = Quiz.getQuizUsingID(quizID);
	
	out.print(q.getDescription() + "<br>");
	
	ArrayList<Question> quesList = Quiz.getQuesListUsingID(quizID);
		
	for(Question ques : quesList){
		request.getSession().setAttribute(String.valueOf(ques.getID()), ques);
		out.print(ques.getQuesText() + "<br>");
		switch(ques.getQuesType()){
		case Question.SINGLE_STR_ANS: 
		case Question.FIB:
		case Question.PICTURE_RESPONSE:
			request.getSession().setAttribute("quesID", ques.getID());
			%>
			<form action = "CheckAnswerServlet" method = "post"> Input your answer: <input type = "text" name = "answer">
			<input type = "submit" value = "Submit Ans">
			</form>
			<% 
			break;
		case Question.MULTI_STR_ANS:
			break;
		case Question.MULTI_CHOICE_C:
		case Question.MULTI_CHOICE_R:
			request.getSession().setAttribute("quesID", ques.getID());
			%>
			<form action = "CheckAnswerMultiChoiceServlet" method = "post">
			<%
			HashMap<String, Integer> options = ques.displayAnswers();
			int i = 0;
			for(String str : options.keySet()){
				//out.print("<input type = 'checkbox' name = 'selected' value='selectedOption"+ (i + 1) + "'> " + str + " <br>");
				out.print("<input type = 'hidden' name = 'options" + (i + 1) + "' value = '" + str+ "'>");
				out.print(str + "<input type = 'radio' name='isValid"+ (i + 1) + "' value = '1'> YES &nbsp; <input type = 'radio'  name='isValid"+ (i + 1) + "' value = '0'> NO <br>");
				i++;			
			}
			%>
			<input type = "submit" value = "Submit Ans">
			</form>
			<% 
			break;
		}
		// call a jsp to display ques + ans using ques id
	}
%>
</body>
</html>