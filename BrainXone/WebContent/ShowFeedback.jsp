<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*,backend.*,java.util.*"%>
<%@ page import= "java.sql.Statement" %>

<%
	HttpSession sess = request.getSession();
	ServletContext servletContext = getServletContext();
	Statement stmt = (Statement) servletContext.getAttribute("Statement");
	Integer quizID = Integer.parseInt(request.getParameter("id"));
	Quiz q = Quiz.getQuizUsingID(quizID, stmt);
	ArrayList<Question> quesList = Quiz.getQuesListUsingID(quizID, stmt);
	
	
	int questionNumber;
	Integer qNum = (Integer) sess.getAttribute("questionNumber");
	if (qNum == null || qNum > quesList.size()) {
		questionNumber = 1;
	} else {
		questionNumber = qNum;
	}

	if (q.isRandomVal()) {
			long seed = (Long) sess.getAttribute("randomSeed");
			System.out.println("Seed is right now: " + seed);
		Collections.shuffle(quesList, new Random(seed));
	}
	Question curr = quesList.get(questionNumber - 1);
	
	ArrayList<String> answers = (ArrayList<String>) sess.getAttribute("givenAnswers");
	Boolean allCorrect = (Boolean) sess.getAttribute("allCorrect");
	
	sess.removeAttribute("givenAnswers");
	sess.removeAttribute("allCorrect");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

	<title>Take a quiz!</title>
	<link rel="stylesheet" href="css/header.css">
	<link rel="stylesheet" href="css/main.css">
	<link rel="stylesheet" href="css/quiz-page.css">

</head>
<body>
<%@ include file="header.html" %>
	<div class="central-content">
		<div class="content-pane">
		<h1 class="quiz-title"><%= q.getDescription() %></h1>
		<div>
		<%
			int type = curr.getQuesType();
			//request.getSession().setAttribute("quesID", ques.getID()); //G: why?
			out.print("<h3 class='quiz-question-header'>The question was:</h3>");
			switch (type) {
			case Question.SINGLE_STR_ANS:
			case Question.FIB:
			case Question.PICTURE_RESPONSE:
			case Question.MULTI_STR_ANS:
				out.print("<p class='quiz-question'>" + curr.getQuesText()
						+ "</p>");
				break;
			case Question.MULTI_CHOICE_C:
			case Question.MULTI_CHOICE_R:
				out.print("<p class='quiz-question'>" + curr.getQuesText()
						+ "</p>");
				out.print("<div class='quiz-answers multi-option'>");
				HashMap<String, Integer> options = curr.displayAnswers(stmt);
				int i = 0;
				for (String str : options.keySet()) {
					i++;
					out.print("<div class='quiz-answer-option'><span>" + str
							+ "</span></div>");
				}
				break;
			}
			out.print("<div class='quiz-answers'><span>You answered: ");
			if (answers.size() == 1) {
				out.print(answers.get(0) + " ");
			} else {
				out.print("</span><ul>");
				for (String s : answers) {
					out.print("<li>" + s + "</li>");
				}
				out.print("</ul><span>");
			}
			out.print("which is </span>");
			if (allCorrect) {
				out.print("<span class='positive-text'>correct!</span>");
			} else {
				out.print("<span class='negative-text'>incorrect.</span> <span>Sorry!</span>");
			}

			out.print("</div></div>");
			out.print("<a class='move-on-button button' href='");
			if(questionNumber == quesList.size()) {
				out.print("quizCompleted.jsp'>Finish Quiz</a>");
			} else {
				out.print("ShowQuiz.jsp?id=" + quizID + "' >Next Question</a>");
			}
		%>
	</div>
	</div>
	</div>
</body>
</html>