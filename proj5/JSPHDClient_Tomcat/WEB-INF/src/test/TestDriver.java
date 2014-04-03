package test;

import adapter.BuildAuto;
import adapter.EditThread;
import scale.EditOption;
import model.Automobile;

/**
 * Project 1-1 for 18641 Java Smart Phone
 * 
 * class TestDriver: used to test entire project.
 * Author: Xuping Lei
 * xlei@andrew.cmu.edu
 * 
 */ 

public class TestDriver {

	static BuildAuto builtauto = null;
	
	public static void main(String [] args)
	{
		System.out.println("TestDriver is running... \n");
		System.out.println("New Tests for version 1.3");
		System.out.println("New Test Case 1: read from testdata3.txt; build multiple Automobile objects at the same time");
		builtauto = new BuildAuto();
		builtauto.buildAuto("txt", "testdata3.txt");
		builtauto.printAuto();
		
		System.out.println("New Test Case 2: run two threads at the same time");
		/* Build Thread 1 */
		EditThread t1 = new BuildAuto();	// using interface
		String argvs[]  = {"A4","color","TestValue_Preferred_color"};
		EditOption a1 = new EditOption(t1, 0, argvs); 
		/* Build Thread 2 */
		EditThread t2 = new BuildAuto();	// using interface
		String argvs2[]  = {"A4","color","TestValue_colors"};
		EditOption a2 = new EditOption(t2, 0, argvs2);
		/* Build Thread 3 */
		EditThread t3 = new BuildAuto();	// using interface
		String argvs3[]  = {"ZTW","transmission","TestValue_Transmissions"};
		EditOption a3 = new EditOption(t3, 0, argvs3);
		/* Build Thread 4 */
		EditThread t4 = new BuildAuto();	// using interface
		String argvs4[]  = {"ZTW","transmission","TestValue_Transmissions", "123"};
		EditOption a4 = new EditOption(t4, 0, argvs4);
		a2.start();
		a1.start();
		a3.start();
		a4.start();
		try {
		    Thread.sleep(1000);
		} catch(InterruptedException ex) {
		}
		builtauto.printAuto();
		
		System.out.println("\n------------------------------");
		System.out.println("\n------------------------------");
		System.out.println("Tests for version 1.1");
		System.out.println("Case 1: read from testdata1.txt; serialize and deserialize same objects");
		Automobile FordZTW = builtauto.getCurrentModel();
		String objFile1 = serializeTest("FordZTW.txt",FordZTW);
		deserializeTest(objFile1);
		
		System.out.println("\n---------------");
		System.out.println("Case 2: read from testdata2.txt; serialize and deserialize same objects");
		builtauto = new BuildAuto();
		builtauto.buildAuto("txt", "testdata2.txt");
		Automobile FordZTW2 = builtauto.getCurrentModel();
		FordZTW2.print();
		String objFile2 = serializeTest("FordZTW2.dat",FordZTW2);
		Automobile newFordZTW2 = deserializeTest(objFile2);
		System.out.println("\n");
		System.out.println("Result after threads processing:");
		builtauto.printAuto();
		
		System.out.println("\n---------------");
		System.out.println("Case 3: Automotive verification - update the instance in case 2 using updateOptionSet() method");
		newFordZTW2.updateOptionSet("color", "yellow", 2);
		newFordZTW2.updateOptionSet("transmission", "automatic", 0);
		newFordZTW2.updateOptionSet("side impact air bags", "none", 0);
		newFordZTW2.updateOptionSet("power moonroof", "none", 0);
		newFordZTW2.print();
		
		System.out.println("\n---------------");
		System.out.println("Case 4: Automotive verification - delete some property in case 3 using deleteOptionSet() method");
		newFordZTW2.deleteOption("brakes/traction control", "Advance Trac");
		newFordZTW2.deleteOptionSet("side impact air bags");
		newFordZTW2.print();
		
		System.out.println("\n---------------");
		System.out.println("Case 5: SourceReader verification - empty data file");
		FordZTW = readFile("testemptydata.txt");	//testemptydata.txt does not exist in disk
		
		System.out.println("\n---------------");
		System.out.println("Case 6: SourceReader verification - too many data in a line");
		FordZTW = readFile("tooManyDataInLine.txt");
		
		System.out.println("\n---------------");
		System.out.println("Case 7: SourceReader verification - too few data in a line");
		FordZTW = readFile("tooFewDataInLine.txt");

		System.out.println("\n---------------");
		System.out.println("Case 8: SourceReader verification - wrong num type input");
		FordZTW = readFile("wrongDataType.txt");

		System.out.println("\n---------------");
		System.out.println("Case 9: ObjectFile verification - serialize with wrong arguments");
		serializeTest("wrongObject",null);
		
		System.out.println("\n---------------");
		System.out.println("Case 10: ObjectFile verification - deserialize with inexistent file");
		deserializeTest("testemptydata.txt");	//testemptydata.txt does not exist in disk
		
	}
	
	
	//Build Automobile Object from a file.
	public static Automobile readFile(String dataFile) {
		
		System.out.println("\n----- Build Automotive objects from a file -----");
		System.out.println("Test result: read file "+ dataFile);
		
		if (builtauto == null )
			return null;
		Automobile  automotive = builtauto.buildAuto("txt", dataFile);
		if(automotive == null){
			System.out.println("Test result: detected SourceReader.buildAutoObject return null\n");
			return null;
		}
		else{
			System.out.println("Test result: finished building object from file\n");
		}
		
		return automotive;
	}

	
	//Test object serialization: write objects into files
	public static String serializeTest(String writeObjFile, Automobile automotive){
		
		System.out.println("\n----- Object Serialization -----");

		String writtenFileName = Automobile.serialize(writeObjFile, automotive);
		if(writtenFileName == null || writtenFileName.equals("")){
			System.out.println("Test result: detected serialization return null");
			return null;
		}
		else{
			System.out.println("Test result: serialized data is saved in " + writtenFileName);
		}
		
		return writtenFileName;
	}
	
	
	//Test object deserialization: read objects from files
	public static Automobile deserializeTest(String readObjFile){		
			
		System.out.println("\n----- Object Deserialization -----");
		
		System.out.println("Test result: read file "+ readObjFile);
		Automobile newAutomotive = Automobile.deserializeFromFile(readObjFile);
		if(newAutomotive == null){
			System.out.println("Test result: detected deserialize return null");
		}
		else{
			System.out.println("Test result: recovered object from file "+ readObjFile);
			
			//Print properties and values
			newAutomotive.print();
		}
		
		return newAutomotive;
	}
		
}
