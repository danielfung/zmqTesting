package zeromqTest.zmqCRMS;

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
	
	@SuppressWarnings("unused")
	public void findEntity(String json, String inputActivityList, String inputIrbList) throws JsonProcessingException, IOException{
		
		if(inputActivityList == "yes" && inputIrbList == "no"){
			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = null;	
			rootNode = mapper.readTree(json);
			
			JsonNode activities = null;
			for(final JsonNode element: rootNode){
				activities = rootNode.findPath("customAttributes");
				_activityFinder.addElementsActivity(activities.asText());
			}

		}
		
		if(inputIrbList == "yes" && inputActivityList == "no"){
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
		
		if(inputIrbList == "no" && inputActivityList == "no"){
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = null;	
			rootNode = mapper.readTree(json);
			JsonNode id = rootNode.findPath("id");
			_activityFinder.addElementRelatedStudies(id.asText());
		}
		
	}
	
	public mongodbCRMS(String database, String Collection, String test, String id, String inputActivityList, String inputIrbList) throws JsonProcessingException, IOException{	
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
						findEntity(testId, inputActivityList, inputIrbList);//testing
						
					}
				}
				mongodb.close();
	}
	
}
