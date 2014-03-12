<%@ page language="java" contentType="text/html; charset=US-ASCII"
    pageEncoding="US-ASCII"%>
    <%@ page import = "java.sql.*, brainxone.*, java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=US-ASCII">
<%


String userName = (String) session.getAttribute("currentUser");
ServletContext servletContext = getServletContext();
Statement stmt = (Statement) servletContext.getAttribute("Statement");
User user = User.retrieveByUserName(userName, stmt);


%>

<title><%=(String)request.getParameter("name")%></title>
</head>
<body>
<h1><%=(String)request.getParameter("name")%></h1>
<% 
ArrayList<String> friends = user.getFriends();
String profileName = (String)request.getParameter("name");
User profileUser = User.retrieveByUserName(profileName, stmt);
if (!userName.equals(profileName) && !friends.contains(profileName)) {
	out.println("<form action=\"AddFriendServlet\" method=\"post\">");
	out.println("<input type=\"hidden\" name=\"currentUser\" value=\"" + userName + "\"/>");
	out.println("<input type=\"hidden\" name=\"friendUser\" value=\"" + profileName + "\"/>");
	out.println("<input type=\"hidden\" name=\"type\" value=\"friend\"/>");
	out.println("Your message: <input type=\"text\" name=\"message\"/>");
    out.println("<input type=\"submit\" value=\"Send Friend Request\"/>");   
    out.println("</form>");
} else if (friends.contains(profileName)) {
	out.println("<form action=\"AddFriendServlet\" method=\"post\">");
	out.println("<input type=\"hidden\" name=\"currentUser\" value=\"" + userName + "\"/>");
	out.println("<input type=\"hidden\" name=\"friendUser\" value=\"" + profileName + "\"/>");
	out.println("<input type=\"hidden\" name=\"type\" value=\"message\"/>");
	out.println("Your message: <input type=\"text\" name=\"message\"/>");
    out.println("<input type=\"submit\" value=\"Send Message\"/>");   
    out.println("</form>");	
    
    
    
    out.println("<form action=\"RemoveFriendServlet\" method=\"post\">");
	out.println("<input type=\"hidden\" name=\"currentUser\" value=\"" + userName + "\"/>");
	out.println("<input type=\"hidden\" name=\"friendUser\" value=\"" + profileName + "\"/>");
	out.println("<input type=\"hidden\" name=\"type\" value=\"friend\"/>");
    out.println("<input type=\"submit\" value=\"Remove Friend\"/>");   
    out.println("</form>");	
} 
if (!profileUser.isAdmin() && user.isAdmin()) {
	out.println("<form action=\"PromoteServlet\" method=\"post\">");
	out.println("<input type=\"hidden\" name=\"currentUser\" value=\"" + userName + "\"/>");
	out.println("<input type=\"hidden\" name=\"promotedUser\" value=\"" + profileName + "\"/>");
    out.println("<input type=\"submit\" value=\"Promote to administrator\"/>");   
    out.println("</form>");
    
    out.println("<form action=\"DeleteServlet\" method=\"post\">");
	out.println("<input type=\"hidden\" name=\"currentUser\" value=\"" + userName + "\"/>");
	out.println("<input type=\"hidden\" name=\"deletedUser\" value=\"" + profileName + "\"/>");
    out.println("<input type=\"submit\" value=\"Delete User\"/>");   
    out.println("</form>");
    
}


%>

<h4> <%= profileName %>'s Achievements </h4>
<%
	try {
		ResultSet rs = stmt.executeQuery("SELECT * FROM achievements WHERE userName = \"" + profileName + "\";");
		while (rs.next()) {
	    	String achievement = rs.getString("achievement");
	    	out.println(achievement);
	    }
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}	    
%>

<%
     ArrayList<Event>  createEvents = Event.getCreateEvents(profileName, stmt);
     int numCreateEvents = createEvents.size();
%>

<%
out.println(profileUser + "<h4>has created" + numCreateEvents + "quizzes: </h4>");
out.println("<ul>");
for (Event createEvent : createEvents) 
{
	String name = "<a href = \"ShowQuiz.jsp?id=" + createEvent.getQuizID() + "\"> QUIZ " + createEvent.getQuizID()  + "</a>";
	out.println("<li>" + name + "</li>");
}
%>   


<%
     ArrayList<TakenEvent>  takenEvents= TakenEvent.getTakenEvents(profileName, stmt);
	 int numTakenEvents = takenEvents.size();    
%>

<%
out.println(profileUser + "<h4>has taken" + numTakenEvents + "quizzes: </h4>");
out.println("<ul>");
for (TakenEvent takenEvent : takenEvents) 
{
	String name = "<a href = \"ShowQuiz.jsp?id=" + takenEvent.getQuizID() + "\"> QUIZ " + takenEvent.getQuizID()  + "</a>";
	out.println("<li>" + name + "</li>");
}
%>    	

</body>
</html>