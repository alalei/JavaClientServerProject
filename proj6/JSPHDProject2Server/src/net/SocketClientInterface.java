package net;

public interface SocketClientInterface {
	
	boolean openConnection();
	
	void handleSession();
	
	void closeSession();
	
}
