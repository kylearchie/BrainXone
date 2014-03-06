package brainxone;

import java.io.IOException;
import java.sql.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Servlet implementation class CreateNewAccountServlet
 */
@WebServlet("/CreateNewAccountServlet")
public class CreateNewAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateNewAccountServlet() {
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
		ServletContext servletContext = getServletContext();
		Statement stmt = (Statement) servletContext.getAttribute("Statement");
		String account = request.getParameter("username");
		if (!User.userExist(account, stmt)) {
			String password = request.getParameter("password");
			User user = new User(stmt, account, password);
			RequestDispatcher dispatch = request.getRequestDispatcher("welcome.jsp");
			dispatch.forward(request, response);
		} else {
			RequestDispatcher dispatch = request.getRequestDispatcher("create_account_failed.jsp");
			dispatch.forward(request, response);
		}
	}

}

