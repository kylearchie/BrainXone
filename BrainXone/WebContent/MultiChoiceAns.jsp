<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Multi Choice Answers With Multiple Corr Ans</title>
</head>
<body>
<h1> Type in all the answers, select if answer is correct ans or not </h1>

<form action = "AddMultiChoiceAns" method = "post">

<%

for(int i = 0; i < 2; i++)
{
	out.print("Option " + (i + 1) + "&nbsp; <input type ='text'  name='options"+ (i + 1) + "'>");
	out.print("Is this the correct option? <input type = 'radio' name='isValid"+ (i + 1) + "' value = '1'> YES &nbsp; <input type = 'radio'  name='isValid"+ (i + 1) + "' value = '0'> NO <br>");
}
%>
<br>
<input type = "submit" value = "Add All Answers">
</form>
</body>
</html>