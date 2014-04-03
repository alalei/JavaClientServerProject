package adapter;


/**
 * Project 1 for 18641 Java Smart Phone
 * 
 * Author: Xuping Lei
 * xlei@andrew.cmu.edu
 * 
 * Version: 1.3
 * Created at:	Feb.13, 2014
 * 
 */

public interface EditThread {
	
	boolean editOptionSetName(String modelName, String optionSetName, String newName);
	
	boolean editOptionPrice(String modelName, String optionSetName, String optionName, float newPrice);

}
