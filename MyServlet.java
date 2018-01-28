package com.mrb.pkg;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MyServlet
 * First entry point servlet
 * Depending on the request made by the user this servlet redirects the requests
 * to corresponding html pages
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MyServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		/*String searchFor = request.getParameter("searchFor");
		PrintWriter out = response.getWriter();
		out.print("Finally..." + searchFor);*/
		//response.sendRedirect("WebContent/api.html");
		
		// get the option selected by the user
		String searchFor = request.getParameter("searchFor");
		//System.out.println(searchFor);
		
		// if the option is api send the request to api.html
		if(searchFor.equals("api")){
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/api.html");
			dispatcher.forward(request, response);
		}
		// else send the request to mashup.html
		else
		{
			RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/mashup.html");
			dispatcher.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
