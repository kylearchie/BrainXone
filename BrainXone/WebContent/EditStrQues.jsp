<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import = "java.sql.*, backend.*, java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edit this question</title>
</head>
<body>
<%
	//pass the question to me.
	//isOrdered is not touched
	HttpSession hs = request.getSession();
	ServletContext servletContext = getServletContext();
	Statement stmt = (Statement) servletContext.getAttribute("Statement");
	StringResponse ques = (StringResponse) hs.getAttribute("Question");
	hs.setAttribute("Question", ques);
%>
	<form action="EditQuesServlet" method = "post">
	<input type = "text" name = "quesText" value = "<%= ques.getQuesType() %>"> <br>
<%

	HashMap<Integer, ArrayList<String>> answerKeys = ques.getAnswerKeys(stmt);
	hs.setAttribute("ansCount", answerKeys.size());
	for(int i = 1; i <= answerKeys.size(); i++){
		ArrayList<String> ans = answerKeys.get(i);
		//TODO change it to arraylist
		String oneAns = ans.get(0);
		out.print("<input type ='text'  name='ans"+ i + "' value = '" + oneAns + "'>");
	}

%>
</form>
</body>
</html>