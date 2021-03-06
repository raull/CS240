package server.access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import server.database.Database;
import server.database.DatabaseException;
import shared.modal.Field;
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

				String content = result.getString(2);
				int row = result.getInt(3);
				int column = result.getInt(4);
				int batchId = result.getInt(5);
				int projectId = result.getInt(6);
				
				Value newValue = new Value(content, row, column);
				newValue.setId(result.getInt(1));
				newValue.setBatchId(batchId);
				newValue.setProjectId(projectId);
				
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
				String content = result.getString(2);
				int row = result.getInt(3);
				int column = result.getInt(4);
				int batchId = result.getInt(5);
				int projectId = result.getInt(6);
				
				value = new Value(content, row, column);
				value.setId(valuetId);
				value.setBatchId(batchId);
				value.setProjectId(projectId);
				
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error finding Value: " + e.getLocalizedMessage(), e);
		} 	
		
		return value;
	}
	
	
	public List<String> getValueMatching(Field field, String value) throws DatabaseException {
		PreparedStatement stm = null;
		ResultSet result = null;
		
		ArrayList<String> tuples = new ArrayList<String>();
		
		try {			
			//Set up Query
			String sql = "SELECT batch.id, batch.file_path, value.row FROM value "
					+ "JOIN batch ON UPPER(value.content) = UPPER(?) AND value.column_number = ? "
					+ "AND value.project_id = ? AND value.batch_id = batch.id";
			stm = db.getConnection().prepareStatement(sql);
			stm.setString(1, value);
			stm.setInt(2, field.getColNumber());
			stm.setInt(3, field.getProject_id());

			//Execute Query
			result = stm.executeQuery();
			
			//Handle Response to create new User
			while (result.next()) {
				int batchId = result.getInt(1);
				String filePath = result.getString(2);
				int row = result.getInt(3);
				
				String tuple = batchId + "," + filePath + "," + row + "," + field.getId();
				tuples.add(tuple);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error finding Value: " + e.getLocalizedMessage(), e);
		} 	
		
		return tuples;
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
			String sql = "INSERT INTO value (content, column_number ,row) VALUES (?,?,?)";
			stm = db.getConnection().prepareStatement(sql);
			stm.setString(1, newValue.getContent());
			stm.setInt(2, newValue.getColNumber());
			stm.setInt(3, newValue.getRowNumber());
			
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
			String sql = "UPDATE value SET content = ?, column_number = ?, row = ? WHERE id = ? ";
			stm = db.getConnection().prepareStatement(sql);
			stm.setString(1, value.getContent());
			stm.setInt(2, value.getColNumber());
			stm.setInt(3, value.getRowNumber());
			stm.setInt(4,value.getId());

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
