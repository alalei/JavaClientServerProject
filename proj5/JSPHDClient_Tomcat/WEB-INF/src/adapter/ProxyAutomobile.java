package adapter;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Properties;

import util.FileIO;
import model.Automobile;

/**
 * Project 1 for 18641 Java Smart Phone
 * 
 * Author: Xuping Lei
 * xlei@andrew.cmu.edu
 * 
 * Version: 1.2
 * Created at:	Feb.7, 2014
 * 
 */

public abstract class ProxyAutomobile{
	protected static Automobile a1 = null; // Current Model
    private static LinkedHashMap<String, Automobile> modelSets; // Set of Models (Automobile)
    //private static int modelCounter = 0;
    private final static boolean DEBUG = true;

    ProxyAutomobile(){
		if (modelSets == null){
			modelSets = new LinkedHashMap<String, Automobile>();
		}
    }

    public Automobile getCurrentModel(){
    	return a1;
    }
    
    public Automobile getModelbyNames(String modelName){
    	if (modelName == null || modelName.equals("")) 
			return null;
		
		if (modelSets.containsKey(modelName)){
			return modelSets.get(modelName);
		}
		else{
			return null;
		}
    }
    
    public LinkedList<String> getModelNames(){
    	LinkedList<String> modelNames = new LinkedList<String>() ;
    	Iterator<Automobile> autoIter = modelSets.values().iterator();
		while(autoIter.hasNext()){
			modelNames.add ( autoIter.next().getName() );
		}
    	return modelNames;
    }
    
    /* Build Car Model using *.properties objects */
    public Automobile buildCarModelOptions (Properties properties) {
    	return buildAuto ("properties", properties);	
    }
    
    /* Build Car Model using *.properties objects */
    public Automobile updateCarModelOptions (Automobile automobile) {
    	return buildAuto ("automobile", automobile);	
    }
    
    /* Build Car Model based on file types */
    public Automobile buildAuto (String fileType, Object object) {
		if (modelSets == null)
			return null;
		if (object == null){
			return null;
		}
		
		Automobile[] automibiles = null;
		FileIO sourceReader = new FileIO();
		if (fileType.equals("txt")) {
			if (DEBUG) System.out.println("Build Automobile from file "+ (String) object);
			automibiles = sourceReader.buildAutoObject( (String) object );
		}
		else if (fileType.equals("properties")){
			if (DEBUG) System.out.println("Build Automobile from Properties Type");
			automibiles = sourceReader.buildAutoObject( (Properties) object );
		}
		else if (fileType.equals("automobile")){
			if (DEBUG) System.out.println("Add/Update Automobile from Automobile Type");
			automibiles = new Automobile[] {(Automobile)object};
		}
		else{
			return null;
		}
		
		if(automibiles == null){
			System.out.println("Test result: detected SourceReader.buildAutoObject return null\n");
			return null;
		}
		
		for(Automobile automobile: automibiles){
			if (automobile == null) 
				continue;
			
			if (modelSets.containsKey(automobile.getModel())){
				modelSets.put(automobile.getModel(), automobile);
			}
			else{
				modelSets.put(automobile.getModel(), automobile);
				//modelCounter++;
			}
		}
		
		a1 = automibiles[0];
		return a1;
		
    }
    
    /* old version of Automobile buildAuto (String dataFile) */
	public Automobile buildAuto2(String dataFile){
		if (modelSets == null)
			return null;
	
		//a1 = new Automobile(20);
		
		FileIO sourceReader = new FileIO();
		System.out.println("Test result: read file "+ dataFile);
		Automobile[] automibiles = sourceReader.buildAutoObject(dataFile);
		
		if(automibiles == null){
			System.out.println("Test result: detected SourceReader.buildAutoObject return null\n");
			return null;
		}
		
		for(Automobile automobile: automibiles){
			if (automobile == null) 
				continue;
			
			if (modelSets.containsKey(automobile.getModel())){
				modelSets.put(automobile.getModel(), automobile);
			}
			else{
				modelSets.put(automobile.getModel(), automobile);
				//modelCounter++;
			}
		}
		
		a1 = automibiles[0];
		return a1;
	}

	public void printAuto(){
		if (modelSets == null) {
			System.out.println("printAuto(): input argument is null");
		}
		
		Iterator<Automobile> autoIter = modelSets.values().iterator();
		int i = 0;
		while(autoIter.hasNext()){
			i++;
			System.out.println("-- "+ i + " --");
			autoIter.next().print();
		}
				
	}
	
	public boolean updateOptionSetName(String modelName, String optionSetName, String newName){		
		if (modelSets == null)
			return false;
			
		if (modelSets.containsKey(modelName)){
			a1 = modelSets.get(modelName);
		}
		else{
			return false;
		}
		
		return a1.updateOptionSetName(optionSetName, newName);
	}
	
	public boolean updateOptionPrice(String modelName, String optionSetName, String optionName, float newPrice){
		if (modelSets == null)
			return false;
		
		if (modelSets.containsKey(modelName)){
			a1 = modelSets.get(modelName);
		}
		else{
			return false;
		}
		
		return a1.updateOptionPrice(optionSetName, optionName, newPrice);
	}
	
	public boolean editOptionSetName(String modelName, String optionSetName, String newName){
		if (modelSets == null)
			return false;
		
		if (modelSets.containsKey(modelName)){
			a1 = modelSets.get(modelName);
		}
		else{
			return false;
		}
		
		return a1.updateOptionSetName(optionSetName, newName);
	}

	public boolean editOptionPrice(String modelName, String optionSetName, String optionName, float newPrice){
		if (modelSets == null)
			return false;
		
		if (modelSets.containsKey(modelName)){
			a1 = modelSets.get(modelName);
		}
		else{
			return false;
		}
		
		return a1.updateOptionPrice(optionSetName, optionName, newPrice);
	}

	
}
