package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.LinkedList;

import adapter.BuildAuto;
import util.FileIO;
import model.Automobile;

public class DatabaseHandler implements DatabaseCRUD {
	// JDBC driver name and database URL
	private static String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	private static String DB_URL = "jdbc:mysql://localhost/mysql";

	// Database credentials
	private static String USER = "root";
	private static String PASS = ""; 
	
	// Database parameters
    Connection connection = null;
    Statement statement = null;
    ResultSet resultSet = null;
    static DatabaseHandler dbHandler = null;
	
	// Debug configuration
	public final static boolean WARNING = true;
	public final static boolean DEBUG = true;
	public final static boolean DEBUG2 = false;
	
	DatabaseHandler(){
		super();
	}
	
	DatabaseHandler(String JDBC_driver, String DB_url, String username, String password){
		super();
		setParameters(JDBC_driver, DB_url, username, password);
	}
	
	public static void setParameters (String JDBC_driver_Para, String DB_url_Para, String username_Para, String password_Para){
		JDBC_DRIVER = JDBC_driver_Para;
		DB_URL = DB_url_Para;
		USER = username_Para;
		PASS = password_Para; 
	}
	
	//STEP 2: Register JDBC driver
	public boolean registerDriver() throws ClassNotFoundException{
		try{
		    Class.forName(JDBC_DRIVER);
		    if (WARNING) System.out.println("Register JDBC driver ...");
		} catch (ClassNotFoundException e0){
			if (WARNING) System.out.println("Exception: failed to initiate JDBC Driver");
			throw new ClassNotFoundException();
		}
		return true;
	}
	//STEP 3: Open a connection
	public boolean openConnection(){
		try{
		    connection = DriverManager.getConnection(DB_URL,USER,PASS);
		} catch (SQLException e1){
			if (WARNING) System.out.println("Exception: failed to Connect database");
			closeConnection();
			return false;
		}
		if (DEBUG) System.out.println("Open connection successfully: " + connection);
		return true;
	}
	
	//STEP 6: Clean-up environment
	public boolean closeConnection(){
		boolean hasException = false;
		try{
			if (resultSet != null) resultSet.close();
		} catch (Exception e1){
			hasException = true;
			if (DEBUG ) System.out.println("Exception: closing resultSet");
			resultSet = null;
			return false;
		} finally {
			
			try{
				if (statement != null) statement.close();
			} catch (Exception e2){
				hasException = true;
				if (DEBUG ) System.out.println("Exception: closing statement");
				statement = null;
				return false;
			} finally {
				
				try{
					if (connection != null) connection.close();
				} catch (Exception e3){
					hasException = true;
					if (DEBUG ) System.out.println("Exception: closing connection");
					connection = null;
					return false;
				} finally {
					if (DEBUG && hasException) System.out.println("Exception: error when close connection");
				}
			}
		}

		if (DEBUG) System.out.println("Closed connection");
		return true;
	}
	
	
	// create a statement
	public boolean createStatement(){
		if (DEBUG2) System.out.println("Creating statement");
		try{
			if (DEBUG2) System.out.println("statement using connection: "+ connection);
			statement = connection.createStatement();
			if (DEBUG2) System.out.println("statement: "+ statement);
		} catch (SQLException e2) {
			if (WARNING) System.out.println("Exception: failed to create Statement");
			closeConnection();
			return false;
		}
		if (DEBUG2) System.out.println("Creating statement successfully");
		return true;
	}
	
	// STEP 4: Execute a query
	public boolean executeQuery(String sql){
		if (!createStatement())
			return false;
		
		// Execute a query 
		try{
			if (DEBUG) System.out.println("Execute Query SQL: " + sql);
			resultSet = statement.executeQuery(sql);
		} catch (Exception e1) {
			resultSet = null;
			if (DEBUG) System.out.println("Exception: executeQuery()");
			//e1.printStackTrace();
			closeConnection();
			return false;
		}
		return true;
	}
	
