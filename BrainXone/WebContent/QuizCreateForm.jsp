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
Input a name for the quiz: <input type = "text" name = "quizName"> <br>
Input description: <input type = "text" name = "description"> <br>

Select category: <select name = "category">  

<option value = "LangVocab"> Language And Vocabulary </option>
<option value = "Math"> Math </option>
<option value = "Science"> Science </option>
<option value = "History"> History </option>
<option value = "Geography"> Geography </option>
<option value = "Arts"> Arts </option>
<option value = "Computers"> Computers </option>
<option value = "Other"> Other </option>
</select>
<br> <br>

Display in Random Order? 
<input type = "radio" name = "isRandom" value = "1"> YES &nbsp;
<input type = "radio" name = "isRandom" value = "0" checked> NO <br>

Display all questions in One Page? 
<input type = "radio" name = "isOnePage" value = "1" checked> YES &nbsp;
<input type = "radio" name = "isOnePage" value = "0"> NO <br>

Is the quiz available for practice? 
<input type = "radio" name = "isPracticeMode" value = "1" checked> YES &nbsp;
<input type = "radio" name = "isPracticeMode" value = "0"> NO <br> <br>

Add Tags: Separate by <b>spaces</b>
<input type = "text" name = "tags">  <br> <br>


<input type = "submit" value = "Move to add questions">
</form>
</body>
</html>