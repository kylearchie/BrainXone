<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
     <%@ page import = "java.sql.*, brainxone.*, java.util.*" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Inbox</title>
<link rel="stylesheet" href="css/header.css">
	<link rel="stylesheet" href="css/main.css">
	<link rel="stylesheet" href="css/messages.css">

</head>
<body>
<%@ include file="header.jsp" %>
	<div class="central-content">
		<div class="content-pane">
			<h1 class="page-title">Inbox</h1>
			<%
				ServletContext servletContext = getServletContext();
				Statement stmt = (Statement) servletContext.getAttribute("Statement");
				String userName = (String) session.getAttribute("currentUser");
				ArrayList<Message> messages = Message.getMessages(userName, stmt);
				out.println("<div class='message-group'><h3> Notes </h3>");
				for (Message message : messages) {
					out.println("<div class='message content-box'>");
					out.println("<h4 class='message-sender'> Message from " + message.getFromID() + " </h4>");
					out.println("<p class='message-text'> " + message.getText() + " </p>");
					out.println("<form action=\"public-profile.jsp?name=" + message.getFromID() + "\" method=\"post\">");
					out.println("<input type=\"hidden\" name=\"currentUser\" value=\"" + userName + "\"/>");
					out.println("<input type=\"hidden\" name=\"friendUser\" value=\"" + message.getFromID() + "\"/>");
					out.println("<input type=\"hidden\" name=\"type\" value=\" message \"/>");
					out.println("<input class='move-on-button button' type=\"submit\" value=\"Reply\"/>");
					out.println("</form></div>");
					message.setRead(stmt);
				}
				out.println("</div>");

				ArrayList<Message> requests = Message.getFriendRequests(userName,
						stmt);
				out.println("<div class='message-group'><h3> Friend Requests </h3>");
				for (Message r : requests) {
					out.println("<div class='message content-box'>");
					out.println("<h4 class='message-sender'> friend request from " + r.getFromID() + " </h4>");
					out.println("<p class='message-text'> " + r.getText() + " </p>");
					out.println("<form action=\"AcceptFriendServlet\" method=\"post\">");
					out.println("<input type=\"hidden\" name=\"currentUser\" value=\"" + userName + "\"/>");
					out.println("<input type=\"hidden\" name=\"friendUser\" value=\"" + r.getFromID() + "\"/>");
					out.println("<input type=\"hidden\" name=\"timeSent\" value=\"" + r.getTimeSent() + "\"/>");
					out.println("<input type=\"hidden\" name=\"type\" value=\"friend\"/>");
					out.println("<input class=\"positive-button button \" type=\"submit\" value=\"Accept Friend Request\"/>");
					out.println("</form>");

					out.println("<form action=\"DenyFriendServlet\" method=\"post\">");
					out.println("<input type=\"hidden\" name=\"currentUser\" value=\"" + userName + "\"/>");
					out.println("<input type=\"hidden\" name=\"friendUser\" value=\"" + r.getFromID() + "\"/>");
					out.println("<input type=\"hidden\" name=\"timeSent\" value=\"" + r.getTimeSent() + "\"/>");
					out.println("<input type=\"hidden\" name=\"type\" value=\"message\"/>");
					out.println("<input class=\"negative-button button\" type=\"submit\" value=\"Deny Friend Request\"/>");
					out.println("</form></div>");
				}
				out.println("</div>");


				ArrayList<Challenge> challenges = Challenge.getChallenges(userName,
						stmt);
				out.println("<div class='message-group'><h3> Challenges </h3>");
				for (Challenge c : challenges) {
					out.println("<div class='message content-box'>");
					out.println("<h4 class='message-sender'> Challenge from " + c.getFromID() + "on" + c.getQuizID() + "</h4>");
					out.println("<p class='message-text'> " + c.getText() + " </p>");
					out.println("<p>" + c.getFromID() + "'s best score on" + c.getQuizID() + " is "
							+ TakenEvent.bestScore(c.getFromID(), c.getQuizID(), stmt) + "</p>");
					//out.println("<form action=\"QuizSummary.jsp?id="+ c.getQuizID() + "\" method=\"post\">");
					out.println("<form action=\"AcceptChallengeServlet\" method=\"post\">");
					out.println("<input type=\"hidden\" name=\"currentUser\" value=\""
							+ userName + "\"/>");
					out.println("<input type=\"hidden\" name=\"friendUser\" value=\""
							+ c.getFromID() + "\"/>");
					out.println("<input type=\"hidden\" name=\"quizID\" value="
							+ c.getQuizID() + ">");
					out.println("<input type=\"hidden\" name=\"timeSent\" value=\""
							+ c.getTimeSent() + "\"/>");
					out.println("<input type=\"hidden\" name=\"type\" value=\"challenge\"/>");
					out.println("<input class='move-on-button button' type=\"submit\" value=\"Take the challenge\"/>");
					out.println("</form></div>");
				}
				out.println("</div>");

				ArrayList<Challenge> reports = Challenge.getReports(userName, stmt);
				out.println("<div class='message-group'><h3> Reports </h3>");
				for (Challenge r : reports) {
					out.println("<div class='message content-box'>");
					out.println("<h4 class='message-sender'> Report from " + r.getFromID() + "on"
							+ r.getQuizID() + "</h4>");
					out.println("<p class='message-text'> " + r.getText() + " </p>");
					out.println("<form action=\"ReadReportServlet\" method=\"post\">");
					out.println("<input type=\"hidden\" name=\"currentUser\" value=\""
							+ userName + "\"/>");
					out.println("<input type=\"hidden\" name=\"reportUser\" value=\""
							+ r.getFromID() + "\"/>");
					out.println("<input type=\"hidden\" name=\"quizID\" value="
							+ r.getQuizID() + ">");
					out.println("<input type=\"hidden\" name=\"timeSent\" value=\""
							+ r.getTimeSent() + "\"/>");
					out.println("<input type=\"hidden\" name=\"type\" value=\"report\"/>");
					out.println("<input class='move-on-button button' type=\"submit\" value=\"See the quiz\"/>");
					out.println("</form></div>");
				}
				out.println("</div>");

				%>
		</div>
	</div>
</body>
</html>