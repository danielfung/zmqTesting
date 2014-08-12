package main.java.zmqcrms;

public class participantFinderCRMS {
	
	/*
	 * convert the id to use [OID[...]]
	 * 
	 * @param idToConvert the string that will be passed in to get converted.
	 * @return            the result of the conversion.
	 */
	public String convertString(String idToConvert){
		
		idToConvert = idToConvert.replaceAll(":", "[OID[");
		idToConvert = idToConvert + "]]";
		
		return idToConvert;		
	}

}
