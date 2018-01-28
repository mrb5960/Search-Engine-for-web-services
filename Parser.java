package com.mrb.pkg;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
import com.mongodb.*;
import com.mongodb.util.JSON;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by mrb5960 on 4/20/2017.
 * This class parses the api.txt and mashup.txt and creates json object for each line
 * These json objects are then dumped into mongodb as documents inside respective collections
 */
public class Parser {

    public static void main(String args[]){
    	// store the api keys
        String[] api_keys = {"id", "title", "summary", "rating", "name", "label", "author", "description", "type",
        "downloads", "useCount", "sampleUrl", "downloadUrl", "dateModified", "remoteFeed", "numComments",
        "commentsUrl", "tags", "category", "protocols", "serviceEndpoint", "version", "wsdl", "dataFormats",
        "apiGroups", "example", "clientInstall", "authentication", "ssl", "readOnly", "vendorApiKits",
        "communityApiKits", "blog", "forum", "support", "accountReq", "commercial", "provider", "managedBy",
        "nonCommercial", "dataLicensing", "fees", "limits", "terms", "company", "updated"};

        // store the mashup keys
        String[] mashup_keys = {"id", "title", "summary", "rating", "name", "label", "author", "description", "type",
        "downloads", "useCount", "sampleUrl", "dateModified", "numComments", "commentsUrl", "tags", "APIs", "updated"};

        try{
            Parser parser = new Parser();
            //Gson gson = new GsonBuilder().setPrettyPrinting().create();
            // JSONObject that will be inserted as one document in the database
            JSONObject obj;
            
            // path of the input file
            String path = "C:\\Study\\RIT\\Web Services\\Assignments\\A3\\api.txt";
            // connect to the apis collection
            DBCollection apis_collection = parser.connectToDB("apis");
            // connect to the mashups collection
            DBCollection mashups_collection = parser.connectToDB("mashups");
            // read the api.txt file
            FileReader input = new FileReader(path);
            BufferedReader br = new BufferedReader(input);
            String line = "";
            // for each line get the tokens of string
            while((line = br.readLine())!=null) {
                //System.out.println(line);
            	// tokenize the input string
                obj = parser.getApiTokens(line, api_keys);
                // add json object as a document in the database
                parser.addToDB(obj, apis_collection);
                //System.out.println(gson.toJson(obj));
            }
            br.close();

            path = "C:\\Study\\RIT\\Web Services\\Assignments\\A3\\mashup.txt";
            // read mashup.txt and follow the same procedure as above
            input = new FileReader(path);
            br = new BufferedReader(input);
            while((line = br.readLine())!=null) {
                line = br.readLine();
                //System.out.println(line);
                //gson = new GsonBuilder().setPrettyPrinting().create();
                obj = parser.getMashupTokens(line, mashup_keys);
                parser.addToDB(obj, mashups_collection);
                //System.out.println(gson.toJson(obj));
            }
            br.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    // method to print an array
    void print_array(String[] arr){
        for(int i = 0; i < arr.length; i++){
            System.out.print(arr[i]);
        }
    }


    // method that establishes a connection to the database
    // returns a DBCollection object 
    DBCollection connectToDB(String collection_name){
        try {
            MongoClient client = new MongoClient("localhost", 27017);
            DB db = client.getDB("webservices");
            System.out.println("Connected to database");
            DBCollection collection = db.getCollection(collection_name);
            // to avoid redundant data
            DBCursor cursor = collection.find();
            while (cursor.hasNext()) {
                collection.remove(cursor.next());
            }
            //System.out.println(collection);
            return collection;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    // method that adds a document to the collection
    void addToDB(JSONObject obj, DBCollection collection){
        DBObject dbobj = (DBObject) JSON.parse(obj.toString());
        collection.insert(dbobj);
    }


    // method that tokenizes each line in api.txt and stores them as key value pairs in a json object
    @SuppressWarnings("unchecked")
    JSONObject getApiTokens(String s, String[] keys){
    	// index to keep track of the api_keys
        int index = 0;
        JSONObject obj = new JSONObject();

        // splitting the string by $#$
        String[] tokens = s.split("\\$\\#\\$");
        for(int i = 0; i < tokens.length; i++){
            //System.out.println(i + " " + tokens[i]);
        	// splitting the string by ###
            String[] values = tokens[i].split("\\#\\#\\#");
            if(values.length == 1){
            	// converting rating to float which helps in comparison while querying
            	// if rating value is not empty
            	if(keys[i] == "rating" && tokens[i].length() != 0)
            		obj.put(keys[index], Float.parseFloat(values[0]));
            	else
            		obj.put(keys[index], values[0]);
            	// incrementing index value
                index++;
            }
            else {
            	// adding attributes in a json array and then adding it to an object
                JSONArray attribute_values = new JSONArray();
                for (int j = 0; j < values.length; j++) {
                    attribute_values.add(values[j]);
                }
                obj.put(keys[index],attribute_values);
                // incrementing index value
                index++;
            }
        }
        // return the resultant json object
        return obj;
    }


 // method that tokenizes each line in mashup.txt and stores them as key value pairs in a json object
    @SuppressWarnings("unchecked")
    JSONObject getMashupTokens(String s, String[] keys){
        int index = 0;
        JSONObject obj = new JSONObject();

        // splitting the string by $#$
        String[] tokens = s.split("\\$\\#\\$");
        for(int i = 0; i < tokens.length; i++){
            //System.out.println(i + " " + tokens[i]);
        	// splitting the string by ###
            String[] values = tokens[i].split("\\#\\#\\#");
            //print_array(values);
            if(values.length == 1){
            	// splitting the string by $$$
                String[] component_api = values[0].split("\\$\\$\\$");
                // adding normal key value pairs in the json object
                if(component_api.length == 1) {
                	obj.put(keys[i], values[0]);
                    index++;
                }
                // else add one component service and its url in a new json object
                if(component_api.length == 2){
                    JSONObject comp = new JSONObject();
                    comp.put("api", component_api[0]);
                    comp.put("url", component_api[1]);
                    obj.put(keys[i], comp);
                    index++;
                }
            }
            // if length is more than one
            else {
                JSONArray attribute_values = new JSONArray();
                for (int j = 0; j < values.length; j++) {
                	// splitting the string by $$$
                    String[] component_api = values[j].split("\\$\\$\\$");
                    if(component_api.length > 1) {
                        JSONObject comp = new JSONObject();
                        comp.put("api", component_api[0]);
                        comp.put("url", component_api[1]);
                        attribute_values.add(comp);
                    }
                    else
                        attribute_values.add(values[j]);
                }
                obj.put(keys[i],attribute_values);
                // incrementing the value of index
                index++;
            }
        }
        // return the resultant json object
        return obj;
    }
}