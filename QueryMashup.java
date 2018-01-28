package com.mrb.pkg;

import com.mongodb.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by mrb5960 on 4/23/2017.
 * This class contains the logic of retrieval of documents(mashups) from the collections
 * based on the user requests
 */
public class QueryMashup {

    void print(String str){
        System.out.println(str);
    }

    public QueryMashup(){
    	
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

    // search by updated year
    public ArrayList<String> searchByUpdatedYear(DBCollection collection, String year){
    	ArrayList<String> out = new ArrayList<String>();
        BasicDBObject where_clause = new BasicDBObject();
        where_clause.put("updated", new BasicDBObject("$regex", year).append("$options", "i"));
        BasicDBObject attr = new BasicDBObject();
        attr.put("name", 1);
        attr.put("updated", 1);
        DBCursor cursor = collection.find(where_clause, attr);
        int count = 0;
        while(cursor.hasNext()) {
            BasicDBObject obj = (BasicDBObject)cursor.next();
            System.out.println(++count + " " + obj.getString("name") + " " + obj.getString("updated"));
            out.add(obj.getString("name"));
        }
        return out;
    }

    // search by apis embedded in the mashup
    public ArrayList<String> searchByApis(DBCollection collection, String apis){
    	ArrayList<String> out = new ArrayList<String>();
        List<BasicDBObject> parameters = new ArrayList<BasicDBObject>();
        BasicDBObject where_clause = new BasicDBObject();
        String[] api_tokens = apis.split(",");
        // checking for the apis in the nested documents
        for(int i = 0; i < api_tokens.length; i++){
            parameters.add(new BasicDBObject("APIs.api", api_tokens[i]));
        }
        // using and operator
        where_clause.put("$and", parameters);
        print(where_clause.toString());
        DBCursor cursor = collection.find(where_clause);
        while(cursor.hasNext()) {
            BasicDBObject obj = (BasicDBObject)cursor.next();
            //System.out.println(obj.getString("name") + " " + obj.getString("rating"));
            out.add(obj.getString("name"));
        }
        return out;
    }

    // search mashups by tags
    public ArrayList<String> searchByTags(DBCollection collection, String tags){
        ArrayList<String> out = new ArrayList<String>();
        List<BasicDBObject> parameters = new ArrayList<BasicDBObject>();
        BasicDBObject where_clause = new BasicDBObject();
        String[] words = tags.split(",");
        for(int i = 0; i < words.length; i++){
            parameters.add(new BasicDBObject("tags", words[i]));
        }
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

    // search mashups by keywords
    public ArrayList<String> searchByKeywords(DBCollection collection, String keywords){
        ArrayList<String> out = new ArrayList<String>();
        List<BasicDBObject> parameters = new ArrayList<BasicDBObject>();
        BasicDBObject where_clause = new BasicDBObject();
        String[] words = keywords.split(",");
        for(int i = 0; i < words.length; i++){
        	//print(words[i]);
            parameters.add(new BasicDBObject("summary", new BasicDBObject("$regex", words[i]).append("$options", "i")));
            parameters.add(new BasicDBObject("description", new BasicDBObject("$regex", words[i]).append("$options", "i")));
            parameters.add(new BasicDBObject("title", new BasicDBObject("$regex", words[i]).append("$options", "i")));
        }
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

    // method that connects to the database and required collection
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
