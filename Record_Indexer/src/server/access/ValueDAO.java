package server.access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import server.Database;
import server.DatabaseException;
import shared.modal.Value;

public class ValueDAO {
	//Instance Fields
	/**
	 * Database object to start and end transactions
	 */
	private Database db;
	
	//-----------------------------------------------------
	//Constructors
	
	public ValueDAO(Database db) {
		this.db = db;
	}
	
	//-----------------------------------------------------
	//Methods

	/**
	 * Find all the Values in the Database
	 * @return A list with all the Values in the Database
	 * */
	public List<Value> getAll() throws DatabaseException {
		PreparedStatement stm = null;
		ResultSet result = null;
				
		ArrayList<Value> valueList = new ArrayList<Value>();
		try {			
			//Set up Query
			String sql = "SELECT * FROM value";
			stm = db.getConnection().prepareStatement(sql);

			//Execute Query
			result = stm.executeQuery();
			
			//Handle Response to create new User
			while (result.next()) {

				String filePath = result.getString(2);
				int status = result.getInt(3);
				
				Value newValue = new Value(filePath, status);
				newValue.setId(result.getInt(1));
				
				valueList.add(newValue);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error getting all Valuees: "+ e.getLocalizedMessage(), e);
		} 
				
		return valueList;
	}
	
	/**
	 * Find a Value by providing the Value's ID
	 * @param id of the value to find
	 * @return A Value matching the provided ID
	 */
	public Value findById(int id) throws DatabaseException {
		PreparedStatement stm = null;
		ResultSet result = null;
		
		Value value = null;
		
		try {			
			//Set up Query
			String sql = "SELECT * FROM value WHERE id = ?";
			stm = db.getConnection().prepareStatement(sql);
			stm.setInt(1, id);

			//Execute Query
			result = stm.executeQuery();
			
			//Handle Response to create new User
			while (result.next()) {
				int valuetId = result.getInt(1);
				String filePath = result.getString(2);
				int status = result.getInt(3);
				
				value = new Value(filePath, status);
				value.setId(valuetId);
				
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error finding Value: " + e.getLocalizedMessage(), e);
		} 	
		
		return value;
	}
	
	/**
	 * Insert a new Value to the Database
	 * @param newValue, a Value object representing the new Value to persist
	 * @return true if the Value was inserted successfully, otherwise false
	 */
	public boolean insertNewValue(Value newValue) throws DatabaseException {
		PreparedStatement stm = null;
		ResultSet result = null;
		Statement keyStm = null; 
				
		try {
			//Set up Query
			String sql = "INSERT INTO value (content, row) VALUES (?,?)";
			stm = db.getConnection().prepareStatement(sql);
			stm.setString(1, newValue.getContent());
			stm.setInt(2, newValue.getRowNumber());
			
			//Execute Query and handle the response to get back the ID
			if (stm.executeUpdate() == 1) {
				keyStm = db.getConnection().createStatement();
				result = keyStm.executeQuery("SELECT last_insert_rowid()");
				result.next();
				newValue.setId(result.getInt(1));
				
				return true;
			} else {
				return false;
			}
						
		} catch (SQLException e) {
			throw new DatabaseException("Error adding Value: " + e.getLocalizedMessage(), e);
		} 
	}
	
	/**
	 * Update Value in the Database
	 * @param value, Value object containing new information to update
	 * @return true if the Value was updated successfully, otherwise false
	 */
	public boolean updateValue(Value value) throws DatabaseException {
		PreparedStatement stm = null;
		
		try {

			//Set up Query
			String sql = "UPDATE value SET content = ?, row = ? WHERE id = ? ";
			stm = db.getConnection().prepareStatement(sql);
			stm.setString(1, value.getContent());
			stm.setInt(2, value.getRowNumber());
			stm.setInt(3,value.getId());

			//Execute Query
			if (stm.executeUpdate() == 1) {
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error updating Value: " + e.getLocalizedMessage(), e);
		} 
	}
	
	/**
	 * Delete Value by providing the Value's ID
	 * @param id representing the Value's ID
	 * @return true if the Value was deleted successfully, otherwise false
	 */
	public boolean deleteValueWithId(int id) throws DatabaseException {
		PreparedStatement stm = null;
		
		try {
			//Set up Query
			String sql = "DELETE FROM value WHERE id = ? ";
			stm = db.getConnection().prepareStatement(sql);
			stm.setInt(1, id);

			//Execute Query
			if (stm.executeUpdate() == 1) {
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error deleting Value: " + e.getLocalizedMessage(), e);
		}
	}

}
