<%
	if(session.getAttribute("currentUser") == null) {
		response.sendRedirect("Login.jsp");
		return;
	}
%>

<header>
	<div class="logo">
		<a href="index.html"> <img src="images/QuiZoneLogo.svg">
		</a>
	</div>
	<nav>
		<ul>
			<li><a href="inbox.jsp">Messages</a></li>
			<li><a href="QuizPlayerForm.jsp">Browse Quiz</a></li>
			<li><a href="QuizCreateForm.jsp">Make Quiz</a></li>
			<li><a href="faqs.jsp">Help</a></li>
			<li><a class="logout-text" href="logoutServlet">Logout</a></li>
			
		</ul>
	</nav>
</header>