<%@ page language="java" contentType="text/html; charset=UTF-8" 
 pageEncoding="UTF-8"%>
 <%@ page import = "java.sql.*, brainxone.*, java.util.*, backend.*" %> 
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
        	if( userName == null ) {
        		response.sendRedirect("Login.jsp");
        		return;
        	}
    		session.setAttribute("currentUser", userName);
        }
%>
<title>Welcome <%= userName %></title>
	<link rel="stylesheet" href="css/header.css">
	<link rel="stylesheet" href="css/main.css">
	<link rel="stylesheet" href="css/quiz-page.css">

</head>
<body>
<%@ include file="header.jsp" %>
	<div class="central-content">
		<div class="content-pane">
<h1>Welcome <%= userName %></h1>
<%
ServletContext servletContext = getServletContext();
Statement stmt = (Statement) servletContext.getAttribute("Statement");

User user = User.retrieveByUserName(userName, stmt);
if (user.isAdmin()) {
	out.println("<form action=\"MakeAnnouncementServlet\" method=\"post\">");
	out.println("Your announcement: <input type=\"text\" name=\"announcement\"/>");
    out.println("<input type=\"submit\" value=\"Make Announcement\"/>");  
    ArrayList<Challenge> reports = Challenge.getReports(userName, stmt);
    int numReports = reports.size();
    out.println("<a href=\"inbox.jsp\"> You have " + numReports + " reports.</a>");
    out.println("</form>");
    out.println("<h4>Site statistics:</h4>");
    out.println("There are " + (User.getNumberOfUsers(stmt) - 1) + " users registered.");
    out.println("There are " + TakenEvent.getNumberOfTakenEvents(stmt) + " quizzes taken.");
}
boolean isPrivate = user.isPrivate();
String status = "OFF";
if(isPrivate) status = "ON";
%>
<p>Privacy is currently: <%= status %></p>
<form action="UpdatePrivacyServlet" method="post">
<input type="submit" value="change privacy"/>
</form>

<h4>Admin Announcements:</h4>
<% 
    ArrayList<Message> announcements = Message.getAnnouncements(stmt);
    for (int i = 0; i < announcements.size() && i < 10; i++) {
		Message announcement = announcements.get(i);
		out.println("<li>" + announcement.getFromID() + " posted " + announcement.getText() + "</li>");	   
	}  

%>

