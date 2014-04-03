package database;

import model.Automobile;
import adapter.BuildAuto;

public class DatabaseTestDriver {
	static boolean DEBUG = true;

	public static void main() throws ClassNotFoundException{
		DatabaseHandler dbHandler = new DatabaseHandler();
		dbHandler.registerDriver();
		
		/* Load automobile from file */
		System.out.println("\nTest 1: Open connection");
		BuildAuto builtauto = new BuildAuto();
		Automobile automobile = builtauto.buildAuto("txt", "sqlStatements/testdata2.txt");
		if (automobile != null) {
			automobile.print();
		}

		// Test 1: Open connection
		System.out.println("\nTest 1: Open connection");
		if ( !dbHandler.openConnection() ){
			System.out.println("Test 1: Open connection successfully");
			return;
		}
		
		// Test 2: create tables
		if (DEBUG) System.out.println ("\n---- Test 2: create tables ----\n");
		if (dbHandler.createTable("sqlStatements/createTable.txt")){
			if (DEBUG) System.out.println("create tables successfully\n");
		}
		try{
			Thread.sleep(500);
		} catch ( InterruptedException e){
		}
		
		
		// Test 3: add models into database
		if (DEBUG) System.out.println ("\n---- Test 3: add models ----\n");
		if (dbHandler.addModel(automobile)){
			if (DEBUG) System.out.println("add a model successfully\n");
		}
		try{
			Thread.sleep(500);
		} catch ( InterruptedException e){
		}
		
		
		// Test 4: update models from database
		if (DEBUG) System.out.println ("\n---- Test 4: update models ----\n");
		if (DEBUG) System.out.println ("Read a changed model");
		automobile = builtauto.buildAuto("txt","sqlStatements/updateModel1.txt");
		if (automobile != null) {
			automobile.print();
		}
		if (dbHandler.updateModel(automobile)){
			if (DEBUG) System.out.println("update a model successfully\n");
		}
		try{
			Thread.sleep(500);
		} catch ( InterruptedException e){
		}
		
		// Test 5: delete models from database
		if (DEBUG) System.out.println ("\n---- Test 5: delete models ----\n");
		if (dbHandler.deleteModel(automobile)){
			if (DEBUG) System.out.println("delete a model successfully\n");
		}		
	}
}
