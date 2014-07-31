package zeromqTest.zmqIRB;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.json.simple.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;

import zeromqTest.rrserver;

public class rrserverIRB extends rrserver{
	
	@SuppressWarnings("rawtypes")
	private static ArrayList irbSubmission_id = new ArrayList();//list of relatedStudies(to be found in _irbsubmissions - under customAttributes)
	@SuppressWarnings("rawtypes")
	private static ArrayList irbRelatedStudies = new ArrayList();//list of _id(based on parent study(_id)) - found in _irbsubmission_customattributesmanagers
	@SuppressWarnings("rawtypes")
	private static ArrayList relatedStudies = new ArrayList();//storing all studies related to the "parent study" ex)i08-411 --> list of modifications/continuing review related to it.
	
	public rrserverIRB(){
		super();
		this.database = "studies";
		this.collection = "_irbsubmissions";
		this.id = "id";
		this.resultString = null;
		this.studiesString = null;
		this.inputActivityList = "yes";
		this.inputRelatedStudies = "no";
	}
	
	public static void searchArray(ArrayList test,String database, String collection, String id, String inputActivityList, String inputRelatedStudiesList) throws JsonProcessingException, IOException{	
		if(!test.isEmpty()){
			for(int i = 0; i<test.size(); i++){
				String arm = (String) test.get(i);
				mongodbIRB mongotest = new mongodbIRB(database, collection, arm, id, inputActivityList, inputRelatedStudiesList);	
			}
		}	
	}
	
	@Override
	public String convertString(String item) {
		String resultString = item;
		resultString = resultString.substring(1);
		resultString = "i"+resultString;
		return resultString;
	}

	@Override
	public String start(String request) throws InterruptedException,
			IOException {
		studiesString = request;
		studiesString = convertString(studiesString);
		inputActivityList = "yes";
		inputRelatedStudies = "no";
		mongodbIRB mongotest2 = new mongodbIRB(database, collection, studiesString, id, inputActivityList, inputRelatedStudies);	

		inputActivityList = "no";
		inputRelatedStudies = "yes";
		id = "parentStudy";
		collection = "_irbsubmission_customattributesmanagers";
		searchArray(irbSubmission_id, database, collection, id, inputActivityList, inputRelatedStudies);
		     		
		inputActivityList = "no";
		inputRelatedStudies = "no";
		id = "customAttributes";
		collection = "_irbsubmissions";
		searchArray(irbRelatedStudies, database, collection, id, inputActivityList, inputRelatedStudies);
	      		
		Collections.sort(relatedStudies);
		if(!relatedStudies.isEmpty()){
			for(int i = 0; i<relatedStudies.size(); i++){
				String irb_id = (String) relatedStudies.get(i);
	
				if(i == relatedStudies.size()-1 && resultString != null){
					resultString += '"'+irb_id+'"';
				}
				else if(i != relatedStudies.size()-1 && resultString != null)
				{
					resultString += '"'+irb_id+'"'+",";
				}
				else if(resultString == null && relatedStudies.size() != 1){
						resultString = '"'+irb_id+'"'+",";
				}
				else{
					resultString = '"'+irb_id+'"';
				}
			}
		}
		if(irbSubmission_id.isEmpty()){
			resultString = "Study: "+studiesString+" related studies not found!";
		}
		relatedStudies.clear();
		irbRelatedStudies.clear();
		irbSubmission_id.clear();
		return resultString;
	}

	@Override
	public void reset() {
		this.database = "studies";
		this.collection = "_irbsubmissions";
		this.id = "id";
		this.resultString = null;
		this.studiesString = null;
		this.inputActivityList = "yes";
		this.inputRelatedStudies = "no";		
	}
	
	@SuppressWarnings("unchecked")
	public void addElementsIrb(String item){
		if(irbRelatedStudies.contains(item)){
		}
		else{
			irbRelatedStudies.add(item);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void addElementIrbParentStudy_id(String item){
		if(irbSubmission_id.contains(item)){
		}
		else{
			irbSubmission_id.add(item);
		}
	}
	
	@SuppressWarnings("unchecked")
	public void addElementIrbRelatedStudies(String item){
		if(relatedStudies.contains(item)){
		}
		else{
			relatedStudies.add(item);
		}
	}
}
