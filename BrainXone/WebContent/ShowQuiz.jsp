<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*,backend.*,java.util.*"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

	<script src="js/quizTiming.js"></script>
	<title>Take a quiz!</title>
	<link rel="stylesheet" href="css/header.css">
	<link rel="stylesheet" href="css/main.css">
	<link rel="stylesheet" href="css/quiz-page.css">

	<% 
		int quizID = Integer.parseInt(request.getParameter("id"));
		request.getSession().setAttribute("isPracticeMode", 0);

		Quiz q = Quiz.getQuizUsingID(quizID);
	%>

</head>
<body>
<%@ include file="header.html" %>
	<div class="central-content">
		<div class="content-pane">
		<h1 class="quiz-title"><%= q.getDescription() %></h1>
		<form id="quiz-submit-form" action="CheckAnswerServlet" method="post">
		<input type="hidden" value="<%= quizID %>" name="quizID">
		<% 
			ArrayList<Question> quesList = Quiz.getQuesListUsingID(quizID);
			if (q.isRandomVal() == 1) {
				long seed = System.nanoTime();
				Collections.shuffle(quesList, new Random(seed));
			}
			int counter = 0;
			for (Question ques : quesList) {
				counter++;
				request.getSession().setAttribute(String.valueOf(ques.getID()), ques); //G: why?
				int type = ques.getQuesType();
				out.print("<div class='quiz-question content-box' type='" + type + "'>");
				out.print("<input type='hidden' name='question" + counter + "' value='" + ques.getID() + "'>");
				out.print("<input type='hidden' name='question" + counter + "type' value='" + type + "'>");
				request.getSession().setAttribute("quesID", ques.getID()); //G: why?

				switch (type) {
				case Question.SINGLE_STR_ANS:
				case Question.FIB:
				case Question.PICTURE_RESPONSE:
					out.print("<h3 class='quiz-question-header'>Fill in the blank below:</h3>");
					out.print("<p class='quiz-question'>" + ques.getQuesText() + "</p>");
					out.print("<div class='quiz-answers'>");
					out.print("<input type='text' name='question" + counter + "answer1'>");
					break;
				case Question.MULTI_STR_ANS:
					out.print("<h3 class='quiz-question-header'>Fill in all the blank spaces below:</h3>");
					out.print("<p class='quiz-question'>" + ques.getQuesText() + "</p>");
					out.print("<div class='quiz-answers multi-option'>");
					for (int i = 0; i < 3; i++) {
						out.print("<div class='quiz-anwer-option'>");
						out.print("<input type='text' name='question" + counter + "answer" + (i + 1) + "'>");
						out.print("</div>");
					}
					break;
				case Question.MULTI_CHOICE_C:
				case Question.MULTI_CHOICE_R:
					out.print("<h3 class='quiz-question-header'>Select the correct answer(s):</h3>");
					out.print("<p class='quiz-question'>" + ques.getQuesText() + "</p>");
					out.print("<div class='quiz-answers multi-option'>");
					HashMap<String, Integer> options = ques.displayAnswers();
					int i = 0;
					for (String str : options.keySet()) {
						i++;
						out.print("<div class='quiz-answer-option'><input type='hidden' name='question" + counter + "option" + i + "' value='" + str + "'>");
						if(type == Question.MULTI_CHOICE_R) out.print("<input type='radio' name='question" + counter + "valid' value='" + i + "'> " + str + "</div>");
						else out.print("<input type='checkbox' name='question" + counter + "valid" + i + "' value='" + i + "'> " + str + "</div>");
					}
					break;
				}
				out.println("</div></div>");
			} 
		%>
		<input id="quiz-submit-button" class="action-button button" type="submit" value="Submit Answers">
	</form>
	</div>
	</div>
</body>
</html>