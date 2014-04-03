package client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

import adapter.BuildAuto;
import adapter.CreateAuto;
import model.Automobile;
import net.DefaultSocketClient;
import util.FileIO;

/**
 * Project 1 for 18641 Java Smart Phone
 * 
 * Author: Xuping Lei
 * xlei@andrew.cmu.edu
 * 
 * Version: 1.4
 * Created at:	Feb.19, 2014
 * 
 * class CarModelOptionsI: 
 * 	a. Read data from a Properties Object for building a car model.
 * 	b. Transfers the read data using a Socket Class to the Server.
 * 	c. Receive a response from the Server verifying that the Car Model obj. is created successfully
 * 	d. Use CreateAuto interface to build Automobile.
 * 
 */ 

public class CarModelOptionsIO extends DefaultSocketClient implements CreateAuto {
	private Automobile automobile = null;
	private Properties properties = null;
	private final static boolean DEBUG = true;
	
	// default configuration
	public CarModelOptionsIO() throws UnknownHostException {
		super(InetAddress.getLocalHost().getHostName(), 8060);
	}
	
	public CarModelOptionsIO(String strHost, int iPort) {
		super(strHost, iPort);
	}

	public Properties buildPropertiesFromFile (String filename){
		FileIO sourceReader = new FileIO();
		properties = sourceReader.buildProperties(filename);
		
		return properties;
	}
	
	/* check the confirmation from server when the Automobile has been created. */
	public boolean checkAutoBuiltOnServer (String request){
		
		if ( request.equals(builtPropertiesSuccess) ){
			System.out.println("Automobile built successfully via Properties file on the Server");
			return true;
		}
		else{
			System.out.println("Objects built failure on the Server");
			return false;
		}

	}
	
	@Override
	public void run(){
	}
	
	public boolean sendPropertiesToServer (Properties properties){
		String request = null;
		if ( !openConnection())
			return false;
		
		if ( !sendRequest(sendPropertiesRequest) )
			return false;
		
		//if (DEBUG) System.out.println("waiting to receive response...");
		request = readRequest();
		//if (DEBUG) System.out.println("Received: "+ request);
		
		if ( (request == null) || !request.equals(listeningForProperties)){
			if (DEBUG) System.out.println("sendPropertiesToServer(): protocol error");
			return false;
		}
		
		//if (DEBUG) System.out.println("waiting to send objects...");
		if ( !sendObject(properties) ){
			if (DEBUG) System.out.println("sendObject(): failed to send object");
			return false;
		}
		if (DEBUG) System.out.println("sent objects...");
		
		//if (DEBUG) System.out.println("waiting for buildingObject message from Server...");
		request = readRequest();
		//if (DEBUG) System.out.println("Received: "+ request);
		if ( (request == null) ){
			if (DEBUG) System.out.println("sendPropertiesToServer(): timeout. Failed to build objects on Server");
			return false;
		}
		if ( !checkAutoBuiltOnServer(request) ){
			return false;
		}
		
		closeSession();
		return true;
	}

	
	@Override
	public Automobile buildAuto(String fileType, Object object) {
		BuildAuto builtauto = new BuildAuto();
		automobile = builtauto.buildAuto(fileType, object);
		
		return automobile;
	}

	@Override
	public void printAuto() {
		// TODO Auto-generated method stub
	}
	
}
