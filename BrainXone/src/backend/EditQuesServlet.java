package backend;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class EditQuesServlet
 */
@WebServlet("/EditQuesServlet")
public class EditQuesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditQuesServlet() {
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
		Question ques = (Question) hs.getAttribute("Question");
		
		int type = ques.getType();
		switch(type){
		case Question.SINGLE_STR_ANS:
		case Question.FIB:
		case Question.PICTURE_RESPONSE:
		case Question.MULTI_STR_ANS:
			updateStringResponse(request, stmt);
			break;
		case Question.MULTI_CHOICE_C:
		case Question.MULTI_CHOICE_R:
			updateMultiChoice(request, stmt);
			break;
		}
	}
	
	private void updateStringResponse(HttpServletRequest request, Statement stmt){
		HttpSession hs = request.getSession();
		int ansCount = (Integer) hs.getAttribute("ansCount");
		StringResponse ques = (StringResponse) hs.getAttribute("Question");
		int maxPoints = Integer.parseInt((String) hs.getAttribute("maxPoints")); 
		for(int i = 1; i <= ansCount; i++){
			String oneKey = (String) request.getParameter("ans" + i);
			try {
				String sql = "UPDATE indexAnswer SET ans = " + oneKey + " WHERE quesID = " + ques.getID() + " AND ansIndex = " + i;
				stmt.executeUpdate(sql);
				System.out.println(sql);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		maxPoints = Math.max(maxPoints, ansCount);
		try {
			stmt.executeUpdate("UPDATE ques SET maxPoints = " + maxPoints + " WHERE quesID = " + ques.getID());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void updateMultiChoice(HttpServletRequest request, Statement stmt){
		HttpSession hs = request.getSession();
		int ansCount = (Integer) hs.getAttribute("ansCount");
		MultiChoice ques = (MultiChoice) hs.getAttribute("Question");
		
		try {
			stmt.executeUpdate("DELETE FROM ansOptions WHERE quesID = \"" + ques.getID() + "\";");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		int maxPoints = 0;
		for(int i = 1; i <= ansCount; i ++)
		{
			String option = (String) request.getParameter("options" + i);
			int isValid = Integer.parseInt((String)(request.getParameter("isValid" + i)));
			if(isValid == 1)
				maxPoints++;
			try {
				stmt.executeUpdate("INSERT INTO ansOptions VALUES (\"" + ques.getID() +"\",\"" + option + "\",\"" + isValid + "\");");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		try {
			stmt.executeUpdate("UPDATE ques SET maxPoints = " + maxPoints + " WHERE quesID = " + ques.getID());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
