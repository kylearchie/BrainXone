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
 * Servlet implementation class AddSingleStrAns
 */
@WebServlet("/AddSingleStrAns")
public class AddSingleStrAns extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddSingleStrAns() {
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
		String singleStrAnsTxt = (String) request.getParameter("singleStrAnsTxt");
		StringResponse ques = (StringResponse) hs.getAttribute("question");
		ArrayList<String> answerKeys = new ArrayList<String>();
		answerKeys.add(singleStrAnsTxt);
		ques.setAnswer(answerKeys, 1, stmt);
		
		try {
			stmt.executeUpdate("UPDATE ques SET maxPoints = " + 1 + " WHERE quesID = " + ques.getID());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		RequestDispatcher rd = request.getRequestDispatcher("MoreOrSubmit.jsp");
        rd.forward(request, response);
	}

}
