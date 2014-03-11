<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<link rel="stylesheet" href="css/header.css">
	<link rel="stylesheet" href="css/main.css">
<title>Player Mode: HomePage</title>
</head>
<body>

<%@ include file="header.html" %>

<h1>Select the category:</h1>

<form action="CategorySelectionServlet" method = "post">
Select category: <select name = "category">  
<option value = "Food"> Food </option>
<option value = "Drink"> Drink </option>
</select>
<br> <br>
<input type = "submit" value = "Move to see the list of quizzes in the selected category">
</form>
</body>
</html>