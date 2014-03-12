package backend;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AddMultiStrAns
 */
@WebServlet("/AddMultiStrAns")
public class AddMultiStrAns extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddMultiStrAns() {
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
		ArrayList<String> answerKeys = new ArrayList<String>();

		for(int i = 0; i < 3; i ++)
		{
			String answer = (String) request.getParameter("multiStringAns" + (i + 1));
			answerKeys.add(answer);
		}

		StringResponse ques = (StringResponse) hs.getAttribute("question");
		int maxPoints = Integer.parseInt((String) request.getParameter("maxPoints"));
		ques.setAnswer(answerKeys, maxPoints);
		
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
