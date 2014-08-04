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
	
	/*
	 * the constructor, set the variables to a specific database, collection, id, resultString, 
	 * studiesString, inputActivityList, inputRelated Studies.
	 */
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
	
	/*
	 * For each item in the ArrayList test query for a specific field, based of the specific database and collection.
	 * 
	 * @param test                the array list in which the function will look in
	 * @param database            which database to use from mongodb
	 * @param collection          which collection to use from the database in mongodb
	 * @param id                  which field within the collection
	 * @param inputActivityList   decide whether to place into ActivityList(yes or no)
	 * @param inputRelatedStudies decide whether to place into RelatedStudies(yes or no)
	 */
	public static void searchArray(ArrayList test,String database, String collection, String id, String inputActivityList, String inputRelatedStudiesList) throws JsonProcessingException, IOException{	
		if(!test.isEmpty()){
			for(int i = 0; i<test.size(); i++){
				String arm = (String) test.get(i);
				mongodbIRB mongotest = new mongodbIRB(database, collection, arm, id, inputActivityList, inputRelatedStudiesList);	
			}
		}	
	}
	
	/*
	 * Given a study id, convert to irb id type.
	 * 
	 * @param item the study id that user wants to obtain related studies of
	 * @return     returns the converted study id back to user
	 */
	@Override
	public String convertString(String item) {
		String resultString = item;
		resultString = resultString.substring(1);
		resultString = "i"+resultString;
		return resultString;
	}
	
	/*
	 * Reset the variables(database, collection, id, resultString, studiesString, 
	 * inputActivityList, inputRelatedStudies) to be able to query from irb
	 */
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
	
	/*
	 * Add an element into the irbRelatedStudies array list.
	 * 
	 * @param item the item to be added into irbRelatedStudies
	 */
	@SuppressWarnings("unchecked")
	public void addElementsIrb(String item){
		if(irbRelatedStudies.contains(item)){
		}
		else{
			irbRelatedStudies.add(item);
		}
	}
	
	/*
	 * Add an element into the irbSubmission_id array list.
	 * 
	 * @param item the item to be added into irbSubmission_id
	 */
	@SuppressWarnings("unchecked")
	public void addElementIrbParentStudy_id(String item){
		if(irbSubmission_id.contains(item)){
		}
		else{
			irbSubmission_id.add(item);
		}
	}
	
	/*
	 * Add an element into the relatedStudies array list.
	 * 
	 * @param item the item to be added into relatedStudies
	 */
	@SuppressWarnings("unchecked")
	public void addElementIrbRelatedStudies(String item){
		if(relatedStudies.contains(item)){
		}
		else{
			relatedStudies.add(item);
		}
	}
	
	/*
	 * Takes a study id input and query mongodb for all studies related to the given study id.
	 * 
	 * @param request the study id the user wants to get all related studies of.
	 * @return        list of related studies if study exists
	 */
	@Override
	public String start(String request) throws InterruptedException, IOException {
		reset();
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
}
