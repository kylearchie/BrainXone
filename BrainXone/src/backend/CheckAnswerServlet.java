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

		boolean isPracticeMode = false;
		Boolean practiceObj = (Boolean) hs.getAttribute("isPracticeMode");
		if(practiceObj != null) isPracticeMode = practiceObj;
		HashMap<Integer, Integer> usedQuestions = (HashMap<Integer, Integer>) hs.getAttribute("usedQuestions");
		ArrayList<String> givenAnswers = null;
		
		int scoreTotal = 0;
		Integer quizIDString = (Integer) hs.getAttribute("quizID");

		int quizID = 0;
		if( quizIDString != null) quizID = quizIDString;

		Quiz currQuiz = Quiz.getQuizUsingID(quizID, stmt);

		if(currQuiz.hasImmediateCorrection()) givenAnswers = new ArrayList<String>();
		
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
				thisQuesPoints = solveMultiString( request, q, id , stmt, givenAnswers);
				break;
			case Question.MULTI_CHOICE_C:
			case Question.MULTI_CHOICE_R:
				thisQuesPoints = solveMultiChoice( request, q, id, type , stmt, givenAnswers);			
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

		System.out.println("*********" + quizID);

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

			String whereToGo = "";
			if(isPracticeMode) {
				if( qNum == Quiz.getNumQuestionsUsingID(quizID, stmt) ) qNum = 0;		
				whereToGo = "ShowQuiz.jsp?id=" + quizID;
			} else if( currQuiz.hasImmediateCorrection() ){
				hs.setAttribute("givenAnswers", givenAnswers);
				hs.setAttribute("allCorrect", new Boolean(scoreTotal == givenAnswers.size()));
				whereToGo = "ShowFeedback.jsp?id=" + quizID;
			} else if( qNum == Quiz.getNumQuestionsUsingID(quizID, stmt) ) {
				whereToGo = "quizCompleted.jsp";
			} else {
				whereToGo = "ShowQuiz.jsp?id=" + quizID;
			}
			RequestDispatcher dispatch = request.getRequestDispatcher(whereToGo);
			dispatch.forward(request, response);

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

	private int solveMultiString(HttpServletRequest request, String q, int quesID, Statement stmt, ArrayList<String> answers) {
		if(answers == null) answers = new ArrayList<String>();
		int counter = 0;
		while(true) {
			counter++;
			String answer = request.getParameter(q + "answer" + counter);
			System.out.println(answer);
			if(answer == null) break;
			answers.add(answer);
		}
		StringResponse sr = new StringResponse(true, quesID, Question.MULTI_STR_ANS, null);
		int points = sr.checkAnswer(answers, stmt);
		System.out.println(points + " for SR");
		return points;
	}

	private int solveMultiChoice(HttpServletRequest request, String q, int quesID, int type, Statement stmt, ArrayList<String> answers) {
		if(answers == null) answers = new ArrayList<String>();
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
		
		if(answers !=null) for( String s : selectedOptions ) answers.add(s);
		
		int points = mc.checkAnswer(selectedOptions, stmt);
		System.out.println(points + " for MC");
		return points;
	}
}
