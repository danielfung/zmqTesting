package zeromqTest.zmqCRMS;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import zeromqTest.rrserver;

import com.fasterxml.jackson.core.JsonProcessingException;

public class rrserverCRMS extends rrserver {
	
	public rrserverCRMS(){
		super();
		this.database = "studies";
		this.collection = "_clinicaltrials";
		this.studiesString = null;
		this.inputActivityList = "yes";
		this.inputRelatedStudies = "no";
		this.id = "id";
		this.resultString = null;
	}
	
	@Override
	public void reset(){
		this.database = "studies";
		this.collection = "_clinicaltrials";
		this.studiesString = null;
		this.inputActivityList = "yes";
		this.inputRelatedStudies = "no";
		this.id = "id";
		this.resultString = null;
	}
	
	@Override
	public String convertString(String item){
		String resultString = null;
       	resultString = item;
		resultString = resultString.substring(1);
		resultString = "c"+resultString;
		return resultString;
	}
	
	public static void searchArray(ArrayList test,String database, String collection, String id, String inputActivityList, String inputRelatedStudiesList) throws JsonProcessingException, IOException{	
		if(!test.isEmpty()){
			for(int i = 0; i<test.size(); i++){
				String arm = (String) test.get(i);
				mongodbCRMS mongotest = new mongodbCRMS(database, collection, arm, id, inputActivityList, inputRelatedStudiesList);	
			}
		}	
	}
	
	@SuppressWarnings("rawtypes")
	private static ArrayList crmsCustomAttribute = new ArrayList(); //store the custom attribute of CRMS
	@SuppressWarnings("rawtypes")
	private static ArrayList crmsARM = new ArrayList();//list of relatedStudies(_clinicaltrialarms)
	@SuppressWarnings("rawtypes")
	private static ArrayList crmsBUDGET = new ArrayList();//list of relatedSstudies(_clinicaltrialbudget) - found in  _clinicaltrial_customattributesmanagers
	@SuppressWarnings("rawtypes")
	private static ArrayList relatedStudies = new ArrayList();//storing all studies(budget/arm) related to the "parent study"
	
	@SuppressWarnings("unchecked")
	public void addElementsActivity(String item){
		if(crmsCustomAttribute.contains(item)){
		}
		else{
		crmsCustomAttribute.add(item);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void addElementBudget(String item){
		if(crmsBUDGET.contains(item)){
		}
		else{
			crmsBUDGET.add(item);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void addElementArm(String item){
		if(crmsARM.contains(item))	{
		}
		else{
			crmsARM.add(item);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void addElementRelatedStudies(String item){
		if(relatedStudies.contains(item)){
		}
		else{
			relatedStudies.add(item);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public String start(String request) throws JsonProcessingException, IOException{
		studiesString = new String(request); 
    	studiesString = convertString(studiesString);
		mongodbCRMS mongotest2 = new mongodbCRMS(database, collection, studiesString, id, inputActivityList, inputRelatedStudies);	
		
		collection = "_clinicaltrial_customattributesmanagers";
		id = "_id";
		inputActivityList = "no";
		inputRelatedStudies = "yes";
		searchArray(crmsCustomAttribute, database, collection, id, inputActivityList, inputRelatedStudies);
		
		collection = "_clinicaltrialarms";
		id = "_id";
		inputActivityList = "no";
		inputRelatedStudies = "no";
		searchArray(crmsARM, database, collection, id, inputActivityList, inputRelatedStudies);
		
		collection = "_clinicaltrialcontracts";
		id = "_id";
		inputActivityList = "no";
		inputRelatedStudies = "no";
		searchArray(crmsBUDGET, database, collection, id, inputActivityList, inputRelatedStudies);
		   		
		Collections.sort(relatedStudies);
		if(!relatedStudies.isEmpty()){
			System.out.println("\r\n");
			for(int i = 0; i<relatedStudies.size(); i++){
				String crms_id = (String) relatedStudies.get(i);
				if(i == relatedStudies.size()-1 && resultString != null)
				{
					resultString += '"'+crms_id+'"';
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
		}
		if(relatedStudies.isEmpty()){
			resultString = "Study: "+studiesString+" related studies not found!";
		}
        crmsCustomAttribute.clear();
        crmsARM.clear();
        crmsBUDGET.clear();  
        relatedStudies.clear();
		return resultString;
	}
}