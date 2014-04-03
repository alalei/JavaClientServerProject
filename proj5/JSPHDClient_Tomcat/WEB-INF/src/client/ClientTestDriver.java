package client;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

public class ClientTestDriver {
	static boolean DEBUG = true;
	
	public static void main(String [] args){
		
		if (DEBUG) System.out.println("Client TestDriver is running...\n");
		
		/* set host */
		String strLocalHost = "";
		try{
			strLocalHost = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e){
			System.err.println ("Unable to find local host");
		}
		
		/* set port */
		int port = 8060;
		if (args.length == 1){
			try {
				port = Integer.parseInt(args[0]);
			} catch (Exception e) {
				System.err.println("Usage: java Server <port_number>");
				System.err.println("<port_number> should be an integer");
				port = 8060;
			}
		}
		
		CarModelOptionsIO testClient = new CarModelOptionsIO(strLocalHost, port);
		
		/*
		 * Test1: build several models on server via client 
		 */
		// Build objects 1
		if (DEBUG) System.out.println("\n--- Test 1: build several models on server via client\n");
		Properties prop = testClient.buildPropertiesFromFile("propertiesData.properties");
		if ( prop != null) System.out.println("Built properties from file");
		else{
			System.out.println("Failed to build properties ");
		}
		// Connect to server*/
		if (DEBUG) System.out.print("Client connected to " + strLocalHost +":" + port + " (default port)\n");
		testClient.sendPropertiesToServer(prop);
		prop = null;
		try{
			Thread.sleep(500);
		} catch (Exception e0){
			if (DEBUG) System.out.print("Thread.sleep(): exception");
		}
		
		// Build objects 2
		prop = testClient.buildPropertiesFromFile("propertiesData2.properties");
		if ( prop != null) System.out.println("Built properties from file");
		else{
			System.out.println("Failed to build properties ");
		}
		// Connect to server
		if (DEBUG) System.out.print("Client connected to " + strLocalHost +":" + port + " (default port)\n");
		testClient.sendPropertiesToServer(prop);
		
		testClient = null;
		
		/*
		 * Test2: query models on server via client
		 */
		if (DEBUG) System.out.println("\n--- Test 2: query models on server via client\n");
		SelectCarOption testClient2 = new SelectCarOption(strLocalHost, port);
		if (DEBUG) System.out.println("Client connected to " + strLocalHost +":" + port + " (default port)");
		if (DEBUG) System.out.println("Get Models from Server");
		testClient2.queryModelsfromServer();
		
		String modelName = "Prius";
		testClient2.getModelfromServer(modelName);
		if (DEBUG) System.out.println("\nSelected Model from the Server: " + strLocalHost);
		testClient2.printModel();
		
		/*
		 * Test3: update options of a selected model
		 */
		if (DEBUG) System.out.println("\n--- Test 3: update options of a selected model\n");
		testClient2.updateModelOnLocal("Prius","Transmission","Automatic",(float)400);
		testClient2.updateModelOnServer("Prius","Brakes","ABS",(float)800);
		// Display the selected options for a class
		System.out.println("\nSelected Model: ");
		testClient2.printModel();
		System.out.println("\nSelected Options: ");
		testClient2.printModelWithSelectedChoice();
		
		/*
		 * Test4: update options of a unselected model
		 */
		if (DEBUG) System.out.println("\n--- Test 4: update options of a unselected model\n");
		testClient2.updateModelOnLocal("ZTW 2.0","Transmission","Automatic",(float)400);
		testClient2.updateModelOnLocal("ZTW 2.0","Side Impact Air Bags","None",(float) 0);
		testClient2.updateModelOnLocal("ZTW 2.0","Power Moonroof","Selected",(float) 300);
		testClient2.updateModelOnServer("ZTW 2.0","Brakes","ABS",(float)700);
		// Display the selected options for a class
		if (DEBUG) System.out.println("\nSelected Model: ");
		testClient2.printModel();
		if (DEBUG) System.out.println("\nSelected Options: ");
		testClient2.printModelWithSelectedChoice();
		
	}

}
