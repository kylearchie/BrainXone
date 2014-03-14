<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*,backend.*,java.util.*"%>
<%@ page import= "java.sql.Statement" %>

<%
	HttpSession sess = request.getSession();
	ServletContext servletContext = getServletContext();
	Statement stmt = (Statement) servletContext.getAttribute("Statement");
	Integer quizID = Integer.parseInt(request.getParameter("id"));
	sess.setAttribute("quizID", quizID);
	ArrayList<Question> quesList = Quiz.getQuesListUsingID(quizID, stmt);
	
	HashMap<Integer, Integer> usedQuestions = (HashMap<Integer, Integer>) sess.getAttribute("usedQuestions");

	Quiz q = Quiz.getQuizUsingID(quizID, stmt);

	Boolean practiceObject = (Boolean) sess.getAttribute("isPracticeMode");
	boolean isPracticeMode;
	if( practiceObject == null ) isPracticeMode = false;
	else isPracticeMode = practiceObject;
	
	int questionNumber = -1;
	if (!q.isOnePage()) {
		Integer qNum = (Integer) sess.getAttribute("questionNumber");
		if (qNum == null || qNum >= quesList.size()) {
			questionNumber = 0;
			sess.setAttribute("questionNumber", questionNumber);
			sess.setAttribute("currentScore", 0);
			sess.setAttribute("currentTime", new Long(0));
			sess.setAttribute("randomSeed", System.nanoTime());

		} else {
			questionNumber = qNum;
		}
		Question curr;
		while(true) {
			curr = quesList.get(questionNumber);
			if(!isPracticeMode) break;
			int uses = usedQuestions.get(curr.getID());
			System.out.println("Question " + curr.getID() + " has " + uses + " uses left.");
			if(uses > 0) {
				break;
			}
			questionNumber++;
			if( questionNumber >= quesList.size()) questionNumber = 0;
		}
		sess.setAttribute("questionNumber", questionNumber);
		quesList = new ArrayList<Question>();
		quesList.add(curr);
	}
	if (q.isRandomVal()) {
		long seed;
		if( q.isOnePage() ) seed = System.nanoTime();
		else seed = (Long) sess.getAttribute("randomSeed");
		System.out.println("Seed is right now: " + seed);
		Collections.shuffle(quesList, new Random(seed));
	}
	if(!q.isOnePage()) {
		
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

	<script src="js/quizTiming.js"></script>
	<% if(q.isTimedQuiz()) { %>
	<script src="js/quizCountdown.js"></script>
	<% } %>
	<title>Take a quiz!</title>
	<link rel="stylesheet" href="css/header.css">
	<link rel="stylesheet" href="css/main.css">
	<link rel="stylesheet" href="css/quiz-page.css">

</head>
<body>
<%@ include file="header.jsp" %>
	<div class="central-content">
		<div class="content-pane">
		<h1 class="quiz-title"><%= q.getDescription() %></h1>
		<form id="quiz-submit-form" action="CheckAnswerServlet" method="post">
		<% 
			int counter = 0;
			for (Question ques : quesList) {
				if(isPracticeMode && usedQuestions.get(ques.getID()) == 0) {
					continue;
				}
				counter++;
				//request.getSession().setAttribute(String.valueOf(ques.getID()), ques); //G: why?
				int type = ques.getQuesType();
				out.print("<div class='quiz-question content-box' type='" + type + "'>");
				out.print("<input type='hidden' name='question" + counter + "' value='" + ques.getID() + "'>");
				out.print("<input type='hidden' name='question" + counter + "type' value='" + type + "'>");
				//request.getSession().setAttribute("quesID", ques.getID()); //G: why?

				switch (type) {
				case Question.SINGLE_STR_ANS:
				case Question.FIB:
				case Question.PICTURE_RESPONSE:
					out.print("<h3 class='quiz-question-header'>Fill in the blank below:</h3>");
					out.print("<p class='quiz-question'>" + ques.getParsedQuesText(counter) + "</p>");
					break;
				case Question.MULTI_STR_ANS:
					out.print("<h3 class='quiz-question-header'>Fill in all the blank spaces below:</h3>");
					out.print("<p class='quiz-question'>" + ques.getParsedQuesText() + "</p>");
					out.print("<div class='quiz-answers multi-option'>");
					StringResponse sr = (StringResponse) ques;
					int numQuestions = sr.getAnswerKeys(stmt).keySet().size();
					for (int i = 0; i < numQuestions; i++) {
						out.print("<div class='quiz-anwer-option'>");
						out.print("<input type='text' name='question" + counter + "answer" + (i + 1) + "'>");
						out.print("</div>");
					}
					out.print("</div>");
					break;
				case Question.MULTI_CHOICE_C:
				case Question.MULTI_CHOICE_R:
					out.print("<h3 class='quiz-question-header'>Select the correct answer(s):</h3>");
					out.print("<p class='quiz-question'>" + ques.getParsedQuesText() + "</p>");
					out.print("<div class='quiz-answers multi-option'>");
					HashMap<String, Integer> options = ques.displayAnswers(stmt);
					int i = 0;
					for (String str : options.keySet()) {
						i++;
						out.print("<div class='quiz-answer-option'><input type='hidden' name='question" + counter + "option" + i + "' value='" + str + "'>");
						if(type == Question.MULTI_CHOICE_R) out.print("<input type='radio' name='question" + counter + "valid' value='" + i + "'> " + str);
						else out.print("<input type='checkbox' name='question" + counter + "valid" + i + "' value='" + i + "'> <span>" + str + "</span>");
						out.print("</div>");
					}
					out.print("</div>");
					break;
				}
				out.print("</div>");
			} 
		%>
		<input id="quiz-submit-button" class="action-button button" type="submit" value="Submit Answers">
	</form>
	<% if (isPracticeMode) { %>
		<a class="move-on-button button" href="QuizSummary.jsp?id=<%= quizID %>" >Leave Practice Mode</a>
	<% } %>
	</div>
	</div>
</body>
</html>