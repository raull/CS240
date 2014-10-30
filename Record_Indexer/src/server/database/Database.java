package server.database;

import java.io.File;
import java.sql.*;

import server.access.*;

/**
 * Database class to handle Database SQL actions like connecting and accessing the Database
 * @author Raul Lopez Villalpando
 *
 */
public class Database {
	
	private Connection connection;
	private boolean isTest = false;
	private UserDAO userDAO;
	private ProjectDAO projectDAO;
	private BatchDAO batchDAO;
	private FieldDAO fieldDAO;
	private ValueDAO valueDAO;
	
	//Static Methods
	public static void initialize() throws DatabaseException{
		
		//Import the driver
		try {
			final String driver = "org.sqlite.JDBC";
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new DatabaseException("Could not load driver", e);
		}
		
	}
	
	//Constructors
	public Database() {
		connection = null;
		userDAO = new UserDAO(this);
		projectDAO = new ProjectDAO(this);
		batchDAO = new BatchDAO(this);
		fieldDAO = new FieldDAO(this);
		valueDAO = new ValueDAO(this);
	}
	
	//Methods
	/**
	 * Opens the connection and starts the transaction on the connection
	 * @throws DatabaseException
	 */
	public void startTransaction() throws DatabaseException{
		String dbNameString;
		if (!this.isTest) {
			dbNameString = "db" + File.separator + "indexerDB.sqlite";
		}else { 
			dbNameString = "db" + File.separator + "indexerDBtest.sqlite";
		}
		
		String dbURLString = "jdbc:sqlite:" + dbNameString;
		
		try {
			connection = DriverManager.getConnection(dbURLString);
			
			connection.setAutoCommit(false);
			
		} catch (SQLException e) {
			throw new DatabaseException("Could not start transaction", e);
		}
	}
	
	/**
	 * Performs a commit or rollback on the connection and then closes the connection.
	 * @param commit determines if it is a commit or rollback on the connection
	 * @throws DatabaseException
	 */
	public void endTransaction(Boolean commit) throws DatabaseException{
		
		//Attempt to commit or rollback 
		try {
			
			if (commit) {
				connection.commit();
			}
			else {
				connection.rollback();
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Could not end transaction: " + e.getLocalizedMessage(), e);
		} finally {
			//No matter what happens attempt to close the connection
			try {
				connection.close();
			} catch (SQLException e) {
				throw new DatabaseException("Could not close the connection", e);
			}
		}
		
		connection = null;
	}
	
	//Getters
	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return connection;
	}
	
	/**
	 * @return the userDAO
	 */
	public UserDAO getUserDAO() {
		return userDAO;
	}
	
	/**
	 * @return the projectDAO
	 */
	public ProjectDAO getProjectDAO() {
		return projectDAO;
	}
	
	/**
	 * @return the batchDAO
	 */
	public BatchDAO getBatchDAO() {
		return batchDAO;
	}
	
	/**
	 * @return the fieldDAO
	 */
	public FieldDAO getFieldDAO() {
		return fieldDAO;
	}
	
	/**
	 * @return the valueDAO
	 */
	public ValueDAO getValueDAO() {
		return valueDAO;
	}
	
	/**
	 * @return if either the Database connection is for testing or not
	 */
	public boolean isTest() {
		return this.isTest;
	}
	
	public void isTest(boolean test) {
		this.isTest = test;
	}

}
