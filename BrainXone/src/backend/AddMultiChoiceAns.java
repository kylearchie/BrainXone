package backend;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
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
		
		ServletContext servletContext = getServletContext();
		Statement stmt = (Statement) servletContext.getAttribute("Statement");
//		
//		for(int i = 0; i < 2; i ++)
//		{
//			String option = (String) request.getParameter("options" + (i + 1));
//			String isValid = (String)(request.getParameter("isValid" + (i + 1)));
//			answerKeys.put(option, Integer.parseInt(isValid));
//		}
		Question ques = (Question) hs.getAttribute("question");
		int counter = 0;
		boolean found = false;
		while( true ) {
			counter ++;
			String option = (String) request.getParameter("option" + counter);
			if( option == null ) break;
			String isValid = (String)(request.getParameter("valid" + counter));
			int validInt;
			if( isValid != null ) validInt = Integer.parseInt(isValid);
			else validInt = 0;
			int toPut = validInt;
			if(ques.getType() == Question.MULTI_CHOICE_R && found) {
				toPut = 0;
			}
			if(validInt == 1) found = true;
			answerKeys.put(option, toPut);
		}
		
		MultiChoice mcQues = (MultiChoice) hs.getAttribute("question");
		int maxPoints = mcQues.setAnswer(answerKeys, stmt);
		
		try {
			stmt.executeUpdate("UPDATE ques SET maxPoints = " + maxPoints + " WHERE quesID = " + ques.getID());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		RequestDispatcher rd = request.getRequestDispatcher("MoreOrSubmit.jsp");
        rd.forward(request, response);
        }

}
