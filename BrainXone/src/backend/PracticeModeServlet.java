package backend;

import java.io.IOException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class PracticeModeServlet
 */
@WebServlet("/PracticeModeServlet")
public class PracticeModeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PracticeModeServlet() {
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
		int quizID = Integer.parseInt(request.getParameter("quizID"));

		Quiz q = Quiz.getQuizUsingID(quizID, stmt);

		ArrayList<Question> quesList = Quiz.getQuesListUsingID(quizID, stmt);
		
		HashMap<Question, Integer> curScore = new HashMap<Question, Integer>();
		
		for(Question ques : quesList){
			curScore.put(ques, 0);
		}

		hs.setAttribute("curScore" , curScore);
		
		RequestDispatcher rd = request.getRequestDispatcher("PracticeMode.jsp");
        rd.forward(request, response);
	}

}
