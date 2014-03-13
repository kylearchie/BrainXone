<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*,backend.*,java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Practice Mode</title>
</head>
<body>

<h1>Let's get some practice!</h1>
<br>
<%
	HashMap<Question, Integer> curScore = (HashMap<Question, Integer>) request.getSession().getAttribute("curScore");
	request.getSession().setAttribute("curScore", curScore);	
	ServletContext servletContext = getServletContext();
	Statement stmt = (Statement) servletContext.getAttribute("Statement");
	System.out.println("+++++++" + curScore.values());
	request.getSession().setAttribute("isPracticeMode", 1);	
	int count = 0;
	for (Question ques : curScore.keySet()) {
		if(curScore.get(ques) < 3){
			count++;
			request.getSession().setAttribute(String.valueOf(ques.getID()),
					ques);
			out.print(ques.getQuesText() + "<br>");
			request.getSession().setAttribute("quesID", ques.getID());
			switch (ques.getQuesType()) {
				case Question.SINGLE_STR_ANS:
				case Question.FIB:
				case Question.PICTURE_RESPONSE:
				%>
					<form action="CheckAnswerServlet" method="post">
					Input your answer: <input type="text" name="answer"> 
					<input type="submit" value="Submit Ans">
					</form>
				<%
					break;
				case Question.MULTI_STR_ANS:
				%>
					<form action="CheckAnswerServlet" method="post">Input your answers <%
					for (int i = 0; i < 3; i++) {
						out.print("<input type = 'text' name = 'multiStringAns" + (i + 1) + "'>");
					}
				%> <input type="submit" value="Submit Ans"></form>
				<%
					break;
				case Question.MULTI_CHOICE_C:
				case Question.MULTI_CHOICE_R:
				%>
					<form action="CheckAnswerServlet" method="post">
					<%
					HashMap<String, Integer> options = ques.displayAnswers(stmt);
					int i = 0;
					for (String str : options.keySet()) {
					//out.print("<input type = 'checkbox' name = 'selected' value='selectedOption"+ (i + 1) + "'> " + str + " <br>");
						out.print("<input type = 'hidden' name = 'options" + (i + 1) + "' value = '" + str + "'>");
						out.print(str + "<input type = 'radio' name='isValid" + (i + 1)
							+ "' value = '1'> YES &nbsp; <input type = 'radio'  name='isValid"
							+ (i + 1) + "' value = '0'> NO <br>");
						i++;
					}
				%> <input type="submit" value="Submit Ans"></form>
				<%
					break;
			}
			// call a jsp to display ques + ans using ques id
		}
		}
	if(count == 0){
		System.out.println("finished practice!");
	}
%>

</body>
</html>