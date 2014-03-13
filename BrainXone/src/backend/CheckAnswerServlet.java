package backend;
import brainxone.*;

import java.io.IOException;
import java.util.ArrayList;
import java.sql.Statement;
import java.util.Collection;
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
		ServletContext servletContext = getServletContext();
		Statement stmt = (Statement) servletContext.getAttribute("Statement");
		String userName = (String) hs.getAttribute("currentUser");

		boolean isPracticeMode = (Boolean) hs.getAttribute("isPracticeMode");
		HashMap<Integer, Integer> usedQuestions = (HashMap<Integer, Integer>) hs.getAttribute("usedQuestions");
		
		//if(isPracticeMode){
			//String quesID = Integer.toString((Integer) hs.getAttribute("quesID"));
			//Question ques = (Question) hs.getAttribute(quesID);
			
			//int type = ques.getType();
			//int points = 0;
			//int max = 0;

//			switch( type ) {
//			case Question.SINGLE_STR_ANS:
//			case Question.FIB:
//			case Question.PICTURE_RESPONSE:
//				points = practiceSingleStrResponse(request, (StringResponse) ques, stmt);
//				max = ques.getMaxPoints(stmt);
//				if(points == max){
//					int score = curScore.get(ques);
//					curScore.put(ques, (score + 1));
//				}
//				break;
//			case Question.MULTI_STR_ANS:
//				points = practiceMultiStrResponse(request, (StringResponse) ques, stmt);
//				max = ques.getMaxPoints(stmt);
//				if(points == max){
//					int score = curScore.get(ques);
//					curScore.put(ques, (score + 1));
//				}
//				break;
//			case Question.MULTI_CHOICE_C:
//			case Question.MULTI_CHOICE_R:
//				points = practiceMultiChoice(request, (MultiChoice) ques, stmt);
//				max = ques.getMaxPoints(stmt);
//				if(points == max){
//					int score = curScore.get(ques);
//					curScore.put(ques, (score + 1));
//				}
//				break;		
//			}
//			
//			request.getSession().setAttribute("curScore", curScore);	
//			RequestDispatcher rd = request.getRequestDispatcher("PracticeMode.jsp");
//	        rd.forward(request, response);
		//} else{
			int scoreTotal = 0;
			Integer quizIDString = (Integer) hs.getAttribute("quizID");
			int quizID = 0;
			if( quizIDString != null) quizID = quizIDString;
			Quiz currQuiz = Quiz.getQuizUsingID(quizID, stmt);
			
			int counter = 0;
			while( true ) {
				counter++;
				String q = "question" + counter;
				String quesID = request.getParameter(q);
				System.out.println("ID: " + quesID);
				if( quesID == null ) break;
				int id = Integer.parseInt(quesID);
				int type = Integer.parseInt(request.getParameter(q + "type"));
				System.out.println("Type: " + type);
				int thisQuesPoints = 0;
				switch( type ) {
				case Question.SINGLE_STR_ANS:
				case Question.FIB:
				case Question.PICTURE_RESPONSE:
				case Question.MULTI_STR_ANS:
					thisQuesPoints = solveMultiString( request, q, id , stmt);
					break;
				case Question.MULTI_CHOICE_C:
				case Question.MULTI_CHOICE_R:
					thisQuesPoints = solveMultiChoice( request, q, id, type , stmt);			
				}
				if(isPracticeMode){
					Question justTaken = new Question(true, id, type, null);
					int maxPoints = justTaken.getMaxPoints(stmt);
					System.out.println("Max Points: " + maxPoints);
					if(thisQuesPoints >= maxPoints) {
						usedQuestions.put(id, usedQuestions.get(id) - 1);
					}
				} else {
					scoreTotal += thisQuesPoints;
				}
			}
			System.out.println(scoreTotal);
			System.out.println(request.getParameter("elapsedTime"));

			if( usedQuestions != null ) {
				hs.setAttribute("usedQuestions", usedQuestions);
				System.out.println("used: " + usedQuestions.toString());
			}
			
			long taken = Long.parseLong(request.getParameter("elapsedTime"));
			TakenEvent takenEvent = new TakenEvent(userName, quizID, scoreTotal, taken, stmt);
			if (!TakenEvent.checkGreatest(userName, stmt) && TakenEvent.CheckQualifiedGreatest(userName, quizID, stmt)) {
				TakenEvent.UpdateGreatestAchievements(userName, stmt);
			}
			if(isPracticeMode && allZeroes(usedQuestions)) {
					hs.setAttribute("isPracticeMode", new Boolean(false));
					RequestDispatcher dispatch = request.getRequestDispatcher("QuizSummary.jsp?id=" + quizID);
					dispatch.forward(request, response);
					return;
			}
			if( currQuiz.isOnePage() ) {
				 if(isPracticeMode) {
					RequestDispatcher dispatch = request.getRequestDispatcher("ShowQuiz.jsp?id=" + quizID);
					dispatch.forward(request, response);
					return;
				}
				hs.setAttribute("currentScore", scoreTotal);
				hs.setAttribute("currentTime", taken);
				RequestDispatcher dispatch = request.getRequestDispatcher("quizCompleted.jsp");
				dispatch.forward(request, response);
				return;
			} else {
				int qNum = (Integer) hs.getAttribute("questionNumber");
				int currScore = (Integer) hs.getAttribute("currentScore");
				long currTime = (Long) hs.getAttribute("currentTime");
				
				hs.setAttribute("questionNumber", qNum+1);
				hs.setAttribute("currentScore", currScore + scoreTotal);
				hs.setAttribute("currentTime", currTime + taken);
				
				if(isPracticeMode) {
					if( qNum == Quiz.getNumQuestionsUsingID(quizID, stmt) ) qNum = 0;
					RequestDispatcher dispatch = request.getRequestDispatcher("ShowQuiz.jsp?id=" + quizID);
					dispatch.forward(request, response);
					return;
				}
				
				if( qNum == Quiz.getNumQuestionsUsingID(quizID, stmt) ) {
					RequestDispatcher dispatch = request.getRequestDispatcher("quizCompleted.jsp");
					dispatch.forward(request, response);
					return;
				} else {
					RequestDispatcher dispatch = request.getRequestDispatcher("ShowQuiz.jsp?id=" + quizID);
					dispatch.forward(request, response);
					return;
				}

			}