<h4> Your Achievements </h4>
<%
	try {
		ResultSet rs = stmt.executeQuery("SELECT * FROM achievements WHERE userName = \"" + userName + "\";");
		while (rs.next()) {
	    	String achievement = rs.getString("achievement");
	    	out.println(achievement);
	    }
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	    
%>


<h4> Inbox </h4>

<%
	 
     ArrayList<Message> unreadMessages = Message.getUnReadMessages(userName, stmt);
     int numUnReadMessages = unreadMessages.size();
%>
<a href="inbox.jsp"> You have <%= numUnReadMessages %> messages.</a>

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
     int numCreateEvents = createEvents.size();
%>


<a href="quizCreated.jsp"> You have created <%= numCreateEvents %> quizes.</a>    


<%
     ArrayList<TakenEvent>  takenEvents= TakenEvent.getTakenEvents(userName, stmt);
	 int numTakenEvents = takenEvents.size();    
%>
 <a href="quizTaken.jsp"> You have taken <%= numTakenEvents %> quizes.</a>  	 

<p>
Most popular quizzes:
<% 
    ArrayList<Integer> popIDs = Quiz.getTopQuiz(stmt);
    for (Integer id : popIDs) {
    	String name = null;
    	try {
   			ResultSet rs = stmt.executeQuery("SELECT quizName FROM quiz WHERE quizID = " + id +";");
   			while (rs.next()) {
   		    	name = rs.getString("quizName");    	
   		    }
    		
    	} catch (SQLException e) {
    			// TODO Auto-generated catch block
    		e.printStackTrace();
   		}	    	
   		String quiz = "<a href = \"QuizSummary.jsp?id=" + id + "\"> Quiz " + id  + "</a>";	
   		out.println("<li> QUIZ: " + quiz + "</li>");   	   
    } 
%>
</p>

<p>
Recently created quizzes:
<% 
    ArrayList<Event>  recentCreateEvents = Event.getRecentCreatedQuiz(stmt);
    for (Event recent : recentCreateEvents) {
    	String creatorName = recent.getUserName();
    	User creator = User.retrieveByUserName(creatorName, stmt);
    	int quizID = recent.getQuizID(); 
    	
    	String createrNameURL;
    	if (!creator.isPrivate() || creator.getFriends().contains(userName)) {
    		createrNameURL = "<a href = \"public-profile.jsp?name=" + creatorName + "\">" + creatorName + "</a>";
    	} else {
    		createrNameURL = "anonymous";
    	}		
    	String quizURL = "<a href = \"QuizSummary.jsp?id=" + quizID + "\"> QUIZ " + quizID  + "</a>";
    	out.println("<li>" + createrNameURL + " created " + quizURL + "</li>");
    	   
    } 
%>
</p>


<p>
Your friends have created quizes:
<% 
    ArrayList<Event>  friendsCreateEvents = Event.getFriendsCreateEvent(userName, stmt);
    for (int i = 0; i < friendsCreateEvents.size() && i < 10; i++) {
    	Event friendCreateEvent = friendsCreateEvents.get(i);
    	String friendName = friendCreateEvent.getUserName();
    	System.out.println(friendName);
    	int quizID = friendCreateEvent.getQuizID();    	
    	String friendNameURL = "<a href = \"public-profile.jsp?name=" + friendName + "\">" + friendName + "</a>";
    	String quizURL = "<a href = \"QuizSummary.jsp?id=" + quizID + "\"> QUIZ " + quizID  + "</a>";
    	out.println("<li>" + friendNameURL + " created " + quizURL + "</li>");
    	   
    }  
%>
</p>

<p>
Your friends have taken quizes:
<%
    ArrayList<TakenEvent>  friendsTakenEvents = TakenEvent.getFriendsTakenEvents(userName, stmt);
    for (int i = 0; i < friendsTakenEvents.size() && i < 10; i++) {
		TakenEvent friendTakenEvent = friendsTakenEvents.get(i);
		String friendName = friendTakenEvent.getUserName();
		System.out.println(friendName);
		int quizID = friendTakenEvent.getQuizID();    	
		String friendNameURL = "<a href = \"public-profile.jsp?name=" + friendName + "\">" + friendName + "</a>";
		String quizURL = "<a href = \"QuizSummary.jsp?id=" + quizID + "\"> QUIZ " + quizID  + "</a>";
		out.println("<li>" + friendNameURL + " took " + quizURL + "</li>");
	}  
%>
</p>

<p>
Your friends have recently earned achievements:
<% 

		
	try {
		ResultSet rs = stmt.executeQuery("SELECT achievements.* FROM achievements,friends WHERE friends.userName1 = \"" + userName + "\" AND friends.userName2 = achievements.userName;");
		int count = 0;
		while (rs.next()) {
			if (count == 10) break;
	    	String friendName = rs.getString("userName");
            String achievement = rs.getString("achievement");
            String friendNameURL = "<a href = \"public-profile.jsp?name=" + friendName + "\">" + friendName + "</a>";
            out.println("<li>" + friendNameURL + " earned " + achievement + ".</li>");
            count++;
	    }
	} catch (SQLException e) {
			// TODO Auto-generated catch block
		e.printStackTrace();
	}	    
		
%>
<p>



<form action="logoutServlet" method="post">
<input type="submit" value="Logout"/>
</form>

<p>Use the search box below to find other users!</p>

<form action="findServlet" method="post">
<input type="text" name="searchTerm"/>
<input type="submit" value="Search"/>
</form>

<p>Use the search box below to find quizzes by tag!</p>

<form action="findQuizServlet" method="post">
<input type="text" name="searchTerm"/>
<input type="submit" value="Search"/>
</form>

<h1>Select which mode you want to play in: </h1> <br>


<a href = "QuizCreateForm.jsp"> Creator Mode </a> <br>
<a href = "QuizPlayerForm.jsp"> Player Mode </a> <br>
</div></div>
</body>
</html>