<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
     <%@ page import = "java.sql.*, brainxone.*, java.util.*" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<title>Inbox</title>
</head>
<body>
<h1>Inbox</h1>
<%
	ServletContext servletContext = getServletContext();
	Statement stmt = (Statement) servletContext.getAttribute("Statement");
	String userName = (String)session.getAttribute("currentUser");
	ArrayList<Message> messages = Message.getMessages(userName, stmt);
	out.println("<h4> Messages </h4>");
	out.println("<ul>");
	for (Message message : messages) 
	{
		out.println("<li> message from " + message.getFromID() + " </li>");
		out.println("<li> " + message.getText() + " </li>");
		out.println("<form action=\"public-profile.jsp?name=" + message.getFromID() + "\" method=\"post\">");
		out.println("<input type=\"hidden\" name=\"currentUser\" value=\"" + userName + "\"/>");
		out.println("<input type=\"hidden\" name=\"friendUser\" value=\"" + message.getFromID() + "\"/>");
		out.println("<input type=\"hidden\" name=\"type\" value=\" message \"/>");
	    out.println("<input type=\"submit\" value=\"Reply\"/>");   
	    out.println("</form>");	
	    message.setRead(stmt);
	}
	ArrayList<Message> requests = Message.getFriendRequests(userName, stmt);
	out.println("<h4> Friend Requests </h4>");
	out.println("<ul>");
	for (Message r : requests) 
	{
		out.println("<li> friend request from " + r.getFromID() + " </li>");
		out.println("<li> " + r.getText() + " </li>");
		out.println("<form action=\"AcceptFriendServlet\" method=\"post\">");
		out.println("<input type=\"hidden\" name=\"currentUser\" value=\"" + userName + "\"/>");
		out.println("<input type=\"hidden\" name=\"friendUser\" value=\"" + r.getFromID() + "\"/>");
		out.println("<input type=\"hidden\" name=\"timeSent\" value=\"" + r.getTimeSent() + "\"/>");
		out.println("<input type=\"hidden\" name=\"type\" value=\"friend\"/>");
	    out.println("<input type=\"submit\" value=\"Accept Friend Request\"/>");
	    
	    out.println("</form>");	
	    
	    out.println("<form action=\"DenyFriendServlet\" method=\"post\">");
		out.println("<input type=\"hidden\" name=\"currentUser\" value=\"" + userName + "\"/>");
		out.println("<input type=\"hidden\" name=\"friendUser\" value=\"" + r.getFromID() + "\"/>");
		out.println("<input type=\"hidden\" name=\"timeSent\" value=\"" + r.getTimeSent() + "\"/>");
		out.println("<input type=\"hidden\" name=\"type\" value=\"message\"/>");
	    out.println("<input type=\"submit\" value=\"Deny Friend Request\"/>");
	    
	    out.println("</form>");	
	}
	out.println("</ul>");
	
	ArrayList<Challenge> challenges = Challenge.getChallenges(userName, stmt);
	out.println("<h4> Challenges </h4>");
	out.println("<ul>");
	for (Challenge c : challenges) 
	{
		out.println("<li> Challenge from " + c.getFromID() + " </li>");
		out.println("<li> " + c.getText() + " </li>");		
		out.println("<form action=\"quiz.jsp?id="+ c.getQuizID() + "\" method=\"post\">");
		out.println("<input type=\"hidden\" name=\"currentUser\" value=\"" + userName + "\"/>");
		out.println("<input type=\"hidden\" name=\"friendUser\" value=\"" + c.getFromID() + "\"/>");
		out.println("<input type=\"hidden\" name=\"timeSent\" value=\"" + c.getTimeSent() + "\"/>");	
		out.println("<input type=\"hidden\" name=\"type\" value=\"challenge\"/>");
	    out.println("<input type=\"submit\" value=\"Take the challenge\"/>");
	    out.println("</form>");	
	}
	out.println("</ul>");
	
	

%>
</body>
</html>