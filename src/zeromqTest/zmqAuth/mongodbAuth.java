package zeromqTest.zmqAuth;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;

public class mongodbAuth {
	
	static jacksonMapperAuth map = new jacksonMapperAuth();
	private String readers = "readers";
	private String editors = "editors";
	
	public static void findStudiesList(Mongo mongodb, DB db, DBCollection collection, BasicDBObject findId, BasicDBObject findField, String listType) throws JsonProcessingException, IOException{	
		DBCursor cursor = collection.find(findId, findField);//findId = specific Id, findField = fields to be returned
		while(cursor.hasNext()) {
		   DBObject o = cursor.next();
		   map.setMapperList(o.toString(), "_id", listType);
		}
	}
	
	public mongodbAuth(String database, String Collection, String test) throws IOException{
		Mongo mongodb = new Mongo("10.137.101.80", 27017);//-->10.137.101.80(location of mongodb) , port(27017) by default
		DB db = mongodb.getDB(database);//which database
		DBCollection collection = db.getCollection(Collection);//what collection to read
			
		//fields to be returned(studies' _id)
		BasicDBObject fields = new BasicDBObject("_id", true);
		//to find a specific id
		BasicDBObject _idReaders = new BasicDBObject("customAttributes.readers._id", test);
		BasicDBObject _idEditors = new BasicDBObject("customAttributes.editors._id", test);
				
		findStudiesList(mongodb, db, collection, _idReaders, fields, readers);//readers - read permission
		findStudiesList(mongodb, db, collection, _idEditors, fields, editors);	//editors - write permission
		
		mongodb.close();
	}
}

