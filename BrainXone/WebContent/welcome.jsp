<%@ page language="java" contentType="text/html; charset=UTF-8" 
 pageEncoding="UTF-8"%>
 <%@ page import = "java.sql.*, brainxone.*, java.util.*" %> 
<!DOCTYPE html> 
<html> 
<head> 
<meta charset="UTF-8" /> 
<%      
        String userName;
        if(session.getAttribute("currentUser") != null)
        {
        	userName = (String) session.getAttribute("currentUser");
        }
        else
        {
        	userName = request.getParameter("username");
    		session.setAttribute("currentUser", userName);
        }
%>
<title>Welcome <%= userName %></title>
</head>
<body>
<h1>Welcome <%= userName %></h1>
<%
	ServletContext servletContext = getServletContext();
	Statement stmt = (Statement) servletContext.getAttribute("Statement");
     ArrayList<Message> messages = Message.getMessages(userName, stmt);
     int numMessages = messages.size();
%>
<a href="inbox.jsp"> You have <%= numMessages %> messages.</a>

<%
     ArrayList<Message> friends = Message.getFriendRequests(userName, stmt);
     int numRequests = friends.size();
%>
<a href="inbox.jsp"> You have <%= numRequests %> friend requests.</a>

<%
     ArrayList<Challenge> challenges = Challenge.getChallenges(userName, stmt);
     int numChallenges = challenges.size();
%>
<a href="inbox.jsp"> You have <%= numChallenges %> challenges.</a>

<%
     ArrayList<Event>  createEvents = Event.getCreateEvents(userName, stmt);
     
%>

<%
     ArrayList<TakenEvent>  takenEvents= TakenEvent.getTakenEventss(userName, stmt);
     
%>



<form action="logoutServlet" method="post">
<input type="submit" value="Logout"/>
</form>

<p>Use the search box below to find other users!</p>

<form action="findServlet" method="post">
<input type="text" name="searchTerm"/>
<input type="submit" value="Search"/>
</form>
</body>
</html>