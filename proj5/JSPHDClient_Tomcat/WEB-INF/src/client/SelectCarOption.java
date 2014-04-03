package client;

import java.net.InetAddress;
import java.net.UnknownHostException;

import model.Automobile;
import net.DefaultSocketClient;

/**
 * Project 1 for 18641 Java Smart Phone
 * 
 * Author: Xuping Lei
 * xlei@andrew.cmu.edu
 * 
 * Version: 1.4
 * Created at:	Feb.19, 2014
 * 
 * class SelectCarOption: 
 * 	a. Prompt the user of available models.
 * 	b. Allow user to select a model and enter its respective options.
 * 	c. Display the selected options for a class.
 * 
 */ 

public class SelectCarOption extends DefaultSocketClient {
	private Automobile automobile = null;
	//private Properties properties = null;
	private final static boolean DEBUG = true;
	
	// default configuration
	public SelectCarOption() throws UnknownHostException {
		super(InetAddress.getLocalHost().getHostName(), 8060);
	}
	
	public SelectCarOption(String strHost, int iPort) {
		super(strHost, iPort);
	}
	
	
	public String queryModelsfromServer (){
		StringBuilder requestBld = new StringBuilder();
		
		if ( !openConnection())
			return null;
		
		if ( !sendRequest(queryModelNames) )
			return null;
		
		System.out.println ("Models on the server:");
		do{
			String message = readRequest();
			if (message == null || message.equals("") ){
				break;
			}
			requestBld.append(message + "\n");
		} while(true);
		System.out.println (requestBld);
		
		closeSession();
		return requestBld.toString();
	}
	
	/* get a model from local client */
	public Automobile getModelfromLocal (){
		return this.automobile;
	}
	
	/* get a model from Server */
	public Automobile getModelfromServer (String modelName){
		if ( modelName == null || modelName.equals("") ){
			return null;
		}
		
		if ( !openConnection())
			return null;
		
		if ( !sendRequest(requestModelBackToClient + "\t" + modelName) )
			return null;
		
		String message = readRequest();
		if (message == null){
			if (DEBUG) System.out.println("Protocol error");
			return null;
		}
		else if ( message.equals(notFoundModel) ){
			if (DEBUG) System.out.println("Not Found Model On server");
			return null;
		}
		else if ( !message.equals(foundModel) ){
			if (DEBUG) System.out.println("Protocol error");
			return null;
		}
		
		if ( !sendRequest(listeningForModel) )
			return null;
		
		Automobile automobileTemp = (Automobile) readObject("Automobile");
		if (automobileTemp == null){
			System.out.println("Failed to get the model " + modelName);
		}
		else{
			this.automobile = automobileTemp;
			if (DEBUG) System.out.println("Get model from server");
		}
		
		closeSession();
		return this.automobile;
	}
	
	/* Update models on Local */
	public boolean updateModelOnLocal
			(String modelName, String optionSetName, String optionName, float newPrice){
		
		if (  (automobile == null) || !(this.automobile.getModel().equals(modelName)) ){
			if ( (getModelfromServer (modelName)) == null)
				return false;
		}
		if ( !(automobile.updateOptionSet(optionSetName, optionName, newPrice)) ){
			return false;
		}
		
		automobile.setOptionChoice(optionSetName, optionName);
		return true;
	}
	
	/* Update models on Server */
	public boolean updateModelOnServer
			(String modelName, String optionSetName, String optionName, float newPrice){
		
		if (!updateModelOnLocal(modelName, optionSetName, optionName, newPrice)){
			return false;
		}
		if ( !sendModelsToServer( automobile) ){
			return false;
		}
		
		return true;
	}
	
	public boolean sendModelsToServer (Automobile automobile){
		String request = null;
		if ( !openConnection())
			return false;
		
		if ( !sendRequest(updateModelRequest) )
			return false;
		
		request = readRequest();
		if ( (request == null) || !request.equals(listeningForModel)){
			if (DEBUG) System.out.println("sendModelsToServer(): protocol error");
			return false;
		}
		
		//if (DEBUG) System.out.println("waiting to send objects...");
		if ( !sendObject(automobile) ){
			if (DEBUG) System.out.println("sendObject(): failed to send object");
			return false;
		}
		if (DEBUG) System.out.println("sent objects...");
		
		//if (DEBUG) System.out.println("waiting for buildingObject message from Server...");
		request = readRequest();
		//if (DEBUG) System.out.println("Received: "+ request);
		if ( (request == null) ){
			if (DEBUG) System.out.println("sendModelsToServer(): timeout. Failed to build objects on Server");
			return false;
		}
		
		if ( request.equals(updateModelSuccess) ){
			if (DEBUG) System.out.println("Update objects on Server successfully");
		}
		else {
			if (DEBUG) System.out.println("Failed to update objects on Server");
			return false;
		}
		
		closeSession();
		return true;
	}
	
	
	/* print options */
	public void printModel (){
		if ( automobile == null ){
			System.out.println("No model has been selected");
			return;
		}
		automobile.print();
	}
	
	public void printModelWithSelectedChoice (){
		if ( automobile == null ){
			System.out.println("No model has been selected");
			return;
		}
		automobile.printWithSelectedChoices();
	}
	

}
