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

        ServletContext servletContext = getServletContext();
        Statement stmt = (Statement) servletContext.getAttribute("Statement");
        User user = User.retrieveByUserName(userName, stmt);
%>
<title>Welcome <%= userName %></title>
	<link rel="stylesheet" href="css/header.css">
	<link rel="stylesheet" href="css/main.css">
	<link rel="stylesheet" href="css/main-page.css">

</head>
<body>
<%@ include file="header.jsp" %>
<div class="main-page-content">
        <div class="left-content">
            <div class="section">
                <div class="section-header">Messages</div>
                <div class="section-content">
                    <%
                         ArrayList<Message> unreadMessages = Message.getUnReadMessages(userName, stmt);
                         int numUnReadMessages = unreadMessages.size();
                    %>
                    <p><a href="inbox.jsp">You have <%= numUnReadMessages %> notes. </a></p>
                    <%
                         ArrayList<Message> friends = Message.getFriendRequests(userName, stmt);
                         int numRequests = friends.size();
                    %>
                    <p> <a href="inbox.jsp">You have <%= numRequests %> friend requests.</a></p>
                    <%
                         ArrayList<Challenge> challenges = Challenge.getChallenges(userName, stmt);
                         int numChallenges = challenges.size();
                    %>
                    <p> <a href="inbox.jsp">You have <%= numChallenges %> challenges.</a></p>
                </div>
            </div>
            <div class="section">
                <div class="section-header">Your Stats</div>
                <div class="section-content">
                    <%
                         ArrayList<Event>  createEvents = Event.getCreateEvents(userName, stmt);
                         int numCreateEvents = createEvents.size();
                    %>
                    <p><a href="quizCreated.jsp"> You have created <%= numCreateEvents %> quizes.</a></p>    
                    <%
                         ArrayList<TakenEvent>  takenEvents= TakenEvent.getTakenEvents(userName, stmt);
                         int numTakenEvents = takenEvents.size();    
                    %>
                     <p><a href="quizTaken.jsp"> You have taken <%= numTakenEvents %> quizes.</a></p>
                </div>
            </div>
            <div class="section">
                <div class="section-header">Your Achievments</div>
                <div class="section-content">
                    <ul class="striped-list">
                    <%
                        try {
                            ResultSet rs = stmt.executeQuery("SELECT * FROM achievements WHERE userName = \"" + userName + "\";");
                            while (rs.next()) {
                                String achievement = rs.getString("achievement");
                                out.println("<li>" + achievement + "</li>");
                            }
                        } catch (SQLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }       
                    %>
                    </ul>
                </div>
             </div>
            <div class="section">
                <div class="section-header">Privacy</div>
                <div class="section-content">
                    <%
                        boolean isPrivate = user.isPrivate();
                        String status = "<span class='negative-text'> OFF </span>";
                        if(isPrivate) status = "<span class='positive-text'> ON </span>";
                    %>
                    <p>Privacy is currently: <%= status %></p>
                    <form action="UpdatePrivacyServlet" method="post">
                        <input class="move-on-button button" type="submit" value="change privacy"/>
                    </form>
                </div>
            </div>
        </div>
        <div class="center-content">
            <div class="section welcome-banner">
                <h1>Welcome, <%= userName %>!</h1>
                <a class="logout-text" href="logoutServlet">Logout</a>
            </div>
            <div class="section">
                <div class="section-header">Announcements</div>
                <div class="section-content">
                <% 
                    ArrayList<Message> announcements = Message.getAnnouncements(stmt);
                    if(announcements.size() == 0) out.println("<p class='no-announcements'> -- No announcements -- </p>");
                    else {
                        out.println("<ul>");
                        for (int i = 0; i < announcements.size() && i < 10; i++) {
                            Message announcement = announcements.get(i);
                            out.println("<li>" + announcement.getFromID() + " posted " + announcement.getText() + "</li>");    
                        }  
                        out.println("</ul>");
                    }
                    if (user.isAdmin()) {
                        out.println("<p>Make a new announcement:</p>");
                        out.println("<form action=\"MakeAnnouncementServlet\" method=\"post\">");
                        out.println("Your announcement: <input type=\"text\" name=\"announcement\"/>");
                        out.println("<input type=\"submit\" value=\"Make Announcement\"/>");  
                    }
                %>
                </div>
            </div>
            <% if(user.isAdmin()){ %>
                <div class="section">
                    <div class="section-header">Admin stats</div>
                    <div class="section-content">
                        <%
                            ArrayList<Challenge> reports = Challenge.getReports(userName, stmt);
                            int numReports = reports.size();
                            out.println("<a href=\"inbox.jsp\"> You have " + numReports + " reports.</a>");
                            out.println("</form>");
                            out.println("<h4>Site statistics:</h4>");
                            out.println("There are " + (User.getNumberOfUsers(stmt) - 1) + " users registered.");
                            out.println("There are " + TakenEvent.getNumberOfTakenEvents(stmt) + " quizzes taken.");
                        %>
                    </div>
                </div>
            <% } %>
                    
            <div class="section">
                <div class="section-header">Take these popular quizzes!</div>
                <div class="section-content">
                    <ul class="striped-list">
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
                                    e.printStackTrace();
                                }            
                                out.println("<li><a href = 'QuizSummary.jsp?id=" + id + "'>" + name  + "</a></li>");       
                            } 
                        %>
                    </ul>
                </div>
            </div>
            
        </div>
        <div class="right-content">
            <div class="section">
                <div class="section-header">Search</div>
                <div class="section-content"></div>
            </div>
            <div class="section">
                <div class="section-header">News</div>
                <div class="section-content">
                    <p>Recently created quizzes:
                    <ul class="striped-list">
                        <%
                            ArrayList<Event> recentCreateEvents = Event
                                    .getRecentCreatedQuiz(stmt);
                            for (Event recent : recentCreateEvents) {
                                String creatorName = recent.getUserName();
                                User creator = User.retrieveByUserName(creatorName, stmt);
                                int quizID = recent.getQuizID();
                                Quiz q = Quiz.getQuizUsingID(quizID, stmt);

                                String createrNameURL;
                                if (!creator.isPrivate()
                                        || creator.getFriends().contains(userName)) {
                                    createrNameURL = "<a href = \"public-profile.jsp?name="
                                            + creatorName + "\">" + creatorName + "</a>";
                                } else {
                                    createrNameURL = "anonymous";
                                }
                                String quizName = q.getName(quizID, stmt);
                                System.out.println( q.getName(quizID, stmt) );
                                String quizURL = "<a href = \"QuizSummary.jsp?id=" + quizID + "\"> " + q.getName(quizID, stmt) + "</a>";
                                out.println("<li>" + createrNameURL + " created " + quizURL + "</li>");

                            }
                        %>
                    </ul>
                    </p>
                    <p>Your friends have created quizes:
                    <ul class="striped-list">
                        <%
                            ArrayList<Event> friendsCreateEvents = Event.getFriendsCreateEvent(
                                    userName, stmt);
                            for (int i = 0; i < friendsCreateEvents.size() && i < 10; i++) {
                                Event friendCreateEvent = friendsCreateEvents.get(i);
                                String friendName = friendCreateEvent.getUserName();
                                System.out.println(friendName);
                                int quizID = friendCreateEvent.getQuizID();
                                Quiz q = Quiz.getQuizUsingID(quizID, stmt);

                                String friendNameURL = "<a href = \"public-profile.jsp?name="
                                        + friendName + "\">" + friendName + "</a>";
                                String quizURL = "<a href = \"QuizSummary.jsp?id=" + quizID
                                        + "\"> " + q.getName(quizID, stmt) + "</a>";
                                out.println("<li>" + friendNameURL + " created " + quizURL
                                        + "</li>");

                            }
                        %>
                    </ul>
                    </p>

                    <p>Your friends have taken quizes:
                    <ul class="striped-list">
                        <%
                            ArrayList<TakenEvent> friendsTakenEvents = TakenEvent
                                    .getFriendsTakenEvents(userName, stmt);
                            for (int i = 0; i < friendsTakenEvents.size() && i < 10; i++) {
                                TakenEvent friendTakenEvent = friendsTakenEvents.get(i);
                                String friendName = friendTakenEvent.getUserName();
                                System.out.println(friendName);
                                int quizID = friendTakenEvent.getQuizID();
                                Quiz q = Quiz.getQuizUsingID(quizID, stmt);

                                String friendNameURL = "<a href = \"public-profile.jsp?name="
                                        + friendName + "\">" + friendName + "</a>";
                                String quizURL = "<a href = \"QuizSummary.jsp?id=" + quizID
                                        + "\">" + q.getName(quizID, stmt) + "</a>";
                                out.println("<li>" + friendNameURL + " took " + quizURL
                                        + "</li>");
                            }
                        %>
                    </ul>
                    </p>

                    <p>Your friends have recently earned achievements:
                    <ul class="striped-list">
                        <%
                            try {
                                ResultSet rs = stmt
                                        .executeQuery("SELECT achievements.* FROM achievements,friends WHERE friends.userName1 = \""
                                                + userName
                                                + "\" AND friends.userName2 = achievements.userName;");
                                int count = 0;
                                while (rs.next()) {
                                    if (count == 10)
                                        break;
                                    String friendName = rs.getString("userName");
                                    String achievement = rs.getString("achievement");
                                    String friendNameURL = "<a href = \"public-profile.jsp?name="
                                            + friendName + "\">" + friendName + "</a>";
                                    out.println("<li>" + friendNameURL + " earned "
                                            + achievement + ".</li>");
                                    count++;
                                }
                            } catch (SQLException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        %>
                    </ul>
                    </p>
                </div>
            </div>
        </div>
    </div>

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