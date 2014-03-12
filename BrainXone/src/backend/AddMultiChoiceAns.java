package backend;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AddMultiChoiceAns
 */
@WebServlet("/AddMultiChoiceAns")
public class AddMultiChoiceAns extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddMultiChoiceAns() {
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
		HashMap<String, Integer> answerKeys = new HashMap<String, Integer>();

		for(int i = 0; i < 2; i ++)
		{
			String option = (String) request.getParameter("options" + (i + 1));
			String isValid = (String)(request.getParameter("isValid" + (i + 1)));
			answerKeys.put(option, Integer.parseInt(isValid));
		}
		
		MultiChoice ques = (MultiChoice) hs.getAttribute("question");
		int maxPoints = ques.setAnswer(answerKeys);
		
		DBConnection conn = new DBConnection();
		Statement stmt = conn.getStmt();
		try {
			stmt.executeUpdate("UPDATE ques SET maxPoints = " + maxPoints + " WHERE quesID = " + ques.getID());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		RequestDispatcher rd = request.getRequestDispatcher("MoreOrSubmit.jsp");
        rd.forward(request, response);
        }

}
