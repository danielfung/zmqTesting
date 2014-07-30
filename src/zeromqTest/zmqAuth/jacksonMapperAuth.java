package zeromqTest.zmqAuth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class jacksonMapperAuth {
	
	static private Map<String, String> studiesList = new HashMap<String, String>();
	private String readers = "readers";
	private String editors = "editors";
	
	public void setMapperList(String json, String field, String listType) throws JsonProcessingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = null;	
		rootNode = mapper.readTree(json);//input the json string into tree
		JsonNode _id = rootNode.findPath(field);//grabbing the study id
		
		if(listType == readers){
			addItem(_id.asText(), "001");//read only
		}
		else if(listType == editors){
			if(studiesList.containsKey(_id.asText())){
				addItem(_id.asText(), "010");//read/write only
			}
			else{
				addItem(_id.asText(), "000");//write only
			}
		}
	}
	
	public void addItem(String key, String value){
		studiesList.put(key, value);
	}
	
	public void removeItem(String key){
		studiesList.remove(key);
	}
	
	public Map<String,String> getStudiesList(){
		return studiesList;
	}
	
	public String getStudyPermission(String studyId){
		String permission = studiesList.get(studyId);
		return permission;
	}
	
	public void emptyStudiesList(){
		studiesList.clear();
	}
}

