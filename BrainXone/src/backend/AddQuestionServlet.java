package backend;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class AddQuestionServlet
 */
@WebServlet("/AddQuestionServlet")
public class AddQuestionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddQuestionServlet() {
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
		String quesText = (String) request.getParameter("quesText");
		
		int quesType = Integer.parseInt((String) request.getParameter("quesType"));
		int type = quesType % 2;
		
		String instruction = Question.INSTRUCTIONS.get(quesType);
		int isOrdered = Integer.parseInt((String) request.getParameter("isOrdered"));
		Question ques = null;
		
		if(type == Question.STRING_RESPONSE){
			ques = new StringResponse(false, 0, quesType, quesText);
		}else if(type == Question.MULTI_CHOICE){
			ques = new MultiChoice(false, 0, quesType, quesText);
		}
		Quiz q = (Quiz) hs.getAttribute("quiz");
		q.addQuestionToDB(ques, quesType, isOrdered);
		hs.setAttribute("question", ques);
		String nextPage = "";
		switch(quesType){
		case Question.SINGLE_STR_ANS: 
		case Question.FIB:
		case Question.PICTURE_RESPONSE:
			nextPage = "SingleStrAns.jsp";
			break;
		case Question.MULTI_STR_ANS:
			nextPage = "MultiStrAns.jsp";
			break;
		case Question.MULTI_CHOICE_C:
		case Question.MULTI_CHOICE_R:
			nextPage = "MultiChoiceAns.jsp";
			break;

		}
		RequestDispatcher rd = request.getRequestDispatcher(nextPage);
        rd.forward(request, response);
	}

}
