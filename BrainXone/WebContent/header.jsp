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
			<li><a href="#">My Account</a></li>
			<li><a href="#">Messages</a></li>
			<li><a href="#">Browse</a></li>
			<li><a href="#">Make Quiz</a></li>
			<li><a href="faqs.jsp">Help!</a></li>
		</ul>
	</nav>
</header>