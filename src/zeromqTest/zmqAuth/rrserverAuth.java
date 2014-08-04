package zeromqTest.zmqAuth;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;

import zeromqTest.rrserver;

public class rrserverAuth extends rrserver{
	
	public rrserverAuth(){
		super();
		this.database = "xtudies";
		this.collection = "_research projects";
		this.studiesString = null;
	}
	
	@Override
	public void reset(){
		this.database = "xtudies";
		this.collection = "_research projects";
		this.studiesString = null;
	}
	
	@Override
	public String convertString(String item){
		String resultString = null;
    	Pattern pattern = Pattern.compile("\\[([A-Za-z0-9.]+)\\]");
        Matcher m = pattern.matcher(item);
        if(m.find()){
        	resultString = m.group(1);
        }
        resultString = resultString.toUpperCase();
        resultString = "com.webridge.account.Person:"+resultString;
		return resultString;
	}
	
	@Override
	public String start(String request) throws InterruptedException, IOException{
		reset();
        String string = request;       
        String resultString = convertString(string);     
        System.out.println("Received request: ["+string+"].");        
        
		@SuppressWarnings("unused")
		mongodbAuth mongo = new mongodbAuth(database, collection, resultString);
		jacksonMapperAuth map = new jacksonMapperAuth();
		if(map.getStudiesList().isEmpty()){
			this.studiesString = "Person "+resultString+" has no studies or does not exist";
		}	
		//get a list of all studies with permissions all in 1 string
		//ex){"com.webridge.entity.Entity:F1C77BB104AF054C999D44ED2AE49C0E":"010",...}
		/* Permissions:
		 * 001 - public users(read only)
		 * 010 - users(read/write only)
		 * 100 - administrator(read/write/delete only)
		 */
		else{
			JSONObject json = new JSONObject(); 
			json.putAll(map.getStudiesList());
			this.studiesString = json.toJSONString();
			map.emptyStudiesList();//clear hashmap
		}
		return this.studiesString; 
	}
}
