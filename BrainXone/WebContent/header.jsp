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
			<li><a href="welcome.jsp">My Account</a></li>
			<li><a href="inbox.jsp">Messages</a></li>
			<li><a href="QuizPlayerForm.jsp">Browse</a></li>
			<li><a href="QuizCreateForm.jsp">Make Quiz</a></li>
			<li><a href="faqs.jsp">Help!</a></li>
		</ul>
	</nav>
</header>