//		}
	}

	private boolean allZeroes( HashMap<Integer, Integer> map ) {
		Collection<Integer> set = map.values();
		for(Integer item : set){
			if(item != 0) return false;
		}
		return true;
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
	private int solveMultiString(HttpServletRequest request, String q, int quesID, Statement stmt) {
		ArrayList<String> answers = new ArrayList<String>();
		int counter = 0;
		while(true) {
			counter++;
			String answer = request.getParameter(q + "answer" + counter);
			if(answer == null) break;
			answers.add(answer);
		}
		StringResponse sr = new StringResponse(true, quesID, Question.MULTI_STR_ANS, null);
		int points = sr.checkAnswer(answers, stmt);
		System.out.println(points + " for SR");
		return points;
	}

	private int solveMultiChoice(HttpServletRequest request, String q, int quesID, int type, Statement stmt) {
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
		
		int points = mc.checkAnswer(selectedOptions, stmt);
		System.out.println(points + " for MC");
		return points;
	}
	
//	private int practiceMultiStrResponse(HttpServletRequest request, StringResponse ques, Statement stmt){
//		ArrayList<String> answers = new ArrayList<String>();
//		for(int i = 0; i < 3; i ++)
//		{
//			String answer = (String) request.getParameter("multiStringAns" + (i + 1));
//			answers.add(answer);
//		}
//		return ques.checkAnswer(answers, stmt);
//	}
//	
//	private int practiceSingleStrResponse(HttpServletRequest request, StringResponse ques, Statement stmt){
//		ArrayList<String> answers = new ArrayList<String>();
//		String answer = (String) request.getParameter("answer");
//		answers.add(answer);
//		return ques.checkAnswer(answers, stmt);
//	}
//	
//	private int practiceMultiChoice(HttpServletRequest request, MultiChoice ques, Statement stmt){
//		HttpSession hs = request.getSession();
//		HashSet<String> selectedOptions = new HashSet<String>();
//		for(int i = 0; i < 2; i ++)
//		{
//			String option = (String) request.getParameter("options" + (i + 1));
//			String isValid = (String)(request.getParameter("isValid" + (i + 1)));
//			if(isValid.equals("1"))
//				selectedOptions.add(option);
//		}
//		return ques.checkAnswer(selectedOptions, stmt);
//	}
	
	
	

}
