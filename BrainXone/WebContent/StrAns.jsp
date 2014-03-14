<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.sql.*,backend.*,java.util.*"%>
    
<%
	HttpSession sess = request.getSession();
	Question ques = (Question) sess.getAttribute("question");
	Quiz quiz = (Quiz) sess.getAttribute("quiz");
   
   %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<% if(ques.getType() == Question.MULTI_STR_ANS) { %>
	<script src="js/addQuestions.js"></script>
<% } %>
	<script src="js.addVariants.js"></script>


<title>Insert title here</title>
</head>
<body>
<%@ include file="header.html" %>

<div class="central-content">
		<div class="content-pane">
			<form id="string-answer-input-form" action="AddMultiStrAns" method="post">
				<fieldset class="content-box">
					<h2 class="question-title"><%= ques.getQuesText() %></h2>
					<div id="multi-choice-form-choices">
						<div class="multi-choice-choice">
							<span></span>
							<span>Answer option</span>
							<span>Add variants?</span>
						</div>
						<div class="multi-choice-choice">
							<div>
								<div class="choice-remove-button hidden"></div>
							</div>
							<div>
								<input type="text" name="option1answer1">
							</div>
							<div>
								<div class="variant-add-button"></div>
							</div>
						</div>
					</div>
					<% if(ques.getType() == Question.MULTI_STR_ANS) { %>
					<button id="add-another-answer" class="button positive-button" type="button">Add another answer</button>
					<% } %>
				</fieldset>
				<% if(quiz.hasPracticeMode()) { %>
				<fieldset class="content-box">
					<label for="max-points">Points required to be considered correct in practice mode:</label>
					<input type="number" name="maxPoints" value ="1">
				</fieldset>
				<% } %>
				<input id="submit-multi-choice" class="button action-button" type="submit">
			</form>
		</div>
	</div>
</body>
</html>