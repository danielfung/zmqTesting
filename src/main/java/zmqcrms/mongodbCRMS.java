package main.java.zmqcrms;

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

public class mongodbCRMS {	
	rrserverCRMS _activityFinder = new rrserverCRMS();
	
	/*
	 * Given a json record, find all fields and extract it. Once extracted add to arraylist
	 * 
	 * @param json an json string
	 * @param inputActivityList decide whether to place into inputActivityList(yes or no)
	 * @param inputCrmsList      decide whether to place into inputIrbList(yes or no)
	 */
	@SuppressWarnings("unused")
	public void findEntity(String json, String inputWhichList) throws JsonProcessingException, IOException{		
		if(inputWhichList == "crmsCustomAttribute"){
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = null;	
			rootNode = mapper.readTree(json);
			
			JsonNode activities = null;
			for(final JsonNode element: rootNode){
				activities = rootNode.findPath("customAttributes");
				_activityFinder.addElementsActivity(activities.asText());
			}
		}
		
		if(inputWhichList == "arms"){
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = null;	
			rootNode = mapper.readTree(json);		
			JsonNode _arms = null;
			for(final JsonNode element: rootNode){
				_arms = rootNode.findPath("arms");
			}
			
			if(_arms.size() > 0){
				for(int j = 0; j<_arms.size(); j++){		
					_activityFinder.addElementArm(_arms.get(j).asText());
				}
			}
			
			JsonNode _budgets = null;
			for(final JsonNode element: rootNode){
				_budgets = rootNode.findPath("budgets");
			}
			
			if(_budgets.size() > 0){
				for(int j = 0; j<_budgets.size(); j++){		
					_activityFinder.addElementBudget(_budgets.get(j).asText());
				}
			}		
		}
		
		if(inputWhichList == "relatedStudies"){
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = null;	
			rootNode = mapper.readTree(json);
			JsonNode id = rootNode.findPath("id");
			_activityFinder.addElementRelatedStudies(id.asText());
		}	
	}
	
	/*
	 * Establish mongodb connection and query specific records based on 
	 * database, collection and id(field).
	 * 
	 * @param database          which database to access in mongodb
	 * @param Collection        which collection to access in mongodb
	 * @param test	            the field(data) to find in mongodb
	 * @param id                the field to access in mongodb
	 * @param inputActivityList decide if anything should be placed into inputActivitiyList(yes or no)
	 * @param inputCrmsList     decide if anything should be placed into inputCrmsList(yes or no)
	 */
	public mongodbCRMS(String database, String Collection, String test, String id, String inputWhichList) throws JsonProcessingException, IOException{	
		String testId = null;
		Mongo mongodb = new Mongo("10.137.101.80", 27017);
		DB db = mongodb.getDB(database);//which database
		DBCollection collection = db.getCollection(Collection);//what collection to read
		
		BasicDBObject _idField = new BasicDBObject(id, test);
		DBCursor cursor = collection.find(_idField);
			while(cursor.hasNext()){
				DBObject o = cursor.next();
				testId = o.toString();
				if(testId != null){
					findEntity(testId, inputWhichList);//testing					
				}
			}
		mongodb.close();
	}
}
