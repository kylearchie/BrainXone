<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import = "java.sql.*,backend.*,java.util.*" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Edit this question</title>
</head>
<body>
<%
	HttpSession hs = request.getSession();
	ServletContext servletContext = getServletContext();
	Statement stmt = (Statement) servletContext.getAttribute("Statement");
	Question ques = (Question) hs.getAttribute("Question");
	hs.setAttribute("Question", ques);
%>
<form action="CheckAnswerMultiChoiceServlet" method="post">
<%
	HashMap<String, Integer> options = ques.displayAnswers(stmt);
	hs.setAttribute("ansCount", options.size());
	int i = 0;
	for (String str : options.keySet()) {
		//out.print("<input type = 'checkbox' name = 'selected' value='selectedOption"+ (i + 1) + "'> " + str + " <br>");
		out.print("<input type = 'text' name = 'options" + (i + 1)
				+ "' value = '" + str + "'>");
		int isValid = options.get(str);
		if (isValid == 1) {
			out.print("<input type = 'radio' name= 'isValid"
					+ (i + 1)
					+ "' value = '1' checked> YES &nbsp; <input type = 'radio'  name='isValid"
					+ (i + 1) + "' value = '0'> NO <br>");
		} else {
			out.print("<input type = 'radio' name= 'isValid"
					+ (i + 1)
					+ "' value = '1'> YES &nbsp; <input type = 'radio'  name='isValid"
					+ (i + 1) + "' value = '0' checked> NO <br>");
		}
		i++;
	}
%> <input type="submit" value="Submit Ans"></form>
	
	
</body>
</html>