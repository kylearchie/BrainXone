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

<form action="InputQuesTextServlet" method = "post">
Select Question Type: <select name = "quesType">  
<option value = "2"> Fill in the Blank </option>
<option value = "4"> Picture Response </option>
<option value = "6"> Single String Response </option>
<option value = "8"> Multiple String Response </option>
<option value = "1"> Multiple Choice Single Correct Answer </option>
<option value = "3"> Multiple Choice Multiple Correct Answers </option>
</select> <br>
<input type = "submit" value = "Add answer">
</form>
</body>
</html>