package backend;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class CheckAnswerMultiChoiceServlet
 */
@WebServlet("/CheckAnswerMultiChoiceServlet")
public class CheckAnswerMultiChoiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckAnswerMultiChoiceServlet() {
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
		HashSet<String> selectedOptions = new HashSet<String>();
		String quesID = Integer.toString((Integer) hs.getAttribute("quesID"));
		Question ques = (Question) hs.getAttribute(quesID);
		
		for(int i = 0; i < 2; i ++)
		{
			String option = (String) request.getParameter("options" + (i + 1));
			String isValid = (String)(request.getParameter("isValid" + (i + 1)));
			if(isValid.equals("1"))
				selectedOptions.add(option);
		}
		
		ques.checkAnswer(selectedOptions);
		System.out.println(ques.getPoints());
	}

}
