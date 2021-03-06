package main.java.zmqcrms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import main.java.zmq.rrserver;

import com.fasterxml.jackson.core.JsonProcessingException;

public class rrserverCRMS extends rrserver {
	
	@SuppressWarnings("rawtypes")
	private static ArrayList crmsCustomAttribute = new ArrayList(); //store the custom attribute of CRMS
	@SuppressWarnings("rawtypes")
	private static ArrayList crmsARM = new ArrayList();//list of relatedStudies(_clinicaltrialarms)
	@SuppressWarnings("rawtypes")
	private static ArrayList crmsBUDGET = new ArrayList();//list of relatedSstudies(_clinicaltrialbudget) - found in  _clinicaltrial_customattributesmanagers
	@SuppressWarnings("rawtypes")
	private static ArrayList relatedStudies = new ArrayList();//storing all studies(budget/arm) related to the "parent study"
	@SuppressWarnings("rawtypes")
	private static ArrayList participantId = new ArrayList();//stores all participantId(_participants) - found in _participantrecord_customattributesmangers
	@SuppressWarnings("rawtypes")
	private static ArrayList participantCustomAttribute = new ArrayList();//stores all participantCustomAttributes - found in _participants
	@SuppressWarnings("rawtypes")
	private static ArrayList relatedParticipants = new ArrayList();//list of all related participants of this study - found in _participant_customattributesmanagers
	/*
	 * the constructor, set the variables to a specific database, collection, id, resultString, 
	 * studiesString, inputActivityList, inputRelated Studies.
	 */
	public rrserverCRMS(){
		super();
		this.database = "studies";
		this.collection = "_clinicaltrials";
		this.studiesString = null;
		this.id = "id";
		this.resultString = null;
		this.inputListName = "crmsCustomAttribute";
	}
	
	/*
	 * For each item in the ArrayList test query for a specific field, based of the specific database and collection.
	 * 
	 * @param test                    the array list in which the function will look in
	 * @param database                which database to use from mongodb
	 * @param collection              which collection to use from the database in mongodb
	 * @param id                      which field within the collection
	 * @param inputActivityList       decide whether to place into ActivityList(yes or no)
	 * @param inputRelatedStudiesList decide whether to place into relatedStudies(yes or no)
	 */
	public static void searchArray(ArrayList test,String database, String collection, String id, String inputWhichList) throws JsonProcessingException, IOException{	
		if(!test.isEmpty()){
			for(int i = 0; i<test.size(); i++){
				String arm = (String) test.get(i);
				mongodbCRMS mongotest = new mongodbCRMS(database, collection, arm, id, inputWhichList);	
			}
		}	
	}
	
	/*
	 * Reset the variables(database, collection, id, resultString, studiesString, 
	 * inputActivityList, inputRelatedStudies) to be able to query from irb
	 */
	@Override
	public void reset(){
		this.database = "studies";
		this.collection = "_clinicaltrials";
		this.studiesString = null;
		this.id = "id";
		this.resultString = null;
		this.inputListName = "crmsCustomAttribute";
	}
	
	/*
	 * Given a study id, convert to crms id type.
	 * 
	 * @param item the study id that user wants to obtain related arms/budget of
	 * @return     returns the converted study id back
	 */
	@Override
	public String convertString(String item){
		String resultString = null;
       	resultString = item;
		resultString = resultString.substring(1);
		resultString = "c"+resultString;
		return resultString;
	}
	
	/*
	 * Add an element into crmsCustomAttribute arraylist
	 * 
	 * @param item the item to be added into crmsCustomAttribute arraylist
	 */
	@SuppressWarnings("unchecked")
	public void addElementsActivity(String item){
		if(crmsCustomAttribute.contains(item)){
		}
		else{
			crmsCustomAttribute.add(item);
		}
	}
	
	/*
	 * Add an element into crmsBUDGET arraylist
	 * 
	 * @param item the item to be added into crmsBUDGET arraylist
	 */
	@SuppressWarnings("unchecked")
	public void addElementBudget(String item){
		if(crmsBUDGET.contains(item)){
		}
		else{
			crmsBUDGET.add(item);
		}
	}
	
	/*
	 * add an element into crmsARM arraylist
	 * 
	 * @param item the item to be added into crmsARM arraylist
	 */
	@SuppressWarnings("unchecked")
	public void addElementArm(String item){
		if(crmsARM.contains(item))	{
		}
		else{
			crmsARM.add(item);
		}
	}
	
