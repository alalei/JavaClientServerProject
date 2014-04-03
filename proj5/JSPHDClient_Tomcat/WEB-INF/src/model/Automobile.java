package model;


import java.util.ArrayList;
import java.util.Iterator;

import util.FileIO;
import model.OptionSet.Option;


/**
 * Project 1 for 18641 Java Smart Phone
 * 
 * class Automotive: class to map Automotive model.
 * Author: Xuping Lei
 * xlei@andrew.cmu.edu
 * 
 * Created at:	Feb.1, 2014
 *
 * Version: 1.3
 * Modified at:	Feb.14, 2014
 *  1. change the type of "opset" from OptionSet[] to ArrayList<OptionSet>[]
 *  
 */ 


public class Automobile implements java.io.Serializable{
	
	private String name;
	private float baseprice;
	private ArrayList<OptionSet> opset;
	private static final long serialVersionUID = 1L;
    private String make;
    private String model;
    Option choice;
    
    private final static boolean DEBUG = true;
    
	// Constructors
	public Automobile(){
		opset = new ArrayList<OptionSet>(10); 
		this.name = "";
		this.baseprice = 0;
	}

	public Automobile(int optionSetSize){
		opset = new ArrayList<OptionSet>(optionSetSize); 
		this.name = "";
		this.baseprice = 0;
	}

	public Automobile(String name, float baseprice, int optionSetSize){
		this.name = name;
		this.baseprice = baseprice;
		opset = new ArrayList<OptionSet>(optionSetSize);
	}
	
	
	// new added method: calculate total price
	public synchronized int calculateTotalPrice(){
		if (opset==null)
			return Integer.MIN_VALUE;
		
		int pricesum = (int) this.baseprice;
		for ( OptionSet optionSet: opset){
			int choicePrice = optionSet.getOptionChoicePrice();
			if (choicePrice!=Integer.MIN_VALUE){
				pricesum += choicePrice;
			}
		}
		return pricesum;
	}
	

	// Getters
	public synchronized String[] getOptionSetNames(){
		if (opset==null)
			return null;
		
		String[] strArray = new String[opset.size()];
		int i =0;
		for ( OptionSet optionSet: opset){
			strArray[i] = optionSet.getName();
			i++;
		}
		return strArray;
	}
	
	public synchronized String[] getOptionNames(String setName){
		if (opset==null)
			return null;
		
		int index = findOptionSet(setName);
		if (index<0)
			return null;
		
		OptionSet optionSet = opset.get(index);
		String[] OptionNames = optionSet.getOptionNames();
		return OptionNames;
	}
	
	public synchronized String getOptionChoice(String setName){
		//if (choice == null) return null;
		int index = findOptionSet(setName);
		if (index < 0) 
			return null;
		
		return opset.get(index).getOptionChoice();
	}
	
	public synchronized int getOptionChoicePrice(String setName){
		int index = findOptionSet(setName);
		if( index<0 ){
			return Integer.MIN_VALUE;
		}
		return opset.get(index).getOptionChoicePrice();
	}
	
	public synchronized String getMake(){
		return make;
	}
	
	public synchronized String getModel(){
		return model;
	}
	
	public synchronized String getName(){
		return name;
	}
	
	public synchronized float getBasePrice(){
		return baseprice;
	}
	
	public synchronized OptionSet getOptionSet(int index){
		if(index<0 || index >= opset.size()) 
			return null;
		if(opset==null) 
			return null;
		
		return opset.get(index);
	}
	
	
	//Find
	public synchronized int findOptionSet (String name){
		if(opset == null)
			return -1;
		if(name == null || name.equals(""))
			return -1;
		
		for(int i = 0, len = opset.size(); i < len ; i++){
			if (opset.get(i) != null && opset.get(i).getName().equals(name))
				return i;	// return index in opset[]
		}
		return -1;
	}
	
	
	//Setters
    public synchronized void setOptionChoice(String setName, String optionName){
    	int index = findOptionSet(setName);
    	
    	// choice = null;
		if( index < 0 ){		// if no opset.name == optionSetName
			return;
		}
		else{
			opset.get(index).setOptionChoice(optionName);
			if(opset.get(index).getOptionChoice() != null){// if no option.name == optionName
				choice = opset.get(index).choice;
			}
			return;
		}
	}

	public synchronized boolean setMake(String make){
		if (make == null || make.equals(""))
			return false;
		
		this.make = make;
		return true;
	}
	
	public synchronized boolean setModel(String modelName){
		if (modelName == null || modelName.equals(""))
			return false;
		
		this.model = modelName;
		return true;
	}
	
	public synchronized boolean setName(String name){
		if (name==null || name.equals(""))
			return false;
		
		this.name = name;
		return true;
	}
	
	public synchronized boolean setBasePrice(float price){
		if (price<0)
			return false;
		
		this.baseprice = price;
		return true;
	}
	
