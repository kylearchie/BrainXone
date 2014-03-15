<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page
	import="java.sql.*, backend.*, java.util.*, brainxone.*, java.text.SimpleDateFormat, java.util.Date"%>

<%
	HttpSession sess = request.getSession();
	Boolean b = (Boolean) sess.getAttribute("isPracticeMode");
	if( b != null && b ) {
		sess.setAttribute("isPracticeMode", new Boolean(false));
		sess.removeAttribute("questionNumber");
		sess.removeAttribute("currentScore");
		sess.removeAttribute("currentTime");
		sess.removeAttribute("randomSeed");
	}

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" href="css/header.css">
<link rel="stylesheet" href="css/main.css">
<title>Quiz Summary!</title>
</head>
<body>
<%@ include file="header.jsp" %>
<%

    
    ServletContext servletContext = getServletContext();
    Statement stmt = (Statement) servletContext.getAttribute("Statement");
    String userName = (String) session.getAttribute("currentUser");
	int quizID = Integer.parseInt(request.getParameter("id"));

	
	
	
	

	HttpSession hs = request.getSession();


	Quiz q = Quiz.getQuizUsingID(quizID, stmt);
	out.print("<h1 class = \"page-title\"> Quiz: " + q.getName(quizID, stmt) + "</h1><br>");
	out.print("Quiz Description: " + q.getDescription() + "<br>");
	User creator = User.retrieveByUserName(q.getCreatorName(), stmt);
	String createrName;
	if (!creator.isPrivate() || creator.getFriends().contains(userName) || creator.getUserName().equals(userName)) {
		createrName = "<a href = \"public-profile.jsp?name=" + q.getCreatorName() + "\">" + q.getCreatorName() + "</a>";
	} else {
		createrName = "anonymous";
	}		
	out.println("Creator Name: " + createrName);	
	
	String guest = "guest";
	if (!userName.equals(guest)) {
		out.print("List of User's Past Performance: <br>");
		ArrayList<TakenEvent> pastPerformance = TakenEvent.getPastPerformance(userName, quizID, stmt);
		for (TakenEvent past: pastPerformance) {
			out.print("At " + past.getTime() + ", you spend " + past.getTimeTaken() + "ms taking this quiz with a score of " + past.getScore());
		}
	}
	
	
	out.print("Creator Name: <a href = \"public-profile.jsp?name=" + q.getCreatorName() + "\">" + q.getCreatorName() + "</a>" + "<br>");
	out.print("List of User's Past Performance: <br>");

	out.print("List of Highest Performance of All Time: <br>");
	ArrayList<TakenEvent> highestPerformance = TakenEvent.getBestPerformance(quizID, stmt);
	int n = 0;
	for (TakenEvent best: highestPerformance) {
		n++;
		User bestUser = User.retrieveByUserName(best.getUserName(), stmt);
		String takerNameURL;
		if (!bestUser.isPrivate() || bestUser.getFriends().contains(userName) || bestUser.getUserName().equals(userName)) {
			takerNameURL = "<a href = \"public-profile.jsp?name=" + best.getUserName() + "\">" + best.getUserName() + "</a>";
		} else {
			takerNameURL = "anonymous";
		}		
		out.print(n +".At " + best.getTime() + ", " + takerNameURL + " spend " + best.getTimeTaken() + "ms taking this quiz with a score of " + best.getScore());
	}
	
	
	out.print("List of Highest Performance in the Last Day: <br>");
	Date now = new Date();
	int dateOfToday = now.getDate();
	int dateOfYesterday = dateOfToday - 1;
	now.setDate(dateOfYesterday);
	String pattern = "yyyy-MM-dd HH:mm:ss";
	SimpleDateFormat formatter = new SimpleDateFormat(pattern);
	String yesterday = formatter.format(now);
	ArrayList<TakenEvent> yesterdaysHighestPerformance = TakenEvent.getYesterdaysBestPerformance(quizID, yesterday, stmt);
	n = 0;
	for (TakenEvent best: yesterdaysHighestPerformance) {
		n++;
		User bestUser = User.retrieveByUserName(best.getUserName(), stmt);
		String takerNameURL;
		if (!bestUser.isPrivate() || bestUser.getFriends().contains(userName) || bestUser.getUserName().equals(userName)) {
			takerNameURL = "<a href = \"public-profile.jsp?name=" + best.getUserName() + "\">" + best.getUserName() + "</a>";
		} else {
			takerNameURL = "anonymous";
		}		
		out.print(n +".At " + best.getTime() + ", " + takerNameURL + " spend " + best.getTimeTaken() + "ms taking this quiz with a score of " + best.getScore());
	}
	
	out.print("List of Performance of Recent Test Takers: <br>");
	ArrayList<TakenEvent> recentPerformance = TakenEvent.getRecentPerformance(quizID, stmt);
	n = 0;
	for (TakenEvent best: recentPerformance) {
		n++;
		User bestUser = User.retrieveByUserName(best.getUserName(), stmt);
		String takerNameURL;
		if (!bestUser.isPrivate() || bestUser.getFriends().contains(userName)  || bestUser.getUserName().equals(userName)) {
			takerNameURL = "<a href = \"public-profile.jsp?name=" + best.getUserName() + "\">" + best.getUserName() + "</a>";
		} else {
			takerNameURL = "anonymous";
		}		
		out.print(n +".At " + best.getTime() + ", " + takerNameURL + " spend " + best.getTimeTaken() + "ms taking this quiz with a score of " + best.getScore());
	}
	
	out.print("List of tags: <br>");
	try {
		ResultSet rs = stmt.executeQuery("SELECT * FROM tag WHERE quizID =" + quizID + ";");
		while (rs.next()) {
			String tag = rs.getString("tag");
	    	out.println(tag + ",");
	    }
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	    
	
	out.print("Summary of staticstics of how well users have performed on quiz: <br> <br><br>");
	if (recentPerformance.size() == 0) {
		out.println("No one has taken this quiz yet.");
	} else {
		try {
			ResultSet rs = stmt.executeQuery("SELECT count(*) AS number, avg(score) AS avgScore, avg(timeTaken) AS avgTimeTaken FROM events WHERE score IS NOT NULL AND timeTaken IS NOT NULL AND quizID = " + quizID + ";");	
			while (rs.next()) {
				int count = rs.getInt("number");
		    	double avgScore = rs.getDouble("avgScore");
		    	double avgTimeTaken = rs.getDouble("avgTimeTaken");
		    	out.println("This quiz has been taken " + count + " times with an average score of " + avgScore + " and an average time of " + avgTimeTaken + ".");
		    }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	if (userName.equals(guest)) {
		out.println("You have to <a href=\"create_new_account.jsp\"> register </a> in order to take this quiz.");
	} else {
		out.print("<li><b><a href=\"ShowQuiz.jsp?id=" + quizID + "\"> PLAY QUIZ </a></li>");
	}
	
	ArrayList<Review> reviews = Quiz.getReviewByQuizID(quizID, stmt);
	for(Review r : reviews){

		User reviewer = User.retrieveByUserName(r.reviewerName, stmt);
		String creatorNameURL;
		if (!reviewer.isPrivate() || reviewer.getFriends().contains(userName) || reviewer.getUserName().equals(userName)) {
			creatorNameURL = "<a href = \"public-profile.jsp?name=" + r.reviewerName + "\">" + r.reviewerName + "</a>";
		} else {
			creatorNameURL = "anonymous";
		}		
		out.print("From reviewer: " + creatorNameURL + "<br");
		out.print("<br>");
		//out.print("Stars: " + String.valueOf(r.stars) + "<br>");
		out.print("Text Review: " + r.textReview + "<br>");
		out.print("Stars: " + r.stars + "<br>");
	}
	
	ArrayList<String> tags = Quiz.getTagsByQuizID(quizID, stmt);
	out.print("Tags for this quiz: ");
	for(int i = 0; i < tags.size(); i++){
		String t = tags.get(i);
		if(i == (tags.size() - 1))
			
            out.print("<a href=\"quiz-results.jsp?tag=" + t + "\"> "+ t +"</a> <br>");
		else
            out.print("<a href=\"quiz-results.jsp?tag=" + t + "\"> "+ t +"</a>" + ", &nbsp; ");
	}
	if(q.hasPracticeMode() && !userName.equals(guest)){
	%>

<form action="PracticeModeServlet" method="post"><input
	type="submit" value="Practice Mode"> <input type="hidden"
	name="quizID" value='<%= request.getParameter("id") %>'></form>

<%
	} else {
		request.getSession().setAttribute("isPracticeMode", false);
	}

	if(q.getCreatorName().equals(userName)){
		out.print("<li><b><a href=\"EditQuiz.jsp?id=" + quizID + "\"> Edit this quiz </a></li>");	
	}
 
    if (!userName.equals(guest)) {
    	%>

<form action="ListFriendsServlet.jsp" method="post"><input
	type="submit" value="Challenge a Friend!"> <input type="hidden"
	name="quizID" value='<%= request.getParameter("id") %>'></form>

<form action="ReportServelrt" method="post"><input type="hidden"
	name="quizID" value='<%= request.getParameter("id") %>'> <input
	type="submit" value="Report quiz as inappropriate"></form>
<%
   }

User user = User.retrieveByUserName(userName, stmt);
if (user.isAdmin()) {
    %>
<form action="DeleteQuizServlet" method="post"><input
	type="hidden" name="quizID" value='<%= request.getParameter("id") %>'>
<input type="submit" value="Delete this quiz"></form>

<form action="DeleteQuizHistoryServlet" method="post"><input
	type="hidden" name="quizID" value='<%= request.getParameter("id") %>'>
<input type="submit" value="Delete this quiz's history"></form>





<%
}


%>
</div>
</div>
</body>
</html>
