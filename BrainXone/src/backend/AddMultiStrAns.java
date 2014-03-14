package backend;

import java.io.IOException;
import java.sql.SQLException;
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
		ServletContext servletContext = getServletContext();
		Statement stmt = (Statement) servletContext.getAttribute("Statement");
		Quiz q = (Quiz) hs.getAttribute("quiz");
		
		ArrayList< ArrayList<String> > answerKeys = new ArrayList< ArrayList<String> >();
		
		StringResponse ques = (StringResponse) hs.getAttribute("question");
		
		int answerCounter = 0;
			while( true ) {
				answerCounter++;
				if(ques.getType() != Question.MULTI_STR_ANS && answerCounter > 1) break; //Just in case.
				String a = "answer" + answerCounter;
				int optionCounter = 0;
				String ao, aoString;
				ArrayList<String> options = new ArrayList<String>();
				while( true ) {
					optionCounter++;
					ao = a + "option" + optionCounter;
					aoString = request.getParameter(ao);
					if(aoString == null || aoString.isEmpty()) break;
					options.add(aoString);
				}
				if(aoString == null) break;
				answerKeys.add(options);
			
		}

		int maxPoints = 0;
		if(q.hasPracticeMode()) {
			maxPoints = Integer.parseInt((String) request.getParameter("maxPoints"));
			maxPoints = Math.max(maxPoints, answerKeys.size());

		}
		ques.setAnswer(answerKeys, maxPoints, stmt);
		
		try {
			stmt.executeUpdate("UPDATE ques SET maxPoints = " + maxPoints + " WHERE quesID = " + ques.getID());
		} catch (SQLException e) {
			e.printStackTrace();
		}

		RequestDispatcher rd = request.getRequestDispatcher("MoreOrSubmit.jsp");
		rd.forward(request, response);
	}
}
