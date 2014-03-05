<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Quiz Creator Form</title>
</head>
<body>

<h1> Step 1: Please input the description and category of this quiz and hit submit when you're ready to input questions </h1> <br>

<form action="QuizCreationServlet" method = "post">
Input description: <input type = "text" name = "description"> <br>
Select category: <select name = "category">  
<option value = "Food"> Food </option>
<option value = "Drink"> Drink </option>
</select>
<br> <br>
<input type = "submit" value = "Move to add questions">
</form>
</body>
</html>