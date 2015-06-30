package searchApp;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String searchWord = request.getParameter("searchWord");
		String result = new String(searchWord.getBytes("ISO-8859-1"),"UTF-8");
		SearchManager searchManager = new SearchManager(result);
		List<SearchResultBean> searchResult = null;
		searchResult = searchManager.search();
		RequestDispatcher dispatcher = request
				.getRequestDispatcher("search.jsp");
		request.setAttribute("searchResult", searchResult);
		dispatcher.forward(request, response);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doPost(request, response);
	}
}
