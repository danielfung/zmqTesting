package main.java.zmqauth;

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
	
	/*
	 * A json string gets passed into a mapper, then find the field and extract it.
	 * Once extracted place into the hashmap.
	 * 
	 * @param json     the json string to pass to mapper
	 * @param field    which field we are using
	 * @param listType which list we are using
	 */
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
	
	/*
	 * add item to studiesList map
	 * 
	 * @param key   key value
	 * @param value the value
	 */
	public void addItem(String key, String value){
		studiesList.put(key, value);
	}
	
	/*
	 * remove key from studiesList map
	 * 
	 * @param key key value
	 */
	public void removeItem(String key){
		studiesList.remove(key);
	}
	
	/*
	 * return the studiesList map
	 */
	public Map<String,String> getStudiesList(){
		return studiesList;
	}
	
	/*
	 * Get the permissions of a particular study
	 * 
	 * @param studyId the studyId associated with this particular person
	 * @return 		  return the permission
	 */
	public String getStudyPermission(String studyId){
		String permission = studiesList.get(studyId);
		return permission;
	}
	
	/*
	 * clear the study list 
	 */
	public void emptyStudiesList(){
		studiesList.clear();
	}
}