	//STEP 4: Execute an update DML
	public int executeUpdate(String sql){
		if (!createStatement())
			return Integer.MIN_VALUE;
		
		int resultInt;
		try{
			if (DEBUG) System.out.println("Executing SQL: " + sql);
			resultInt = statement.executeUpdate(sql,Statement.RETURN_GENERATED_KEYS);
		} catch (SQLFeatureNotSupportedException e1) {
			if (DEBUG) System.out.println("executeUpdate() Exception: method not supported");
			closeConnection();
			return Integer.MIN_VALUE;
		} catch (SQLTimeoutException e1) {
			if (DEBUG) System.out.println("executeUpdate() Exception: timeout");
			closeConnection();
			return Integer.MIN_VALUE;
		} catch (SQLException e) {
			if (DEBUG) System.out.println("executeUpdate() Exception: database access error");
			//e.printStackTrace();
			closeConnection();
			return Integer.MIN_VALUE;
		}
		
		return resultInt;
	}
	
	// check whether connection is valid; if not, create a new
	public boolean checkConnection(){
		try{
			if ( connection == null || !(connection.isValid(10)) ){
				if ( dbHandler.openConnection() ){
					if (DEBUG) System.out.println("checkConnection(): Open connection successfully");
					return true;
				}
				else{
					return false;
				}
			}
		}catch(Exception e0) {
		}
		return true;
	}
	
	// get Primary Key
	public int getPrimaryKey(){
		if (statement == null){
			return Integer.MIN_VALUE;
		}
		try {
			resultSet = statement.getGeneratedKeys();
			if (resultSet.next()) {
				return resultSet.getInt(1); // primary key
		    } else {
		    	if (DEBUG) System.out.println("Warning: no primary key returned");
		    	return Integer.MIN_VALUE;
		    } 
		} catch (Exception e) {
			if (DEBUG) System.out.println("Exception: failed to get primary key");
			return Integer.MIN_VALUE;
		}
	}
	

	// create a new table
	@Override
	public boolean createTable(String filename){
		String[] tableSQLs = FileIO.readSQLFile(filename);
		if (tableSQLs == null || tableSQLs.equals("")){
			return false;
		}
		// wake a valid connection 
		if ( !checkConnection() ){
			if (DEBUG) System.out.println("Error: checkConnection()");
			return false;
		}
		
		// add model into the table 
		for (String tableSQLStatement: tableSQLs){
			if (tableSQLStatement == null) continue;
			if (DEBUG2) System.out.println("Executing: " + tableSQLStatement);
			if (executeUpdate(tableSQLStatement) == Integer.MIN_VALUE){
				if (DEBUG) System.out.println("Exception: failed to add table to the database");
				return false;
			}else{
				if (DEBUG2) System.out.println("create a table successfully\n");
			}
		}
		
		return true;
	}
	
	
	// add a model in table
	@Override
	public boolean addModel(Automobile automobile){
		if (automobile == null){
			return false;
		}
		// generate SQL
		// add to TABLE automobile
		String sql = null;
		sql = "INSERT INTO automobile (name, model, make, baseprice) "
				+ "VALUES ( '"
				+ automobile.getName() + "', '"
				+ automobile.getModel() + "', '"
				+ automobile.getMake() + "', "  +
				+ (int)automobile.getBasePrice()
				+ " );" ;
		if (executeUpdate(sql) == Integer.MIN_VALUE){
			if (DEBUG) System.out.println("Exception: failed to add model to the database");
			return false;
		}
		int modelAtoIncKey = getPrimaryKey();
		if (modelAtoIncKey<0) {
			return false;
		}
		if (DEBUG) System.out.println("modelAtoIncKey: " + modelAtoIncKey);
		
		// wake a valid connection 
		if ( !checkConnection() ){
			if (DEBUG) System.out.println("Error: checkConnection()");
			return false;
		}
		
		String[] optSetsNames = automobile.getOptionSetNames();
		for (int i=0; i < optSetsNames.length; i++){
			// add to TABLE optionset, auto_optset
			sql = "INSERT INTO optionset (name) VALUES ( '"+ optSetsNames[i] + "') ;" ;
			if (executeUpdate(sql) == Integer.MIN_VALUE){
				return false;
			}
			int optsetAutoIncKey = getPrimaryKey();
			if ( optsetAutoIncKey < 0) {
				continue;
			}
			sql = "INSERT INTO auto_optset (auto_id, set_id) VALUES ( "
					+ modelAtoIncKey + ", "+ optsetAutoIncKey + ") ;" ;
			if (executeUpdate(sql) == Integer.MIN_VALUE){
				return false;
			}
			String[] optionNames = automobile.getOptionNames(optSetsNames[i]);
        	// add to TABLE option, set_opt
        	for(String optionName: optionNames){
        		int price = automobile.getOptionPrice(optSetsNames[i], optionName);
        		sql = "INSERT INTO optionunit (name, price) VALUES ( '"+ optionName + "', " + price + ") ;" ;
    			if (executeUpdate(sql) == Integer.MIN_VALUE){
    				return false;
    			}
    			int optionAutoIncKey = getPrimaryKey();
    			sql = "INSERT INTO set_opt (set_id, opt_id) VALUES ( "+ optsetAutoIncKey + ", " + optionAutoIncKey + ") ;" ;
    			if (executeUpdate(sql) == Integer.MIN_VALUE){
    				return false;
    			}
        	}
        }
		return true;
	}
		
