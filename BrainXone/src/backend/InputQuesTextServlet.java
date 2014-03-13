package backend;

import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class InputQuesTextServlet
 */
@WebServlet("/InputQuesTextServlet")
public class InputQuesTextServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InputQuesTextServlet() {
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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HttpSession hs = request.getSession();
		
		int quesType = Integer.parseInt((String) request.getParameter("quesType"));
		request.getSession().setAttribute("quesType", quesType);
				
		int type = quesType % 2;
		
		Question.init();
		String instructions = Question.INSTRUCTIONS.get(quesType);
		request.getSession().setAttribute("instructions", instructions);
		
		String nextPage = "";
		switch(quesType){
		case Question.SINGLE_STR_ANS: 
		case Question.FIB:
		case Question.MULTI_CHOICE_C:
		case Question.MULTI_CHOICE_R:
			nextPage = "AddQuesTextNormal.jsp";
			break;
			
		case Question.PICTURE_RESPONSE:
			nextPage = "AddQuesTextPicRes.jsp";
			break;
			
		case Question.MULTI_STR_ANS:
			nextPage = "AddQuesTextMultiStrResp.jsp";
			break;

		}
		RequestDispatcher rd = request.getRequestDispatcher(nextPage);
        rd.forward(request, response);
	}

}
