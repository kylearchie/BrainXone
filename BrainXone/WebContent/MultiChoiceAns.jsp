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
<title>Multi Choice Answers With Multiple Corr Ans</title>
</title>
	<link rel="stylesheet" href="css/header.css">
	<link rel="stylesheet" href="css/main.css">
	<link rel="stylesheet" href="css/multi-choice.css">
	<script src="js/addChoices.js"></script>
</head>
<body>
<%@ include file="header.jsp" %>
	<div class="central-content">
		<div class="content-pane">
			<form id="multi-choice-form" action="AddMultiChoiceAns" method="post">
				<fieldset class="content-box">
					<h2><%= ques.getQuesText() %></h2>
					<div id="multi-choice-form-choices">
						<div class="multi-choice-choice">
							<span></span>
							<span>Answer option</span>
							<span>Is correct?</span>
						</div>
						<div class="multi-choice-choice">
							<div>
								<div class="choice-remove-button hidden"></div>
							</div>
							<div>
								<input type="text" name="option1">
							</div>
							<div>
								<input type="checkbox" name="valid1" value="1">
							</div>
						</div>
					</div>
					<button id="add-another-answer" class="button positive-button" type="button">Add another answer</button>
				</fieldset>
				<% if(ques.getType() == Question.MULTI_CHOICE_R) { %>
				<p class="small-reminder">If you indicate that more than one answer is correct, we will take only the first one.</p>
				<% } %>
				<input id="submit-multi-choice" class="button action-button" type="submit">
			</form>
		</div>
	</div>
</body>
</html>