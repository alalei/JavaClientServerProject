package server;

import java.net.Socket;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

import net.DefaultSocketClient;
import adapter.BuildAuto;
import model.Automobile;

/**
 * Project 1 for 18641 Java Smart Phone
 * 
 * Author: Xuping Lei
 * xlei@andrew.cmu.edu
 * 
 * Version: 1.4
 * Created at:	Feb.19, 2014
 * 
 * class BuildCarModelOptions: accept a properties file and create an Automobile
 * 
 */ 

public class BuildCarModelOptions extends DefaultSocketClient implements AutoServer {

	BuildAuto builtauto = null;
	private final static boolean DEBUG = true;
	private final static boolean DEBUG2_PART = true;
	
	public BuildCarModelOptions(Socket socket, BuildAuto builtauto) {
		super(socket);
		this.builtauto = builtauto;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public Automobile buildCarModelOptions(Properties properties) {
		if (properties == null)
			return null;
		
		return builtauto.buildCarModelOptions(properties);
	}
	
	@Override
	public void run(){
		if (DEBUG) System.out.println ("Thread " + getId() + " is running...");
		
		if ( !openConnection() ){
			if (DEBUG) System.out.println ("Server: failed to open connection");
			return;
		}
		
		if (DEBUG2_PART) System.out.println ("Reading request... ");
		String request = readRequest();
		if (request == null){
			closeSession();
			return;
		}
		
		/* Recognize different requests */
		if (request.equals(sendPropertiesRequest)){
			getPropertiesFromClient();
		}
		else if (request.equals(updateModelRequest)){
			updateModelFromClient();
		}
		else if ( request.equals(queryModelNames) ){
			provideModelListToClient();
		}
		else if ( request.contains(requestModelBackToClient) ){
			String[] requestParas = request.split("\t");
			if (requestParas[1] != null){
				sendModelToClient(requestParas[1]);
			}
		}
		
		closeSession();
	}

	public boolean getPropertiesFromClient() {
		if (!sendRequest(listeningForProperties)) return false;
		
		Object properties = readObject("Properties");
		if ( properties == null ) return false;

		Automobile automobile = buildCarModelOptions( (Properties) properties);
		if (automobile == null){
			sendRequest(builtPropertiesFailure);
			return false;
		}
		
		if (DEBUG) automobile.print();
		sendRequest(builtPropertiesSuccess);
		return true;
	}
	
	public boolean updateModelFromClient() {
		if (!sendRequest(listeningForModel)) return false;
		
		Object model = readObject("Automobile");
		if ( model == null ) return false;

		
		Automobile automobile = builtauto.updateCarModelOptions((Automobile) model);
		if (automobile == null){
			sendRequest(updateModelFailure);
			return false;
		}
		
		if (DEBUG) automobile.print();
		sendRequest(updateModelSuccess);
		return true;
	}
	
	@Override
	public boolean provideModelListToClient() {
		if (!sendRequest("Model names:")) return false;
		
		LinkedList<String> modelNames = builtauto.getModelNames();
		Iterator<String> nameIter = modelNames.descendingIterator();
		while (nameIter.hasNext()){
			if ( !sendRequest (nameIter.next()) ){
				return false;
			}
		}

		return true;
	}

	@Override
	public boolean sendModelToClient(String modelName) {
		if (modelName == null || modelName.equals("")) 
			return false;
		
		Automobile automobile = builtauto.getModelbyNames( modelName);
		if ( automobile == null){
			sendRequest (notFoundModel);
			return false;
		}
		
		if ( !sendRequest (foundModel)  ){
			if (DEBUG) System.out.println("Protocol error");
			return false;
		}
		if ( !(readRequest().equals(listeningForModel)) ){
			if (DEBUG) System.out.println("Protocol error");
			return false;
		}
		if ( !sendObject(automobile) ){
			if (DEBUG) System.out.println("sendObject(): failed to send object");
			return false;
		}
		if (DEBUG) System.out.println("sent objects...");
		return false;
	}

}
