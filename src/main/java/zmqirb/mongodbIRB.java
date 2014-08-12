package main.java.zmqirb;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class mongodbIRB {
	rrserverIRB _rrserver = new rrserverIRB();
	
	/*
	 * Given a json record, find all fields and extract it. Once extracted place into an array list
	 * 
	 * @param json an json string
	 * @param inputActivityList decide whether to place into inputActivityList(yes or no)
	 * @param inputIrbList      decide whether to place into inputIrbList(yes or no)
	 */
	@SuppressWarnings("unused")
	public void findEntity(String json, String inputWhichList) throws JsonProcessingException, IOException{	
		if(inputWhichList == "irbSubmission_id"){			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = null;	
			rootNode = mapper.readTree(json);			
			JsonNode parentStudy = null;
			for(final JsonNode element: rootNode){
				parentStudy = rootNode.findPath("_id");
				_rrserver.addElementIrbParentStudy_id(parentStudy.asText());
			}			
		}
		
		if(inputWhichList == "irbRelatedStudies"){
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = null;	
			rootNode = mapper.readTree(json);			
			JsonNode _id = rootNode.findPath("_id");
			_rrserver.addElementsIrb(_id.asText());			
		}
		
		if(inputWhichList == "relatedStudies"){
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = null;	
			rootNode = mapper.readTree(json);
			JsonNode id = rootNode.findPath("id");
			_rrserver.addElementIrbRelatedStudies(id.asText());
		}	
	}
	
	/*
	 * Establish mongodb connection and query specific records based on 
	 * database, collection and id(field).
	 * 
	 * @param database          which database to access in mongodb
	 * @param collection        which collection to access in mongodb
	 * @param test              the field(data) to find in mongodb
	 * @param id                the field to access in mongodb
	 * @param inputActivityList decide whether to place into inputActivityList(yes or no)
	 * @param inputIrbList      decide whether to place into inputIrbList(yes or no)
	 */
	public mongodbIRB(String database, String collection, String test, String id, String inputWhichList) throws JsonProcessingException, IOException{	
		String testId = null;
		//Mongo mongodb = new Mongo();//local machine's mongodb
		Mongo mongodb = new Mongo("10.137.101.80", 27017);
		DB db = mongodb.getDB(database);//which database
		DBCollection collections = db.getCollection(collection);//what collection to read
		
		BasicDBObject _idField = new BasicDBObject(id, test);
		//do a query to find a specific id
		DBCursor cursor = collections.find(_idField);
		while(cursor.hasNext()){
			//System.out.println(cursor.next());
			DBObject o = cursor.next();
			testId = o.toString();
			if(testId != null){
				findEntity(testId, inputWhichList);//testing						
			}
		}
		mongodb.close();
	}
}
