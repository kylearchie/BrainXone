package backend;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class CategorySelectionServlet
 */
@WebServlet("/CategorySelectionServlet")
public class CategorySelectionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CategorySelectionServlet() {
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
		String selectedCategory = request.getParameter("category");
		ServletContext servletContext = getServletContext();
		Statement stmt = (Statement) servletContext.getAttribute("Statement");
		HashMap<Integer, String> quizList = new HashMap<Integer, String>();
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM quiz WHERE category = \"" + selectedCategory + "\";");
			if(rs != null){
				while(rs.next()){
					int quizID = Integer.parseInt(rs.getString(1));
					String description = rs.getString(3);
					quizList.put(quizID, description);
				}
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		hs.setAttribute("quizList", quizList);
		RequestDispatcher rd = request.getRequestDispatcher("QuizList.jsp");
	    rd.forward(request, response);
	}
}
