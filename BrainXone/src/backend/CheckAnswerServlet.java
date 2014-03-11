package backend;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class CheckAnswerServlet
 */
@WebServlet("/CheckAnswerServlet")
public class CheckAnswerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckAnswerServlet() {
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
		String answer = (String) request.getParameter("answer");
		String quesID = Integer.toString((Integer) hs.getAttribute("quesID"));
		Question ques = (Question) hs.getAttribute(quesID);
		HashMap<String, Integer> mapB = new HashMap<String, Integer>();
		mapB.put(answer, 1);
		
		ques.checkAnswer(Integer.parseInt(quesID), mapB);
		int isPracticeMode = (Integer)hs.getAttribute("isPracticeMode");
		if(isPracticeMode == 1){
			HashMap<Question, Integer> curScore = (HashMap<Question, Integer>) request.getSession().getAttribute("curScore");
			System.out.println("ques.getPoints()" + ques.getPoints() + "ques.getMaxPoints()" + ques.getMaxPoints());
			if(ques.getPoints() == 1){
				int score = curScore.get(ques);
				curScore.put(ques, (score + 1));
			}
			request.getSession().setAttribute("curScore", curScore);	
			RequestDispatcher rd = request.getRequestDispatcher("PracticeMode.jsp");
	        rd.forward(request, response);
		}
		System.out.println(ques.getPoints());
		
        
	}

}