	/*
	 * add an element into relatedStudies arraylist
	 * 
	 * @param item the item to be added into relatedStudies arraylist
	 */
	@SuppressWarnings("unchecked")
	public void addElementRelatedStudies(String item){
		if(relatedStudies.contains(item)){
		}
		else{
			relatedStudies.add(item);
		}
	}
	
	/*
	 * add an element into relatedParticipants arraylist
	 * 
	 * @param item the item to be added into relatedParticipants arraylist
	 */
	@SuppressWarnings("unchecked")
	public void addElementRelatedParticipant(String item){
		if(relatedParticipants.contains(item)){
		}
		else{
			relatedParticipants.add(item);
		}
	}
	
	/*
	 * add an element into participantCustomAttribute arraylist
	 * 
	 * @param item the item to be added into participantCustomAttribute arraylist
	 */
	@SuppressWarnings("unchecked")
	public void addElementParticipantCustomAttribute(String item){
		if(participantCustomAttribute.contains(item)){
		}
		else{
			participantCustomAttribute.add(item);
		}
	}
	
	/*
	 * add an element into participantId arraylist
	 * 
	 * @param item the item to be added into participantId arraylist
	 */
	@SuppressWarnings("unchecked")
	public void addElementParticipant(String item){
		if(participantId.contains(item)){
		}
		else{
			participantId.add(item);
		}
	}
	/*
	 * Takes a study id input and query mongodb for all related arms/budgets.
	 * 
	 * @param request the study id the user wants to get all related studies of.
	 * @return        list of related related arms/budgets if study exists
	 */
	@Override
	@SuppressWarnings("unchecked")
	public String start(String request) throws JsonProcessingException, IOException{
		reset();
		studiesString = new String(request); 
    	studiesString = convertString(studiesString);
		mongodbCRMS mongotest2 = new mongodbCRMS(database, collection, studiesString, id, inputListName);	
		
		collection = "_clinicaltrial_customattributesmanagers";
		id = "_id";
		inputListName = "arms";
		searchArray(crmsCustomAttribute, database, collection, id, inputListName);
		
		collection = "_clinicaltrialarms";
		id = "_id";
		inputListName = "relatedStudies";
		searchArray(crmsARM, database, collection, id, inputListName);
		
		collection = "_clinicaltrialcontracts";
		id = "_id";
		inputListName = "relatedStudies";
		searchArray(crmsBUDGET, database, collection, id, inputListName);
		
		collection = "_participantrecord_customattributesmanagers";
		id = "visitSchedule";
		inputListName = "participantList";
		for(int i = 0; i<crmsARM.size(); i++){
			String arm = (String) crmsARM.get(i);
			participantFinderCRMS partFind = new participantFinderCRMS();
			arm = partFind.convertString(arm);
			mongodbCRMS mongotest = new mongodbCRMS(database, collection, arm, id, inputListName);	
		}
		
		collection = "_participants";
		id = "_id";
		inputListName = "participantCustomAttributeList";
		searchArray(participantId, database, collection, id, inputListName);
		
		collection = "_participant_customattributesmanagers";
		id = "_id";
		inputListName = "relatedParticipantList";
		searchArray(participantCustomAttribute, database, collection, id, inputListName);
		   		
		Collections.sort(relatedStudies);
		if(!relatedStudies.isEmpty()){
			for(int i = 0; i<relatedStudies.size(); i++){
				String crms_id = (String) relatedStudies.get(i);
				if(i == relatedStudies.size()-1 && resultString != null)
				{
					resultString += '"'+crms_id+'"'+",";
				}
				else if( i != relatedStudies.size()-1 && resultString != null)
				{
					resultString += '"'+crms_id+'"'+",";
				}
				else if(resultString == null && relatedStudies.size() != 1){
					resultString = '"'+crms_id+'"'+",";
				}
				else{
					resultString = '"'+crms_id+'"';
				}
			}
			
			for(int i = 0; i<relatedParticipants.size(); i++){
				String crms_id = (String) relatedParticipants.get(i);
				if(i == relatedParticipants.size()-1 && resultString != null)
				{
					resultString += '"'+crms_id+'"';
				}
				else if( i != relatedParticipants.size()-1 && resultString != null)
				{
					resultString += '"'+crms_id+'"'+",";
				}
				else{
					resultString = '"'+crms_id+'"';
				}
			}
		}
		if(relatedStudies.isEmpty()){
			resultString = "Study: "+studiesString+" related studies not found!";
		}
        crmsCustomAttribute.clear();
        crmsARM.clear();
        crmsBUDGET.clear();  
        relatedStudies.clear();
        participantId.clear();
        participantCustomAttribute.clear();
        relatedParticipants.clear();
		return resultString;
	}
}