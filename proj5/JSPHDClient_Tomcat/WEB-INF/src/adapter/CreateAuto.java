package adapter;



import model.Automobile;

/**
 * Project 1 for 18641 Java Smart Phone
 * 
 * Author: Xuping Lei
 * xlei@andrew.cmu.edu
 * 
 * Version: 1.4
 * Created at:	Feb.7, 2014
 * Modified at: Feb.19. 2014
 */

public interface CreateAuto {
	
	//Automobile buildAuto(String dataFile);
	Automobile buildAuto(String fileType, Object object);
	
	void printAuto();

}
