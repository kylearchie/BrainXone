package brainxone;

import java.io.IOException;
import java.sql.Statement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class AcceptChallengeServlet
 */
@WebServlet("/AcceptChallengeServlet")
public class AcceptChallengeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AcceptChallengeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ServletContext servletContext = getServletContext();
		Statement stmt = (Statement) servletContext.getAttribute("Statement");
	    String currentUser = request.getParameter("currentUser");
		String friendUser = request.getParameter("friendUser");
		String timeSent = request.getParameter("timeSent");
		int quizID = Integer.parseInt(request.getParameter("quizID"));
		Challenge.deleteChallenge(currentUser, friendUser, timeSent, stmt);
		RequestDispatcher dispatch = request.getRequestDispatcher("QuizSummary.jsp?id="+ quizID);
		dispatch.forward(request, response);	
	}

}
