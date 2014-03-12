package backend;
import brainxone.*;

import java.io.IOException;
import java.util.ArrayList;
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
		String userName = (String) hs.getAttribute("currentUser");
		int isPracticeMode = (Integer)hs.getAttribute("isPracticeMode");

		if(isPracticeMode == 1){
			String quesID = Integer.toString((Integer) hs.getAttribute("quesID"));
			Question ques = (Question) hs.getAttribute(quesID);
			HashMap<Question, Integer> curScore = (HashMap<Question, Integer>) request.getSession().getAttribute("curScore");
			int type = ques.getType();
			int points = 0;
			int max = 0;

			switch( type ) {
			case Question.SINGLE_STR_ANS:
			case Question.FIB:
			case Question.PICTURE_RESPONSE:
				points = practiceSingleStrResponse(request, (StringResponse) ques);
				max = ques.getMaxPoints();
				if(points == max){
					int score = curScore.get(ques);
					curScore.put(ques, (score + 1));
				}
				break;
			case Question.MULTI_STR_ANS:
				points = practiceMultiStrResponse(request, (StringResponse) ques);
				max = ques.getMaxPoints();
				if(points == max){
					int score = curScore.get(ques);
					curScore.put(ques, (score + 1));
				}
				break;
			case Question.MULTI_CHOICE_C:
			case Question.MULTI_CHOICE_R:
				points = practiceMultiChoice(request, (MultiChoice) ques);
				max = ques.getMaxPoints();
				if(points == max){
					int score = curScore.get(ques);
					curScore.put(ques, (score + 1));
				}
				break;		
			}
			
			request.getSession().setAttribute("curScore", curScore);	
			RequestDispatcher rd = request.getRequestDispatcher("PracticeMode.jsp");
	        rd.forward(request, response);
		} else{
			int scoreTotal = 0;
			String quizIDString = (String) hs.getAttribute("quizID");
			int quizID = 0;
			if( quizIDString != null) quizID = Integer.parseInt(quizIDString);
			Quiz currQuiz = Quiz.getQuizUsingID(quizID);
			
			int counter = 0;
			while( true ) {
				counter++;
				String q = "question" + counter;
				String quesID = request.getParameter(q);
	//			System.out.println("ID: " + quesID);
				if( quesID == null ) break;
				int id = Integer.parseInt(quesID);
				int type = Integer.parseInt(request.getParameter(q + "type"));
	//			System.out.println("Type: " + type);
				switch( type ) {
				case Question.SINGLE_STR_ANS:
				case Question.FIB:
				case Question.PICTURE_RESPONSE:
				case Question.MULTI_STR_ANS:
					scoreTotal += solveMultiString( request, q, id );
					break;
				case Question.MULTI_CHOICE_C:
				case Question.MULTI_CHOICE_R:
					scoreTotal += solveMultiChoice( request, q, id, type );			
				}
			}
			System.out.println(scoreTotal);
			System.out.println(request.getParameter("elapsedTime"));

			long taken = Long.parseLong(request.getParameter("elapsedTime"));
			ServletContext servletContext = getServletContext();
			Statement stmt = (Statement) servletContext.getAttribute("Statement");
			TakenEvent takenEvent = new TakenEvent(userName, quizID, scoreTotal, taken, stmt);
			if (!TakenEvent.checkGreatest(userName, stmt) && TakenEvent.CheckQualifiedGreatest(userName, quizID, stmt)) {
				TakenEvent.UpdateGreatestAchievements(userName, stmt);
			}
			
			if( currQuiz.isOnePage() ) {
				RequestDispatcher dispatch = request.getRequestDispatcher("quizCompleted.jsp");
				dispatch.forward(request, response);
			} else {
				int qNum = (Integer) hs.getAttribute("questionNumber");
				int currScore = (Integer) hs.getAttribute("currentScore");
				int currTime = (Integer) hs.getAttribute("currentTime");
				
				hs.setAttribute("questionNumber", qNum+1);
				hs.setAttribute("currentScore", currScore + scoreTotal);
				hs.setAttribute("currentTime", currTime + Integer.parseInt(request.getParameter("elapsedTime")));
				
				if( qNum == Quiz.getNumQuestionsUsingID(quizID) ) {
					RequestDispatcher dispatch = request.getRequestDispatcher("quizCompleted.jsp");
					dispatch.forward(request, response);
				} else {
					RequestDispatcher dispatch = request.getRequestDispatcher("showQuiz.jsp");
					dispatch.forward(request, response);
				}

			}
		}
	}

// G: This doesn't feel very OOP to me.
// G: This method no longer necessary

//	private int solveSingleString(HttpServletRequest request, String q, int quesID) {
//		String answer = request.getParameter(q + "answer");
//		HashMap<String, Integer> mapB = new HashMap<String, Integer>();
//		mapB.put(answer, 1);
//		StringResponse sr = new StringResponse( true, quesID, Question.STRING_RESPONSE, null); //Create a temporary question we can use to check the answer.
//		sr.checkAnswer(quesID, mapB); // G: This is hella redundant.
//		return sr.getPoints();
//	}

	/* G: BUG -- What happens when answers to different questions are the same? */
	private int solveMultiString(HttpServletRequest request, String q, int quesID) {
		ArrayList<String> answers = new ArrayList<String>();
		int counter = 0;
		while(true) {
			counter++;
			String answer = request.getParameter(q + "answer" + counter);
			if(answer == null) break;
			answers.add(answer);
		}
		StringResponse sr = new StringResponse(true, quesID, Question.MULTI_STR_ANS, null);
		int points = sr.checkAnswer(answers);
		System.out.println(points + " for SR");
		return points;
	}

	private int solveMultiChoice(HttpServletRequest request, String q, int quesID, int type) {
		HashSet<String> selectedOptions = new HashSet<String>();
		int counter = 0;
		while(true) {
			counter++;
			String answer = request.getParameter(q + "option" + counter);
			if(answer == null) break;
			String valid;
			if(type == Question.MULTI_CHOICE_C) valid = request.getParameter(q + "valid" + counter);
			else valid = request.getParameter(q + "valid");
			if(valid != null && Integer.parseInt(valid) == counter) {
				selectedOptions.add(answer);
			}
		}
		MultiChoice mc = new MultiChoice(true, quesID, Question.MULTI_CHOICE, null);
		
		int points = mc.checkAnswer(selectedOptions);
		System.out.println(points + " for MC");
		return points;
	}
	
	private int practiceMultiStrResponse(HttpServletRequest request, StringResponse ques){
		ArrayList<String> answers = new ArrayList<String>();
		for(int i = 0; i < 3; i ++)
		{
			String answer = (String) request.getParameter("multiStringAns" + (i + 1));
			answers.add(answer);
		}
		return ques.checkAnswer(answers);
	}
	
	private int practiceSingleStrResponse(HttpServletRequest request, StringResponse ques){
		ArrayList<String> answers = new ArrayList<String>();
		String answer = (String) request.getParameter("answer");
		answers.add(answer);
		return ques.checkAnswer(answers);
	}
	
	private int practiceMultiChoice(HttpServletRequest request, MultiChoice ques){
		HttpSession hs = request.getSession();
		HashSet<String> selectedOptions = new HashSet<String>();
		for(int i = 0; i < 2; i ++)
		{
			String option = (String) request.getParameter("options" + (i + 1));
			String isValid = (String)(request.getParameter("isValid" + (i + 1)));
			if(isValid.equals("1"))
				selectedOptions.add(option);
		}
		return ques.checkAnswer(selectedOptions);
	}
	
	
	

}
