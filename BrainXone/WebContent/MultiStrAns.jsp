<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Multi String Answers</title>
</title>
	<link rel="stylesheet" href="css/header.css">
	<link rel="stylesheet" href="css/main.css">
</head>
<body>
<%@ include file="header.jsp" %>
	<div class="central-content">
		<div class="content-pane">

		<h1 class="page-title"> Enter the correct answers</h1>
<form action="AddMultiStrAns" method="post">

<% for(int i = 0; i < 3; i++) {
	out.print("Answer " + (i + 1) + "&nbsp; <input type ='text'  name='multiStringAns"+ (i + 1) + "'>");
}
	out.print("Number of correct answers(maxPoints) <input type = 'text' name = 'maxPoints'>");
%>

<input type = "submit" value = "Add All Answers">
</form>
</div></div>
</body>
</html>