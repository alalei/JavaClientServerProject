package model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Project 1-1 for 18641 Java Smart Phone
 * 
 * class OptionSet: class to represent properties of Automotive model;
 * 					have inner class Option inside.
 * Author: Xuping Lei
 * xlei@andrew.cmu.edu
 * Created at:	Feb.1, 2014
 *
 * Version: 1.3
 * Modified at:	Feb.14, 2014
 *  1. change the type of "opt" from Option[] to ArrayList<Option>[]
 *  
 */ 

class OptionSet implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	private ArrayList<Option> opt;
	private String name;
	protected Option choice = null;
	
	//Constructors
	protected OptionSet(String name, int size){
		opt = new ArrayList<Option>(size);
		this.name = name;
	}
	
	
	//Getters
	protected String[] getOptionNames(){ // return the name of option
		if(opt == null)
			return null;
		
		String[] OptionNames = new String[opt.size()];
		int i = 0;
		for (Option optUnit: opt){
			OptionNames[i] = optUnit.getName();
			i++;
		}
		return OptionNames;
	}
	
	protected String getOptionChoice(){ // return the name of option
		if(choice == null)
			return null;
		
		return choice.name;
	}
	
	protected int getOptionChoicePrice(){ // return the name of option
		if(choice == null)
			return Integer.MIN_VALUE;
		
		return (int)choice.price;
	}
	
	protected int getOptionPrice(String optionName){
		int index = findOption(optionName);
		if(index<0 || index>=opt.size()) 
			return Integer.MIN_VALUE;;
		if(opt==null) 
			return Integer.MIN_VALUE;;
		
		return (int) opt.get(index).getPrice();
	}
	
	protected String getName(){
		return name;
	}
	
	protected Option getOption(int index){
		if(index<0 || index>=opt.size()) 
			return null;
		if(opt==null) 
			return null;
		
		return opt.get(index);
	}
	
	//Find
	protected int findOption (String name){
		if(opt==null)
			return -1;

		for(int i=0, len = opt.size(); i < len; i++){
			if (opt.get(i)!=null && opt.get(i).getName().equals(name))
				return i;
		}
		
		return -1;
	}
	
	//Setter
	protected void setOptionChoice(String optionName){
		if(optionName == null || optionName.equals(""))
			return;
		
    	int optionIndex = findOption(optionName);
		if(optionIndex < 0){	// no option.name == optionName
			choice = null;
		}
		else{
			choice = getOption(optionIndex);
		}
	}
	
	protected boolean setName(String name){
		if(name==null || name.equals(""))
			return false;
		
		this.name = name;
		return true;
	}
	
	protected boolean setOption(String optionName, float price){
		// setOption could be used to set several options in continuous time
		if(opt == null) 
			return false;
		if(optionName == null || optionName.equals(""))
			return false;

		int index = findOption(optionName);
		if( index < 0 ){ // the Option does not exist, create a new
			opt.add(new Option(optionName,price));
			return true;
		}
		else{
			opt.get(index).setPrice(price);
			return true;
		}
	}
	
	//Update
	protected boolean updateOption(String optionName, float price){
		// each time update a option, the whole opt[] will be replaced
		deleteAllOption();
		return setOption(optionName, price);		
	}
	
	//Delete
	protected boolean deleteOption (String optionName){
		
		int index = findOption(optionName);
		if(index < 0)
			return false;
		
		opt.remove(index);
		return true;
	}
	
	protected boolean deleteAllOption (){
		
		opt = new ArrayList<Option>(3);
		return true;
	}
	
	
	//Print and toString
	public String toString(){
		
		StringBuilder strBuilder = new StringBuilder("OptionSet: "+name+"\n");
		if (choice != null){
			strBuilder.append("Selected choice: " + choice.name +"\n");
		}
		Iterator<Option> iterOp = opt.iterator();
		while(iterOp.hasNext()){
			strBuilder.append(iterOp.next().toString());
		}
		
		return strBuilder.toString();
	}
	
	protected void print(){
		System.out.println(toString());
	}
	
	protected void printSelectedChoice(){
		StringBuilder strBuilder = new StringBuilder("OptionSet: "+name+"\n");
		if (choice != null){
			strBuilder.append("Selected choice - " + choice.toString());
		}
		else{
			strBuilder.append("Selected choice - null");
		}
		
		System.out.println (strBuilder.toString());
	}
	
	
	/*
	 * Inner class Option
	 * It stands for a value (String) and pertinent price of OptionSet
	 */
	
	class Option implements java.io.Serializable {	//values

		private static final long serialVersionUID = 1L;
		private String name;
		private float price;
		
		protected Option(){
			this.name ="";
			this.price = 0;
		}
		
		protected Option(String name, float price){
			this.name = name;
			this.price = price;
		}
		
		//Getters
		protected String getName(){
			return name;
		}
		
		protected float getPrice(){
			return price;
		}
		
		//Setters
		protected boolean setName(String name){
			if(name == null || name.equals(""))
				return false;
			
			this.name = name;
			return true;
		}
		
		protected boolean setPrice(float price){
			this.price = price;
			return true;
		}
		
		//Print Option
		public String toString(){
			StringBuilder strBuilder = new StringBuilder("Option name: "+name+"\tprice: "+price+"\n");
			return strBuilder.toString();
		}
		protected void print(){
			System.out.println(toString());
		}
	}
	
}
