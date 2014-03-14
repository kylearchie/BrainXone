package backend;

import java.io.IOException;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
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
		ServletContext servletContext = getServletContext();
		Statement stmt = (Statement) servletContext.getAttribute("Statement");
		String quesText = (String) request.getParameter("quesText");
		String quesImgURL = (String) request.getParameter("quesImgURL");
		int quesType = (Integer)request.getSession().getAttribute("quesType");
		int type = quesType % 2;
		String toParse ="";

		if(quesType == 4)
			toParse = "#QTEXT " + quesText + " #IMGURL " + quesImgURL;
		else
			toParse = quesText;

		Question ques = null;

		if(type == Question.STRING_RESPONSE){
			ques = new StringResponse(false, 0, quesType, toParse);
		}else if(type == Question.MULTI_CHOICE){
			ques = new MultiChoice(false, 0, quesType, toParse);
		}

		Quiz q = (Quiz) hs.getAttribute("quiz");
		
		// TAGS TAGS TAGS
		String allTags = (String) request.getSession().getAttribute("tags");
		System.out.println(allTags);
		StringTokenizer st = new StringTokenizer(allTags, ",");
		while (st.hasMoreTokens()) {
			q.addTagsToDB(st.nextToken(), stmt);
		}

		if(quesType == Question.MULTI_STR_ANS) {
			int isOrdered = Integer.parseInt((String) request.getParameter("isOrdered"));
			q.addQuestionToDB(ques, quesType, isOrdered, stmt);
		}
		else
			q.addQuestionToDB(ques, quesType, 0, stmt);

		hs.setAttribute("question", ques);
		String nextPage = "";

		switch(quesType){
		case Question.SINGLE_STR_ANS: 
		case Question.PICTURE_RESPONSE:
		case Question.FIB:
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