	public synchronized boolean setOptionSetSize(int optionSetSize){
		//opset[] has been set before
		if (opset != null)
			return false;
		
		opset = new ArrayList<OptionSet>(optionSetSize); 
		return true;
	}
	
	/* Equal to add an OptionSet in the initiation */
	public synchronized boolean setOptionSet(OptionSet optSet){
		if(opset==null) 
			return false;
		
		this.opset.add(optSet);
		return true;
	}
	
	public synchronized boolean setOptionSet(OptionSet optSet, int index){
		if(index<0 || index > opset.size()) 
			return false;
		if(opset==null) 
			return false;
		
		// this.opset.remove(index);
		this.opset.add(index, optSet);
		return true;
	}
	
	public synchronized boolean setOptionSetValue(String optionSetName, String optionName, float price){
		if(opset == null)
			return false;

		int index = findOptionSet(optionSetName);
		if( index < 0 ){ // the OptionSet not exist, create a new
			OptionSet newOp = new OptionSet(optionSetName,3);
			newOp.setOption(optionName,price);
			opset.add(newOp);
			return true;
		}
		else{
			return opset.get(index).setOption(optionName,price);
		}
	}
	
	
	// Update
	public synchronized boolean updateOptionSet(String optionSetName, String optionName, float price){
		if (opset == null)
			return false;
		
		int index = findOptionSet(optionSetName);
		if( index <0 ){	// no opset.name == optionSetName, then create a new
			OptionSet newOp = new OptionSet(optionSetName,3);
			newOp.setOption(optionName,price);
			opset.add(newOp);
			return true;
		}
		
		// find opset[index].name == optionSetName, then update it
		return opset.get(index).updateOption(optionName, price);
	}
	
	public synchronized boolean updateOptionSetName(String optionSetName, String newName){
		int index = findOptionSet(optionSetName);
		
		if( index <0 ){		// no opset.name == optionSetName
			return false;
		}
		
		// find opset[index].name == optionSetName, then update it
		opset.get(index).setName(newName);
		
		return true;
	}
	
	public synchronized boolean updateOptionPrice(String optionSetName, String optionName, float price){
		int index = findOptionSet(optionSetName);
		if( index <0 ){		// no opset.name == optionSetName
			return false;	// the Option[] in opset[i] is full and no opset.name == optionSetName
		}
		
		// find opset[index].name == optionSetName, then update it
		return opset.get(index).updateOption(optionName, price);
	}
	
	//Delete
	public synchronized boolean deleteOptionSet(String optionSetName){
		int index = findOptionSet(optionSetName);
		if(index < 0)
			return false;
		
		opset.remove(index);
		return true;
	}
	
	public synchronized boolean deleteOption(String optionSetName, String optionName){
		int index = findOptionSet(optionSetName);
		if(index < 0)
			return false;
		
		return opset.get(index).deleteOption(optionName);
	}
	
	
	//Print and toString automobile
	public synchronized String toString(){
		if(this.name == null || this.name.equals(""))
			return null;
			
		StringBuilder strBuilder = new StringBuilder("Automotive infomation\n");
		strBuilder.append("Model: "+ this.model + "\n");
		strBuilder.append("Make: "+ this.make + "\n");
		strBuilder.append("Name: "+ this.name + "\n");
		strBuilder.append("Base Price: "+ baseprice + "\n");
		
		Iterator<OptionSet> iterOpset = opset.iterator();
		while(iterOpset.hasNext()){
			strBuilder.append("\n" + iterOpset.next().toString());
		}
		
		return strBuilder.toString();
	}
	
	public synchronized void print(){
		System.out.println(toString());
	}
	
	public synchronized void printWithSelectedChoices(){
		if(this.model == null || this.model.equals(""))
			return;
		StringBuilder strBuilder = new StringBuilder("Automotive infomation\n");
		strBuilder.append("Model: "+ this.model + "\n");
		strBuilder.append("Make: "+ this.make + "\n");
		strBuilder.append("Name: "+ this.name + "\n");
		strBuilder.append("Base Price: "+ baseprice + "\n");
		
		Iterator<OptionSet> iterOpset = opset.iterator();
		while(iterOpset.hasNext()){
			iterOpset.next().printSelectedChoice();
		}
	}
	
	// Serialize
	public static String serialize(String writeObjFileName, Automobile automotive){

		FileIO objFile = new FileIO();
		String writtenFileName = objFile.serialize(writeObjFileName, automotive);
		if(writtenFileName == null || writtenFileName.equals("")){
			if (DEBUG) System.out.println("Test result: detected serialization return null");
			return null;
		}
		
		return writtenFileName;
	}
	
	// Deserialize
	public static Automobile deserializeFromFile(String readObjFile){
		
		FileIO objFile = new FileIO();
		Automobile newAutomotive = objFile.deserialize(readObjFile);
		if(newAutomotive == null){
			if (DEBUG) System.out.println("Test result: detected deserialize return null");
			return null;
		}
		
		return newAutomotive;
	}
	
}
