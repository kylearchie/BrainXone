package brainxone;

import java.io.IOException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ReportServelrt
 */
@WebServlet("/ReportServelrt")
public class ReportServelrt extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportServelrt() {
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
		int quizID = Integer.parseInt(request.getParameter("quizID"));
		String type = "report";
		String text = "This quiz is regardes as inappropriate!";
		ArrayList<String> admins = User.getAllAdminUserName(stmt);
		for (String adminName: admins) {
			Challenge challenge = new Challenge(fromUser, adminName, type, text, quizID, stmt);
		}
		RequestDispatcher dispatch = request.getRequestDispatcher("QuizSummary.jsp?id="+ quizID);
		dispatch.forward(request, response);
	}

}
