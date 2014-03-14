<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
    <%@ page import = "java.sql.*, brainxone.*, java.util.*, backend.*" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
</title>
	<link rel="stylesheet" href="css/header.css">
	<link rel="stylesheet" href="css/main.css">
</head>
<body>
<%@ include file="header.jsp" %>
	<div class="central-content">
		<div class="content-pane">

		<h1 class="page-title"> Welcome guest</h1>
<%
ServletContext servletContext = getServletContext();
Statement stmt = (Statement) servletContext.getAttribute("Statement");
%>
<h4>Admin Announcements:</h4>
<% 
    ArrayList<Message> announcements = Message.getAnnouncements(stmt);
    for (int i = 0; i < announcements.size() && i < 10; i++) {
		Message announcement = announcements.get(i);
		out.println("<li>" + announcement.getFromID() + " posted " + announcement.getText() + "</li>");	   
	}  

%>
 
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
   		String quiz = "<a href = \"ShowQuiz.jsp?id=" + id + "\"> " + name  + "</a>";	
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
    	if (!creator.isPrivate()) {
    		createrNameURL = "<a href = \"public-profile.jsp?name=" + creatorName + "\">" + creatorName + "</a>";
    	} else {
    		createrNameURL = "anonymous";
    	}		

    	String quizURL = "<a href = \"QuizSummary.jsp?id=" + quizID + "\"> QUIZ " + quizID  + "</a>";
    	out.println("<li>" + createrNameURL + " created " + quizURL + "</li>");
    	   
    } 
%>
</p>

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

<h1>Select which mode you want to play in: </h1> <br>


<a href = "QuizCreateForm.jsp"> Creator Mode </a> <br>
<a href = "QuizPlayerForm.jsp"> Player Mode </a> <br>
</div></div>
</body>
</html>