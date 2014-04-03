package database;

import model.Automobile;

public interface DatabaseCRUD {
	
	public boolean createTable(String filename);
	
	public boolean addModel(Automobile automobile);
	
	public boolean deleteModel(Automobile automobile);
	
	public boolean updateModel(Automobile automobile);

}
