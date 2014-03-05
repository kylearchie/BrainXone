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
		String singleStrAnsTxt = (String) request.getParameter("singleStrAnsTxt");
		StringResponse ques = (StringResponse) hs.getAttribute("question");
		HashMap<String, Integer> answerKeys = new HashMap<String, Integer>();
		answerKeys.put(singleStrAnsTxt, 1);
		ques.setAnswer(answerKeys);
		
		RequestDispatcher rd = request.getRequestDispatcher("MoreOrSubmit.jsp");
        rd.forward(request, response);
	}

}
