package com.mrb.pkg;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.DBCollection;


/**
 * Servlet implementation class APISearch
 * This class gets the parameter from the user and makes the appropriate method calls
 * to find apis
 */
public class APISearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public APISearch() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// connecting to the database
		QueryAPI cli = new QueryAPI();
        DBCollection apis_collection = cli.connectToDB("apis");
		
        // list that contains the resultant names of the web services 
		ArrayList<String> output = new ArrayList<String>();
		
		// creating an object of printwriter to send the response to the server
		PrintWriter writer = response.getWriter();
		
		// get the selected parameter from the user
		String apiradio = request.getParameter("apiradio");
		
		// formatting the output
		writer.print("<!DOCTYPE html>");
        writer.print("<html><head>");
        writer.print("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
        writer.print("<title>Results</title></head>");
        writer.print("<body>");
        writer.print("<h1><center>Results</center></h1>");
        
        // using switch to call the appropriate methods
		switch(apiradio){
		// find apis by updated year
		case "updated":
			String updated = request.getParameter("updated_text");
			output = cli.searchByUpdatedYear(apis_collection, updated);
			if(output.size() == 0)
				writer.println("<p> No results found related to your query </p>");
			for(int i = 0; i < output.size(); i++){
				writer.print("<p><center>" +output.get(i) + "</center></p>");
			}
			break;
			
		// find apis by protocols
		case "protocol":
			String protocol = request.getParameter("protocol_text");
			output = cli.searchByProtocol(apis_collection, protocol);
			if(output.size() == 0)
				writer.println("<p> No results found related to your query </p>");
			for(int i = 0; i < output.size(); i++){
				writer.print("<p><center>" +output.get(i) + "</center></p>");
			}
			break;
			
		// find apis by category
		case "category":
			String category = request.getParameter("Category_text");
			output = cli.searchByCategory(apis_collection, category);
			if(output.size() == 0)
				writer.println("<p> No results found related to your query </p>");
			for(int i = 0; i < output.size(); i++){
				writer.print("<p><center>" +output.get(i) + "</center></p>");
			}
			break;
			
		// find apis by rating
		case "rating":
			String compare = request.getParameter("compare");
			String rating = request.getParameter("rating_text");
			output = cli.searchByRating(apis_collection, rating, compare);
			if(output.size() == 0)
				writer.println("<p> No results found related to your query </p>");
			for(int i = 0; i < output.size(); i++){
				writer.print("<p><center>" +output.get(i) + "</center></p>");
			}
			break;
			
		// find apis by tags
		case "tags":
			String tags = request.getParameter("tags_text");
			output = cli.searchByTags(apis_collection, tags);
			if(output.size() == 0)
				writer.println("<p> No results found related to your query </p>");
			for(int i = 0; i < output.size(); i++){
				writer.print("<p><center>" +output.get(i) + "</center></p>");
			}
			break;
			
		// find apis by keywords
		case "keywords":
			String keywords = request.getParameter("keywords_text");
			output = cli.searchByKeywords(apis_collection, keywords);
			if(output.size() == 0)
				writer.println("<p> No results found related to your query </p>");
			for(int i = 0; i < output.size(); i++){
				writer.print("<p><center>" +output.get(i) + "</center></p>");
			}
			break;
		}
		writer.print("</body>");
        writer.print("</html>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
