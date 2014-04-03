package test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Project 1-1 for 18641 Java Smart Phone
 * 
 * class CustomException: self-defined exception handlers;
 * 					 print exception information in an easier way.
 * Author: Xuping Lei
 * xlei@andrew.cmu.edu
 * 
 */ 

public class CustomException extends Exception {

	/**
	 * class CustomException:
	 *     Custom Exception Handler.
	 */
	private String message;
	private static final long serialVersionUID = 1L;
	private ExceptionType exceptionType;
	
	//Constructors
	public CustomException(){
		super();
		this.message = "CustomException: an undefined exception is detected";
	}
	
	public CustomException( ExceptionType exceptionType){
		super();
		this.exceptionType = exceptionType;
	}
	
	public CustomException(String message){
		super();
		this.exceptionType = ExceptionType.UndefinedType;
		this.message = "CustomException: "+ message;
	}
	
	
	// Set
	public void setMessage(String msg){
		this.message = msg;
	}
	
	// Output
	public void printException(){
		
		//Print exception message to screen
		switch (this.exceptionType){
		case UndefinedType:
			break;
		case WrongFileFormat:
			setMessage("WrongFileFormatException: can not recognize the file format");
			break;
		case WrongFilename:
			setMessage("WrongFilenameException: missing filename or wrong filename");
			break;
		case TooFewItemALine:
			setMessage("TooFewItemALine: too few data items in a line");
			break;
		case TooManyItemALine:
			setMessage("TooManyItemALine: too many data items in a line");
			break;
		case PriceMissing:
			setMessage("PriceMissingException: missing price for Automobile in Text file");
			break; 
		}

		System.out.println("CustomException: "+ this.message);
		
	}
	
	public void writeException(String filepath){
		
		//write exception message to file
		BufferedWriter output = null;
		try{
			output = new BufferedWriter(new FileWriter(filepath));
			output.write(message);	
		}
		catch(Exception e){
			System.out.println("Failure: write exception logs ");
		}
		finally{
			try{
				if(output!=null)output.close();
			}
			catch(IOException ex){
				System.out.println("Failure: close exception logs");
			}		
		}
	}
		
}