	public boolean addModel(String filename){
		String[] tableSQLs = FileIO.readSQLFile(filename);
		
		if (tableSQLs == null || tableSQLs.equals("")){
			return false;
		}
		// wake a valid connection 
		if ( !checkConnection() ){
			if (DEBUG) System.out.println("Error: checkConnection()");
			return false;
		}
		
		// insert model data into the table 
		for (String tableSQLStatement: tableSQLs){
			if (tableSQLStatement == null) continue;
			if (DEBUG2) System.out.println("Executing: " + tableSQLStatement);
			if (executeUpdate(tableSQLStatement) == Integer.MIN_VALUE){
				if (DEBUG) System.out.println("Exception: failed to add model to the database");
				return false;
			}else{
				if (DEBUG2) System.out.println("update a row successfully\n");
			}
		}
		
		// closeConnection();
		return true;
	}
	
	
	// get id
	public int[] getID(String colName){
		if (resultSet == null){
			if (DEBUG) System.out.println("Exception: resultSet is null");
			return null;
		}
		LinkedList<String> IDlist = new LinkedList<String>();
		try{
			while (resultSet.next() ){
				String IDstr = resultSet.getString(colName);
				IDlist.add( IDstr); // primary key
			}
		} catch (Exception e){
			if (DEBUG) System.out.println("Exception: resultSet");
		}
		
		int size =IDlist.size();
		if ( size <= 0 ){
			return null;
		}
		int[] id = new int[size ];
		for (int j =0; j< size ; j++){
			id[j] = Integer.valueOf(IDlist.poll());
		}
		return id;
	}
	
