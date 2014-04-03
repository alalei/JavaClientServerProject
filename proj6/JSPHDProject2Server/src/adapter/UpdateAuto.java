package adapter;

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

public interface UpdateAuto {

	boolean updateOptionSetName(String modelName, String optionSetName, String newName);
	
	boolean updateOptionPrice(String modelName, String optionSetName, String optionName, float newPrice);
	
}
