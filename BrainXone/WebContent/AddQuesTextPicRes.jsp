<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Add a question!</title>
	<link rel="stylesheet" href="css/header.css">
	<link rel="stylesheet" href="css/main.css">
</head>
<body>
<%@ include file="header.jsp" %>
	<div class="central-content">
		<div class="content-pane">

		<h1 class="page-title">Step 2-a: Please add question text and relevant instructions: </h1>

<form action="AddQuestionServlet" method = "post">
<%
String instructions = (String) request.getSession().getAttribute("instructions");
out.println("<b>" + instructions + "</b><br>");
%>
Input Question Text: <input type = "text" name = "quesText"> 
<br> <br>

Input Image URL: <input type = "text" name = "quesImgURL"> 
<br> <br>

<input type = hidden name = "isOrdered">

<input type = "submit" value = "Add answer">
</form>

</body>
</html>