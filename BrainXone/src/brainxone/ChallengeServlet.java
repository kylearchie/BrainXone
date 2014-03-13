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
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ChallengeServlet
 */
@WebServlet("/ChallengeServlet")
public class ChallengeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChallengeServlet() {
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
		HttpSession session = request.getSession();
		String fromUser = (String) session.getAttribute("currentUser");
		System.out.println(request.getParameter("quizID"));
		int quizID = Integer.parseInt(request.getParameter("quizID"));
		String toUser = request.getParameter("friendUser");
		String text = request.getParameter("message");
		String type = request.getParameter("type");	
		Challenge challenge = new Challenge(fromUser, toUser, type, text,quizID, stmt);		
		RequestDispatcher dispatch = request.getRequestDispatcher("QuizSummary.jsp?id="+ quizID);
		dispatch.forward(request, response);	
	}

}
