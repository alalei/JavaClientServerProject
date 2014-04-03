package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import adapter.BuildAuto;



public class SocketServer {

	private static BuildAuto builtauto = new BuildAuto();
	private static ServerSocket srvSock;
	private static final boolean DEBUG = true;

	public static void main(String [] args){
		/* Default port */
		int port = 8060;
		
		if (args.length == 1){
			try {
				port = Integer.parseInt(args[0]);
			} catch (Exception e) {
				System.out.println("Usage: java Server <port_number>");
				System.out.println("<port_number> should be an integer");
			}
		}

		/* Initial models */
		builtauto.buildAuto("txt", "testdata3.txt");
		System.out.println("Initiate preset models:");
		if(DEBUG) {
			builtauto.printAuto();
		}
		
		/*
		 * Create a socket to accept() client connections. This combines
		 * socket(), bind() and listen() into one call. Any connection
		 * attempts before this are terminated with RST.
		 */
		try {
			srvSock = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println("Unable to listen on port " + port);
			System.exit(1);
		}
		System.out.println("The server is running at port: " + port);
		
		while (true) {
			Socket clientSock = null;
			try {
				/*
				 * Get a sock for further communication with the client. This
				 * socket is sure for this client. Further connections are still
				 * accepted on srvSock
				 */
				clientSock = srvSock.accept();
				if (DEBUG){
					System.out.println("Accpeted new connection from "+ clientSock.getInetAddress()
							+ ":" + clientSock.getPort());
				} 
			} catch (IOException e) {
				if (DEBUG) System.out.println("Server: IOException detected");
				continue;
			}
			
			BuildCarModelOptions buildCarModelOptions = new BuildCarModelOptions(clientSock, builtauto);
			Thread buildCarModelThread = new Thread(buildCarModelOptions);
			buildCarModelThread.start();
		}
		
	}
	
}
