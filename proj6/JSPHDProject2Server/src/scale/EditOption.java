package scale;

import adapter.EditThread;

/**
 * Project 1 for 18641 Java Smart Phone
 * 
 * Author: Xuping Lei
 * xlei@andrew.cmu.edu
 * 
 * Version: 1.3
 * Created at:	Feb.13, 2014
 * 
 * class EditOption: Edit Options for a given model set in its own thread. Internally access the model set.
 * 
 */

public class EditOption extends Thread{
	protected EditThread editThread;
	private int opTag;
	protected String[] argvs;
	
	public EditOption(EditThread t1, int operationParameter, String [] argvs){
		editThread = t1;
		opTag = operationParameter;
		this.argvs = argvs;
	}

	public void run(){
		
		switch(opTag)
		{
			case 0: 
				// String modelName, String optionSetName, String newName
				editThread.editOptionSetName(argvs[0] , argvs[1], argvs[2]);
				break;
				
			case 1:
				// String modelName, String optionSetName, String optionName, float newPrice);
				editThread.editOptionPrice(argvs[0] , argvs[1], argvs[2], Float.parseFloat(argvs[3]));
				break;
				
		}
		
		// print thread and task information
		String outStr = "[ ";
		for(String str: argvs){
			outStr += str+" ";
		}
		outStr += "]";
		
		System.out.println("EditOption Thread: "+ getId() + 
			". Finished with arguments-" + outStr); 
	}

}
