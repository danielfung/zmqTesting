package zeromqTest.zmqIRB;

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
	
	@SuppressWarnings("unused")
	public void findEntity(String json, String inputActivityList, String inputIrbList) throws JsonProcessingException, IOException{	
		if(inputActivityList == "yes" && inputIrbList == "no"){			
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = null;	
			rootNode = mapper.readTree(json);			
			JsonNode parentStudy = null;
			for(final JsonNode element: rootNode){
				parentStudy = rootNode.findPath("_id");
				_rrserver.addElementIrbParentStudy_id(parentStudy.asText());
			}			
		}
		
		if(inputIrbList == "yes" && inputActivityList == "no"){
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = null;	
			rootNode = mapper.readTree(json);			
			JsonNode _id = rootNode.findPath("_id");
			_rrserver.addElementsIrb(_id.asText());			
		}
		
		if(inputIrbList == "no" && inputActivityList == "no"){
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = null;	
			rootNode = mapper.readTree(json);
			JsonNode id = rootNode.findPath("id");
			_rrserver.addElementIrbRelatedStudies(id.asText());
		}	
	}
	
	public mongodbIRB(String database, String Collection, String test, String id, String inputActivityList, String inputIrbList) throws JsonProcessingException, IOException{	
		String testId = null;
		//Mongo mongodb = new Mongo();//local machine's mongodb
		Mongo mongodb = new Mongo("10.137.101.80", 27017);
		DB db = mongodb.getDB(database);//which database
		DBCollection collection = db.getCollection(Collection);//what collection to read

				BasicDBObject _idField = new BasicDBObject(id, test);
				//do a query to find a specific id
				DBCursor cursor = collection.find(_idField);
				while(cursor.hasNext()){
					//System.out.println(cursor.next());
					DBObject o = cursor.next();
					testId = o.toString();
					if(testId != null){
						findEntity(testId, inputActivityList, inputIrbList);//testing						
					}
				}
				mongodb.close();
	}
}
