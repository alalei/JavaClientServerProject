package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import test.CustomException;
import test.ExceptionType;
import model.Automobile;

/**
 * Project 1-1 for 18641 Java Smart Phone
 * 
 * class SourceReader: read from file and build an Automotive object
 * Author: Xuping Lei
 * xlei@andrew.cmu.edu
 * Created at:	Feb.1, 2014
 * 
 * Version: 1.3
 * Modified at:	Feb.14, 2014
 * 
 *  1. writeObject() was renamed as serialize()
 *  2. readObject() was renamed as deserialize()
 *  3. removed SourceReader.buildAutoObject(String filename) to FileIO.buildAutoObject(String filename)
 * 
 * 
 * Version: 1.2
 * Modified at:	Feb.7, 2014
 * 	1. add PriceMissingException handling
 *	2. extend the ability to read and create multiple automobile instances
 *
 */ 

public class FileIO {
	
	/*
	 * public readSQLFile (String filename):
	 * 		return String if succeed;
	 * 		return null if fail.
	 */
	@SuppressWarnings("resource")
	public static String[] readSQLFile (String filename) {
		if (filename == null ) return null;
		
		LinkedList<String> sqlList = new LinkedList<String>();
		FileReader file = null;
	    String aline = new String();
	    StringBuilder strBuilder = null;
	    BufferedReader buffer = null;
				
		/* Loads the file into Properties class */
		try{
			file = new FileReader(filename);
		}catch(Exception e){
			System.out.println("readSQLFile: failed to open file: " + filename);
		}
	  
      try{
    	buffer = new BufferedReader(file);
	    while((aline = buffer.readLine())!=null){	//line != null => !EOF 
	    	/* Filter empty lines */
	    	if(aline.equals("")){
	    		continue;
	    	}
	    	/* Find the new instance beginning tag */
	    	if(aline.equals("NEW")){
	    		strBuilder = new StringBuilder();
	    		continue;
	    	}
	    	/* Finished read data of one Automobile instance, save and continue */
	    	if(aline.equals("END")){
	    		if(strBuilder != null && !(strBuilder.equals("")) ){
	    			sqlList.add(strBuilder.toString());
	    		}
	    		continue;
	    	}
	    	
	    	strBuilder.append(aline + " ");
	    }
      } catch (Exception e){
    	  e.printStackTrace();
    	  return null;
      }
	    
	    String[] result = new String[sqlList.size()];
		int count = result.length;
		for (int i = 0; i < count; i++ ){
			result[i] = sqlList.poll();
		}
		return result;
	}
	
