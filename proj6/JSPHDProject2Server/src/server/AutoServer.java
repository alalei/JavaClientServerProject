package server;

import java.util.Properties;

import model.Automobile;

public interface AutoServer {
	
	Automobile buildCarModelOptions(Properties properties);
	
	boolean provideModelListToClient ();
	
	boolean sendModelToClient (String modelName);

}
