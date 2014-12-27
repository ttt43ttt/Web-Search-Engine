package com.wilson.servelet;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wilson.search.KeywordSearcher;
import com.wilson.search.SearchResult;

/**
 * Servlet implementation class SearchServelet
 */
@WebServlet("/s")
public class SearchServelet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchServelet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// ��ȡ����
		String keyword = new String(request.getParameter("kw").getBytes("ISO-8859-1"), "UTF-8");
		String rootDir = request.getServletContext().getRealPath("WEB-INF/data");

		if (keyword != null) {
			// ��ѯ
			KeywordSearcher searcher = new KeywordSearcher(rootDir);
			List<SearchResult> result = searcher.search(keyword);
			// ����ҳ������
			request.setAttribute("kw", keyword);
			request.setAttribute("result", result);
		}

		ServletContext sc = getServletContext();
		// ����ҳ��
		RequestDispatcher dispatcher = sc.getRequestDispatcher("/index.jsp");
		dispatcher.forward(request, response);
	}

}
