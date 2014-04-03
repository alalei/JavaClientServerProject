package net;
import java.net.*;
import java.util.Properties;
import java.io.*;

import model.Automobile;


public class DefaultSocketClient extends Thread 
			implements SocketClientInterface, SocketClientConstants, SocketClientProtocolSignals {
	
	private BufferedReader reader;
	private BufferedWriter writer;
	ObjectOutputStream objectOutStream;
	ObjectInputStream objectInStream;
	private Socket sock;
	private String strHost;
	private int iPort;
	private InetAddress inetAddress; 
	private static final boolean DEBUG = true;
	private static final boolean DEBUG2_PART = true;
	
	/* configure server */
	public DefaultSocketClient(String strHost, int iPort) {
		setPort (iPort);
		setHost (strHost);
		try{
			this.sock = new Socket(this.strHost, this.iPort);
		this.inetAddress = sock.getInetAddress();
		} catch (IOException socketError) {
			if (DEBUG) System.err.println("Unable to connect to " + strHost);
		}
	}
	
	public DefaultSocketClient(Socket socket) {
		this.sock = socket;
		this.inetAddress = this.sock.getInetAddress();
		this.iPort = this.sock.getPort();
	}
	
	public void setHost(String strHost){
		this.strHost = strHost;
	}
	
	public void setPort(int iPort){
		this.iPort = iPort;
	}
	
	/* Thread: run() */
	public void run(){
		if (openConnection()){
			handleSession();
			closeSession();
		}
	}

	/* open connection */
	public boolean openConnection(){
		if (sock != null) return true;
		
		try {
			sock = new Socket(inetAddress, iPort);
		} catch (IOException socketError){
			if (DEBUG) System.err.println("Unable to connect to " + strHost);
			return false;
		}
		
		return true;
		
	}
	
	/* 
	 * readRequest(), readProperties, sendRequest(), sendProperties:
	 * Connection IO , added by xlei
	 */
	public String readRequest(){
		String request = null;
		
		try {
			reader = new BufferedReader ( new InputStreamReader(sock.getInputStream()) );
			request = reader.readLine();
			if (DEBUG && DEBUG2_PART) System.out.println("received message: " + request);
			reader = null;
		} catch (Exception e){
			if (DEBUG) System.err.println("Unable to obtain stream from " + strHost);
			return null;
		}
		
		return request;
	} 
	
	public boolean sendRequest(String request){
		request += "\r\n";
		try {
			 writer = new BufferedWriter (new OutputStreamWriter (sock.getOutputStream()) );
			 writer.write(request, 0, request.length());
			 writer.flush();
			 writer = null;
		} catch (Exception e) {
			if (DEBUG) System.out.println ("sendRequest(): Error responsing to " + strHost);
			return false;
		}
		
		if (DEBUG && DEBUG2_PART) System.out.println("sent message: " + request);
		return true;
	}
	
	public Object readObject(String objectType) {
		Object properties = null;	
		
		try {
			objectInStream = new ObjectInputStream( sock.getInputStream() );
			if (objectType.equals("Properties")){
				properties = (Properties) objectInStream.readObject();
			}
			else if (objectType.equals("Automobile")){
				properties = (Automobile) objectInStream.readObject();
			}
			else{
				properties = objectInStream.readObject();
			}
		} catch (Exception e1) {
			System.out.println("Exception: reading object failure");
			return null;
		}
		
		return properties;
	}
	
	public boolean sendObject(Object properties){
		try {
			objectOutStream = new ObjectOutputStream ( sock.getOutputStream() );
			objectOutStream.writeObject(properties);
			objectOutStream.flush();
		} catch (Exception e) {
			if (DEBUG) System.out.println ("Error writing to " + strHost);
			return false;
		}
		
		return true;
	}
	
	public void closeSession(){
		
		if (objectOutStream != null){
			try{
				if (objectOutStream!= null){
					objectOutStream.close();
				}
			}catch(Exception e3){
				if (DEBUG) System.out.println("Exception: ObjectOutputStream closing failed");
			}
		}
		if (objectInStream != null){
			try{
				if (objectInStream != null){
					objectInStream.close();
				}
			}catch(Exception e3){
				if (DEBUG) System.out.println("Exception: ObjectInputStream closing failed");
			}
		}
		try {
			writer = null;
			reader = null;
			sock.close();
			sock = null;
			if (DEBUG) System.out.println ("Closed connection");
		} catch (IOException e){
			if (DEBUG) System.err.println ("Error closing socket to " + strHost);
		}
	}
	
	// Methods below are not used
	public void sendOutput(String strOutput){
		try {
			writer.write(strOutput, 0, strOutput.length());
		} catch (IOException e){
		if (DEBUG) System.out.println ("Error writing to " + strHost);
		}
	}
	
	public void handleSession(){
		String strInput = "";
		if (DEBUG) System.out.println ("Handling session with " + strHost + ":" + iPort);
		
		try {
			while ( (strInput = reader.readLine()) != null){
				handleInput (strInput);
			}
		} catch (IOException e){
			if (DEBUG) System.out.println ("Handling session with " + strHost + ":" + iPort);
		}
	}
	
	public void handleInput(String strInput){
		System.out.println(strInput);
	}

}
