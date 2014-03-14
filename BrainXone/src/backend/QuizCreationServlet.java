package backend;

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
 * Servlet implementation class QuizCreationServlet
 */
@WebServlet("/QuizCreationServlet")
public class QuizCreationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public QuizCreationServlet() {
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
		HttpSession hs = request.getSession();
		ServletContext servletContext = getServletContext();
		Statement stmt = (Statement) servletContext.getAttribute("Statement");
		String quizName = (String) request.getParameter("quizName");
		String description = (String) request.getParameter("description");
		String category = (String) request.getParameter("category");
		int isRandom = Integer.parseInt((String) request.getParameter("isRandom"));
		int isOnePage = Integer.parseInt((String) request.getParameter("isOnePage"));
		int isPracticeMode = Integer.parseInt((String) request.getParameter("isPracticeMode"));
		int isImmediateCorrection = Integer.parseInt((String) request.getParameter("isImmediateCorrection"));
		String allTags = (String)request.getParameter("tags");
		request.getSession().setAttribute("tags", allTags);

		// userName hardcoded 1 here.
		String creatorName = (String) hs.getAttribute("currentUser");
		Quiz q = new Quiz(false, quizName, description, creatorName, category, isRandom, isOnePage, isPracticeMode, isImmediateCorrection);
		q.pushToQuizDB(stmt);
		hs.setAttribute("quiz", q);
		RequestDispatcher rd = request.getRequestDispatcher("AddQuestion.jsp");
        rd.forward(request, response);
	}
}
