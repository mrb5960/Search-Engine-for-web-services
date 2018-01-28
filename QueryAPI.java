package com.mrb.pkg;

import com.mongodb.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrb5960 on 4/23/2017.
 * This class contains the logic of retrieval of documents(apis) from the collections
 * based on the user requests
 */
public class QueryAPI {

    void print(String str){
        System.out.println(str);
    }

    public QueryAPI(){
    	
    }
    
    /*public static void main(String args[]){
        QueryAPI cli = new QueryAPI();
        DBCollection apis_collection = cli.connectToDB("apis");
        //cli.searchByCategory(apis_collection, "science");
        cli.searchByUpdatedYear(apis_collection, "2012");
        //cli.searchByProtocol(apis_collection, "REST");
        //cli.searchByRating(apis_collection, "4.4");
        //String[] keywords = {"proteome"};
        //cli.combinedSearch(apis_collection, keywords);
    }*/

    // search api by updated year
    public ArrayList<String> searchByUpdatedYear(DBCollection collection, String year){
    	// list that contains the apis obtained as result of the query
    	ArrayList<String> out = new ArrayList<String>();
    	// objects that contains the conditions
        BasicDBObject where_clause = new BasicDBObject();
        // search for year using regular expressions
        where_clause.put("updated", new BasicDBObject("$regex", year).append("$options", "i"));
        BasicDBObject attr = new BasicDBObject();
        // retrieve the name and updated values
        attr.put("name", 1);
        attr.put("updated", 1);
        // execute the query
        DBCursor cursor = collection.find(where_clause, attr);
        int count = 0;
        // store the result in the result
        while(cursor.hasNext()) {
            BasicDBObject obj = (BasicDBObject)cursor.next();
            //System.out.println(++count + " " + obj.getString("name") + " " + obj.getString("updated"));
            out.add(obj.getString("name"));
        }
        // return the list
        return out;
    }

    // search api by protocol
    public ArrayList<String> searchByProtocol(DBCollection collection, String protocol_name){
    	ArrayList<String> out = new ArrayList<String>();
        BasicDBObject where_clause = new BasicDBObject();
        // use regex and ignore case
        where_clause.put("protocols", new BasicDBObject("$regex", protocol_name).append("$options", "i"));
        BasicDBObject attr = new BasicDBObject();
        attr.put("name", 1);
        attr.put("protocols", 1);
        DBCursor cursor = collection.find(where_clause, attr);
        while(cursor.hasNext()) {
            BasicDBObject obj = (BasicDBObject)cursor.next();
            //System.out.println(obj.getString("name") + " " + obj.getString("protocols"));
            out.add(obj.getString("name"));
        }
        return out;
    }

    // search apis by category
    public ArrayList<String> searchByCategory(DBCollection collection, String category_name){
    	ArrayList<String> out = new ArrayList<String>();
        BasicDBObject where_clause = new BasicDBObject();
        where_clause.put("category", new BasicDBObject("$regex", category_name).append("$options", "i"));
        BasicDBObject attr = new BasicDBObject();
        attr.put("name", 1);
        attr.put("category", 1);
        DBCursor cursor = collection.find(where_clause, attr);
        while(cursor.hasNext()) {
            BasicDBObject obj = (BasicDBObject)cursor.next();
            //System.out.println(obj.getString("name") + " " + obj.getString("category"));
            out.add(obj.getString("name"));
        }
        return out;
    }

    // search api by rating
    public ArrayList<String> searchByRating(DBCollection collection, String rating, String compare){
    	ArrayList<String> out = new ArrayList<String>();
        BasicDBObject where_clause = new BasicDBObject();
        // query for greater than
        if(compare.equals("gt"))
        	// converting the value entered by the user from string to float for comparison
        	where_clause.put("rating", new BasicDBObject("$gt", Float.parseFloat(rating)));
        // query for less than
        else
        	where_clause.put("rating", new BasicDBObject("$lt", Float.parseFloat(rating)));
        BasicDBObject attr = new BasicDBObject();
        attr.put("name", 1);
        attr.put("rating", 1);
        DBCursor cursor = collection.find(where_clause, attr);
        while(cursor.hasNext()) {
            BasicDBObject obj = (BasicDBObject)cursor.next();
            System.out.println(obj.getString("name") + " " + obj.getString("rating"));
            out.add(obj.getString("name"));
        }
        return out;
    }
    
    // search apis by tags
    public ArrayList<String> searchByTags(DBCollection collection, String tags){
        ArrayList<String> out = new ArrayList<String>();
        List<BasicDBObject> parameters = new ArrayList<BasicDBObject>();
        BasicDBObject where_clause = new BasicDBObject();
        // split tags using ',' a delimiter
        String[] words = tags.split(",");
        // add each word to a list of BasicDBObject so that it can be searched in tags
        for(int i = 0; i < words.length; i++){
            parameters.add(new BasicDBObject("tags", words[i]));
        }
        // using and operator, will return the name if all words are present in the tags
        where_clause.put("$and", parameters);
        //print(where_clause.toString());
        DBCursor cursor = collection.find(where_clause);
        while(cursor.hasNext()) {
            BasicDBObject obj = (BasicDBObject)cursor.next();
            //System.out.println(obj.getString("name") + " " + obj.getString("rating"));
            out.add(obj.getString("name"));
        }
        return out;
    }

    // search apis by keywords
    public ArrayList<String> searchByKeywords(DBCollection collection, String keywords){
        ArrayList<String> out = new ArrayList<String>();
        List<BasicDBObject> parameters = new ArrayList<BasicDBObject>();
        BasicDBObject where_clause = new BasicDBObject();
        String[] words = keywords.split(",");
        // adding combination of words and attributes in which they are to be found to the list
        // regex is used and case is ignored
        for(int i = 0; i < words.length; i++){
        	//print(words[i]);
            parameters.add(new BasicDBObject("summary", new BasicDBObject("$regex", words[i]).append("$options", "i")));
            parameters.add(new BasicDBObject("description", new BasicDBObject("$regex", words[i]).append("$options", "i")));
            parameters.add(new BasicDBObject("title", new BasicDBObject("$regex", words[i]).append("$options", "i")));
        }
        // returns the names if words are present in summary, description and title of the api
        where_clause.put("$and", parameters);
        //print(where_clause.toString());
        DBCursor cursor = collection.find(where_clause);
        while(cursor.hasNext()) {
            BasicDBObject obj = (BasicDBObject)cursor.next();
            //System.out.println(obj.getString("name") + " " + obj.getString("rating"));
            out.add(obj.getString("name"));
        }
        return out;
    }

    // method that connects to the database abd the required collection
    @SuppressWarnings({ "resource", "deprecation" })
	DBCollection connectToDB(String collection_name){
        try {
            MongoClient client = new MongoClient("localhost", 27017);
            DB db = client.getDB("webservices");
            System.out.println("Connected to database");
            DBCollection collection = db.getCollection(collection_name);
            /*DBCursor cursor = collection.find();
            while (cursor.hasNext()) {
                collection.remove(cursor.next());
            }*/
            //System.out.println(collection);
            return collection;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    
    void closeConnection(){
    	
    }
}
