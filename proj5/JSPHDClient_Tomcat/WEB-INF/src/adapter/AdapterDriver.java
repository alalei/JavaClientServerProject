package adapter;

import util.FileIO;

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

public class AdapterDriver {
	public static void main(String [] args)
	{
		BuildAuto builtauto = new BuildAuto();
		
		System.out.println("AdapterDriver is running... \n");
		System.out.println("\n---------------");
		System.out.println("Test 1: buildAuto() and printAuto()");
		builtauto.buildAuto("txt","testdata1.txt");
		builtauto.printAuto();
		
		System.out.println("\n---------------");
		System.out.println("Test 2: updateOptionSetName()");
		if (builtauto.updateOptionSetName("ZTW", "color", "colors"))
			System.out.println("Update successfully: 'color'->'colors'");
		if (builtauto.updateOptionSetName("ZTW", "transmission","advanced transmission"))
			System.out.println("Update successfully: 'transmission'->'advanced transmission'");
		if (!builtauto.updateOptionSetName("ZTW","illegal name", "legal name"))
			System.out.println("checked illegal update: 'illegal name'->'legal name'");
		builtauto.printAuto();
		
		System.out.println("\n---------------");
		System.out.println("Test 3: updateOptionSetPrice()");
		System.out.println("read from testdata1.txt");
		builtauto.buildAuto("txt","testdata1.txt");
		builtauto.printAuto();
		if (builtauto.updateOptionPrice("ZTW", "color","black",(float) 2.01))
			System.out.println("Update successfully: black price '0'->'2.01'");
		if (builtauto.updateOptionPrice("ZTW", "transmission","automatic",(float) 1500))
			System.out.println("Update successfully: automatic price '0'->'1500'");
		if (!builtauto.updateOptionPrice("ZTW", "illegal price", "legal price",2))
			System.out.println("checked illegal update: 'illegal price'->'legal price'");
		builtauto.printAuto();
		
		System.out.println("\n---------------");
		System.out.println("Test 4: detect WrongFilenameException():");
		System.out.println("read from legalfilename.txt");
		builtauto.buildAuto("txt","legalfilename.txt");
		
		System.out.println("\n---------------");
		System.out.println("Test 5: detect PriceMissingException():");
		System.out.println("read from wrongDataType.txt");
		builtauto.buildAuto("txt", "wrongDataType.txt");
		
		System.out.println("\n---------------");
		System.out.println("Test 6: builds several models");
		System.out.println("read from testdata3.txt");
		builtauto.buildAuto("txt", "testdata3.txt");
		builtauto.printAuto();
		
		System.out.println("\n---------------");
		System.out.println("Test 7: buildProperties() and buildAutoObject(Properties props)");
		System.out.println("read from propertiesData.properties");
		FileIO sourceReader = new FileIO();
		builtauto.buildAuto("properties", sourceReader.buildProperties("propertiesData.properties"));
		builtauto.printAuto();
	}

}