	// delete a model in table
	@Override
	public boolean deleteModel(Automobile automobile){
		if (automobile == null){
			return false;
		}
		// generate SQL 
		// check primary keys TABLE automobile
		String sql = null;
		sql = "SELECT auto_id FROM automobile WHERE name = '" + automobile.getName()+ "' ;" ;
		if (!executeQuery(sql)){
			return false;
		}
		int modelAtoIncKey = getID("auto_id")[0];
		if (modelAtoIncKey <= 0) {
			return false;
		}
		if (DEBUG) System.out.println("modelAtoIncKey: " + modelAtoIncKey);
		
		// wake a valid connection
		if ( !checkConnection() ){
			if (DEBUG) System.out.println("Error: checkConnection()");
			return false;
		}
		
		// check TABLE optionset, auto_optset
		sql = "SELECT set_id FROM auto_optset WHERE auto_id = " + modelAtoIncKey + " ;" ;
		if (!executeQuery(sql)){
			return false;
		}
		int[] optsetAutoIncKey = getID("set_id");
		if ( optsetAutoIncKey == null) {
			return false;
		}
		for (int setIDKey: optsetAutoIncKey){
			sql = "SELECT opt_id FROM set_opt WHERE set_id = " + setIDKey + " ;" ;
			if (!executeQuery(sql)){
				return false;
			}
			int[] optIncKey = getID("opt_id");
			if (optIncKey == null) return false;
			for (int optId: optIncKey){
				if (DEBUG2) System.out.println("\t optId: "+ optId);
				sql = "DELETE FROM set_opt "
						+ "WHERE set_id = " + setIDKey + " AND opt_id = " + optId + ";";
				if ( executeUpdate(sql) == Integer.MIN_VALUE){
					return false;
				}
				sql = "DELETE FROM optionunit "
						+ "WHERE opt_id = " + optId + ";";
				if ( executeUpdate(sql) == Integer.MIN_VALUE){
					return false;
				}
			}
			
			sql = "DELETE FROM auto_optset "
					+ "WHERE set_id = " + setIDKey + ";";
			if ( executeUpdate(sql) == Integer.MIN_VALUE){
				return false;
			}
			sql = "DELETE FROM optionset "
					+ "WHERE set_id = " + setIDKey + ";";
			if ( executeUpdate(sql) == Integer.MIN_VALUE){
				return false;
			}
		}
		
		sql = "DELETE FROM automobile "
				+ "WHERE auto_id = " + modelAtoIncKey + ";";
		if ( executeUpdate(sql) == Integer.MIN_VALUE){
			return false;
		}
        return true;
	}
	
	
	@Override
	public boolean updateModel(Automobile automobile) {
		if (automobile == null){
			return false;
		}
		// wake a valid connection
		if ( !checkConnection() ){
			if (DEBUG) System.out.println("Error: checkConnection()");
			return false;
		}
		
		// generate SQL 
		// check primary keys TABLE automobile
		String sql = "SELECT auto_id FROM automobile WHERE name = '" + automobile.getName()+ "' ;" ;
		if (!executeQuery(sql)){
			return false;
		}
		int modelAtoIncKey = getID("auto_id")[0];
		if (modelAtoIncKey <= 0) {
			return false;
		}
		if (DEBUG) System.out.println("modelAtoIncKey: " + modelAtoIncKey);
		
		// update 
		sql = "UPDATE automobile "
			+ " SET name = '" + automobile.getName()+ "', "
			 	+" model = '" + automobile.getModel() + "', "
				+" make = '" + automobile.getMake() + "', "
				+" baseprice = " + (int) automobile.getBasePrice() 
			+ " WHERE auto_id = " + modelAtoIncKey + ";" ;
		if (executeUpdate(sql) == Integer.MIN_VALUE){
			return false;
		}
		
		// check TABLE optionset, auto_optset
		String[] optSetsNames = automobile.getOptionSetNames();
		for(String setName: optSetsNames){
			// check whether there is such a optionName
			sql = "SELECT A.set_id FROM auto_optset AS A, optionset AS O "
					+ " WHERE A.set_id = O.set_id "
					+ " AND A.auto_id = " + modelAtoIncKey 
					+ " AND O.name = '" + setName + "'"
					+"; " ;
			if (!executeQuery(sql)){
				return false;
			}
			int[] optsetAutoIncKey = getID("set_id");
			if ( optsetAutoIncKey == null) {
				if (DEBUG2) System.out.println("No valid optsetAutoIncKey. OptionSetName: " + setName +". Create a new row.");
				// create new
			}
			else{
				int setIDKey = optsetAutoIncKey[0];
				if (DEBUG2) System.out.println("optsetAutoIncKey: " + setIDKey);
				String[] optionNames = automobile.getOptionNames(setName);
				if (optionNames == null) continue;
			}
		}
        return true;
	}
	
	// delete a model in table
	public boolean deleteModel(String filename){
		String[] tableSQLs = FileIO.readSQLFile(filename);
		boolean hasException = false;
		
		if (tableSQLs == null || tableSQLs.equals("")){
			return false;
		}
		// wake a valid connection 
		if ( !checkConnection() ){
			if (DEBUG) System.out.println("Error: checkConnection()");
			return false;
		}
		
		// delete model data into the table
		for (String tableSQLStatement: tableSQLs){
			if (tableSQLStatement == null) continue;
			if (DEBUG) System.out.println("Executing SQL: " + tableSQLStatement);
			if (executeUpdate(tableSQLStatement) == Integer.MIN_VALUE){
				if (DEBUG) System.out.println("Exception: failed to delete model to the database");
				hasException = true;
			}else{
				if (DEBUG2) System.out.println("delete a row successfully\n");
			}
		}
		
		// closeConnection();
		if (hasException) return false;
		else return true;
	}

	
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
	    
		dbHandler = new DatabaseHandler();
		dbHandler.registerDriver();
		
		// Load automobile from file 
		System.out.println("\nTest 1: Open connection");
		BuildAuto builtauto = new BuildAuto();
		Automobile automobile = builtauto.buildAuto("txt","sqlStatements/testdata2.txt");
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
