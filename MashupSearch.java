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
 * Servlet implementation class MashupSearch
 * This class gets the parameter from the user and makes the appropriate method calls
 * to find mashups
 */
public class MashupSearch extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MashupSearch() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		// connnecting to the database
		QueryMashup cli = new QueryMashup();
        DBCollection mashups_collection = cli.connectToDB("mashups");
		
        // list that will contain the resultant names of the mashups
		ArrayList<String> output = new ArrayList<String>();
		PrintWriter writer = response.getWriter();
		
		// get the selected parameter from the user
		String mashupradio = request.getParameter("mashupradio");
		
		// formatting the output
		writer.print("<!DOCTYPE html>");
        writer.print("<html><head>");
        writer.print("<meta http-equiv='Content-Type' content='text/html; charset=UTF-8'>");
        writer.print("<title>Results</title></head>");
        writer.print("<body>");
        writer.print("<h1><center>Results</center></h1>");
        
     // using switch to call the appropriate methods
		switch(mashupradio){
		// find mashups by updated year
		case "updated":
			String updated = request.getParameter("updated_text");
			output = cli.searchByUpdatedYear(mashups_collection, updated);
			if(output.size() == 0)
				writer.println("<p> No results found related to your query </p>");
			for(int i = 0; i < output.size(); i++){
				writer.print("<p><center>" +output.get(i) + "</center></p>");
			}
			break;
			
		// find mashups by the embedded apis
		case "apis":
			String apis = request.getParameter("apis_text");
			System.out.println(apis);
			output = cli.searchByApis(mashups_collection, apis);
			if(output.size() == 0)
				writer.println("<p> No results found related to your query </p>");
			for(int i = 0; i < output.size(); i++){
				writer.print("<p><center>" +output.get(i) + "</center></p>");
			}
			break;
			
		// find mashups by tags
		case "tags":
			String tags = request.getParameter("tags_text");
			output = cli.searchByTags(mashups_collection, tags);
			if(output.size() == 0)
				writer.println("<p> No results found related to your query </p>");
			for(int i = 0; i < output.size(); i++){
				writer.print("<p><center>" +output.get(i) + "</center></p>");
			}
			break;
			
		// find mashups by keywords
		case "keywords":
			String keywords = request.getParameter("keywords_text");
			output = cli.searchByKeywords(mashups_collection, keywords);
			if(output.size() == 0)
				writer.println("<p> No results found related to your query </p>");
			for(int i = 0; i < output.size(); i++){
				writer.print("<p><center>" +output.get(i) + "</center></p>");
			}
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