	/*
	 * public buildProperties (String filename):
	 * 		return Properties instance if succeed;
	 * 		return null if fail.
	 */
	public Properties buildProperties (String filename) {
		
		Properties props = null;
				
		/* Loads the file into Properties class */
		try{
			FileInputStream in = new FileInputStream(filename);
			props = new Properties();
			props.load(in);
		} catch (Exception e0) {
			System.out.println("buildProperties: failed to open file: " + filename);
		}

		return props;
	}
	
	
	/*
	 * public buildAutoObject(Properties properties):
	 * 		return Automotive instance if succeed;
	 * 		return null if fail.
	 */
	public Automobile[] buildAutoObject (Properties props) {
		Automobile automobile = null;
		
		String CarMake = props.getProperty("CarMake");
		String CarModel = props.getProperty("CarModel");
		if( (CarModel == null ) || ( CarMake == null ) 
				|| CarModel.equals("") || CarMake.equals(""))
		{ return null; }
		
		String CarName = props.getProperty("CarName");
		String CarBasePrice = props.getProperty("CarBasePrice");
		if( (CarName ==  null ) || CarName.equals("")  ){
			CarName = CarMake + " " + CarModel;
		}
		if( ( CarBasePrice == null) || CarBasePrice.equals("") ){
			CarBasePrice = "0";
		}
		
		automobile = new Automobile(CarName, Float.valueOf(CarBasePrice), 10);
		automobile.setMake(CarMake);
		automobile.setModel(CarModel);
		
		int optionCounter = 0;
		while( true ){
			++ optionCounter;
			String optionItem = "Option" + String.valueOf(optionCounter); // for example: optionItem == "Option1"
			String optionName = props.getProperty(optionItem) ;	// for example: optionName == "Brakes"
			if ( (optionName == null) || (optionName.equals("")) ){
				break;
			}

			char optionValueCounter = 'a';
			while ( optionValueCounter <= 'z') {
				String optionValueItem = "OptionValue" + String.valueOf(optionCounter) + optionValueCounter;
				String optionValueData = props.getProperty(optionValueItem);
				if ( optionValueData == null){
					break;
				}
				else{
					automobile.setOptionSetValue(optionName,optionValueData,0);
				}
				++ optionValueCounter;
			}	
			
		}
		
		return new Automobile[]{automobile};
	}
	
	
	/*
	 * public buildAutoObject(String, Automotive):
	 * 		return Automotive instance if succeed;
	 * 		return null if fail.
	 */
	public Automobile[] buildAutoObject(String filename){

		List<Automobile> automobiles = new LinkedList<Automobile>();
		Automobile automobile = null;
		BufferedReader buffer = null;
		FileReader file;
		
		try {
			// read file from "filename"
			try{
				file = new FileReader(filename);
			}catch(FileNotFoundException eFile){
				throw new CustomException(ExceptionType.WrongFilename);
				// throw new WrongFilenameException();
			}
			
		    buffer = new BufferedReader(file);
		    String aline = new String();
	    
		    while((aline = buffer.readLine())!=null){	//line != null => !EOF {
		    	/* Filter empty lines */
		    	if(aline.equals("")){
		    		continue;
		    	}
		    	
		    	/* Find the new instance beginning tag */
		    	if(aline.equals("NEW Automobile")){
		    		automobile = new Automobile(20);
		    		continue;
		    	}
		    	
		    	/* Finished read data of one Automobile instance, save and continue */
		    	if(aline.equals("END Automobile")){
		    		if(automobile != null){
		    			automobiles.add(automobile);
		    		}
		    		continue;
		    	}
		    	
		    	/* Tokenize each line using StringTokenizer Object */
				StringTokenizer tokens = new StringTokenizer(aline,",");
				int tokenNum=tokens.countTokens();
				if(tokenNum < 3){
					throw new CustomException(ExceptionType.TooFewItemALine);
					//throw new TooManyItemALineException();
				}
				if(tokenNum > 4){
					throw new CustomException(ExceptionType.TooManyItemALine);
					//throw new TooFewItemALineException();
				}
				
				/* the 1st, 2nd elements: */
				String propertyType = tokens.nextToken();
				String propertyName = tokens.nextToken();
				
				if (propertyType.equals("Automobile")){
					/* data format in file when OptionSet.name=="Automobile": 
					 *	(example) Automotive,base price,18445
					 * there should be 3 data items in a line is required 
					 */
					if(tokenNum!=3){
						throw new CustomException("wrong inputfile format: too many data in a line");
					}		
					if(propertyName.equals("name")){
						if(!automobile.setName(tokens.nextToken())){
							throw new CustomException("Failure: Automobile.setName()");
						}
					}
					else if(propertyName.equals("base price")){
						try{
							float baseprice = Float.valueOf(tokens.nextToken());
							if(!automobile.setBasePrice(baseprice)){
								throw new CustomException("Failure: Automobile.setBasePrice()");
							}
						}catch(NumberFormatException eNumFormat){
							throw new CustomException(ExceptionType.PriceMissing);
							//throw new PriceMissingException();
						}
						
					}
					else if (propertyName.equals("make")){
						if(!automobile.setMake(tokens.nextToken())){
							throw new CustomException("Failure: Automobile.setMake()");
						}
					}
					else if (propertyName.equals("model")){
						if(!automobile.setModel(tokens.nextToken())){
							throw new CustomException("Failure: Automobile.setModel()");
						}
					}
					else{
						throw new CustomException("Failure: Property Name of Automotive should be 'name' or 'base price'");
					}
				}
				else if (propertyType.equals("OptionSet")){
					/*
					 * data format in file when OptionSet.name=="OptionSet"
					 * 	example: OptionSet,power moonroof,none,0
					 * there should be 4 data items in a line is required 
					 */
					if(tokenNum!=4){
						throw new CustomException("Wrong inputfile format: too few data in a line");
					}
					String propertyValue = tokens.nextToken();
					try{
						float price = Float.valueOf(tokens.nextToken());
						if(!automobile.setOptionSetValue(propertyName,propertyValue,price)){
							throw new CustomException("Failure: Automotive.setOptionSetValue()");
						}
					}catch(NumberFormatException eNumFormat){
						throw new CustomException(ExceptionType.PriceMissing);
						// throw new PriceMissingException();
					}
				}
				else{
					throw new CustomException(ExceptionType.WrongFileFormat);
					//throw new WrongFileFormatException();
				}				
		    }
		    
		}catch(CustomException customExpt){
			customExpt.printException();
			customExpt.writeException("CustomExceptionLog.txt");
			return null;
		}catch(IOException e1){
			System.out.println("IOException: data reading failed");
			return null;
		}catch(NullPointerException e2){
			System.out.println("NullPointerException: wrong data format");
			return null;
		}finally{
			try{
				if (buffer!= null) buffer.close();
			}catch(IOException e6){
				System.out.println("IOException: buffer closing failed");
				e6.printStackTrace();
			}	
		}
		
		Automobile[] result = new Automobile[automobiles.size()];

		int count = automobiles.size();
		for (int i = 0; i < count; i++ ){
			Automobile temp = ((LinkedList<Automobile>) automobiles).poll();
			result[i] = temp;
		}
		
		return result;
	}
	
	
	//Serialize the object (write objects)
	public String serialize(String filename, Automobile automotive){	

		if(filename == null || filename.equals(""))
			return null;
		if(automotive == null)
			return null;
		
		ObjectOutputStream out = null;
		
		try{
			out = new ObjectOutputStream(new FileOutputStream(filename));
			out.writeObject(automotive);
		}catch(Exception e1){
			System.out.print("Exception: writing object failure");
			return null;
		}finally{
			try{
				if (out!= null) out.close();
			}catch(Exception e3){
				System.out.println("Exception: ObjectOutputStream closing failed");
			}
		}
		
		return filename;
		
	}	
		
		
	//Deserialize from file to object instance (read object)
	public Automobile deserialize(String filename){	
			
		if(filename == null || filename.equals(""))
			return null;
		
		ObjectInputStream in = null;
		Automobile automotive = null;	
		
		try{
			in = new ObjectInputStream(new FileInputStream(filename));
			automotive = (Automobile) in.readObject();
		}catch(Exception e1){
			System.out.println("Exception: reading object failure");
			return null;
		}finally{
			try{
				if (in!= null) in.close();
			}catch(Exception e3){
				System.out.println("Exception: ObjectInputStream closing failed");
			}
		}
		
		return automotive;
	}	

	
}
