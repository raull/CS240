package server.access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import server.database.Database;
import server.database.DatabaseException;
import shared.modal.*;

/**
 * Batch Data Access Object class. For Accessing and persisting to the database concerning Batch operations
 * @author Raul Lopez Villalpando
 *
 */
public class BatchDAO {
	
	
	//Instance Fields
	/**
	 * Database object to start and end transactions
	 */
	private Database db;
	
	//-----------------------------------------------------
	//Constructors
	
	public BatchDAO(Database db) {
		this.db = db;
	}
	
	//-----------------------------------------------------
	//Methods

	/**
	 * Find all the Batches in the Database
	 * @return A list with all the Batches in the Database
	 * */
	public List<Batch> getAll() throws DatabaseException {
		PreparedStatement stm = null;
		ResultSet result = null;
				
		ArrayList<Batch> batchList = new ArrayList<Batch>();
		try {			
			//Set up Query
			String sql = "SELECT * FROM batch";
			stm = db.getConnection().prepareStatement(sql);

			//Execute Query
			result = stm.executeQuery();
			
			//Handle Response to create new User
			while (result.next()) {

				String filePath = result.getString(2);
				int status = result.getInt(3);
				int projectId = result.getInt(4);
				
				Batch newBatch = new Batch(filePath, status);
				newBatch.setId(result.getInt(1));
				newBatch.setProjectId(projectId);
				
				batchList.add(newBatch);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error getting all Batches: "+ e.getLocalizedMessage(), e);
		} 
				
		return batchList;
	}
	
	/**
	 * Find a Batch by providing the Batch's ID
	 * @param id of the batch to find
	 * @return A Batch matching the provided ID
	 */
	public Batch findById(int id) throws DatabaseException {
		PreparedStatement stm = null;
		ResultSet result = null;
		
		Batch batch = null;
		
		try {			
			//Set up Query
			String sql = "SELECT * FROM batch WHERE id = ?";
			stm = db.getConnection().prepareStatement(sql);
			stm.setInt(1, id);

			//Execute Query
			result = stm.executeQuery();
			
			//Handle Response to create new User
			while (result.next()) {
				int batchtId = result.getInt(1);
				String filePath = result.getString(2);
				int status = result.getInt(3);
				int projectId = result.getInt(4);
				
				batch = new Batch(filePath, status);
				batch.setId(batchtId);
				batch.setProjectId(projectId);
				
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error finding Batch: " + e.getLocalizedMessage(), e);
		} 	
		
		return batch;
	}
	
	/**
	 * Insert a new Batch to the Database
	 * @param newBatch, a Batch object representing the new Batch to persist
	 * @return true if the Batch was inserted successfully, otherwise false
	 */
	public boolean insertNewBatch(Batch newBatch) throws DatabaseException {
		PreparedStatement stm = null;
		ResultSet result = null;
		Statement keyStm = null; 
				
		try {
			//Set up Query
			String sql = "INSERT INTO batch (file_Path, status) VALUES (?,?)";
			stm = db.getConnection().prepareStatement(sql);
			stm.setString(1, newBatch.getFilePath());
			stm.setInt(2, newBatch.getStatus());
			
			//Execute Query and handle the response to get back the ID
			if (stm.executeUpdate() == 1) {
				keyStm = db.getConnection().createStatement();
				result = keyStm.executeQuery("SELECT last_insert_rowid()");
				result.next();
				newBatch.setId(result.getInt(1));
				
				return true;
			} else {
				return false;
			}
						
		} catch (SQLException e) {
			throw new DatabaseException("Error adding Batch: " + e.getLocalizedMessage(), e);
		} 
	}
	
	/**
	 * Insert a new Batch into the specified Project. It creates the relationship between the two on the database.However does not update the Objects provided
	 * @param value The value to insert
	 * @param batch The batch to insert the value to
	 * @return True if the value was inserted successfully, false otherwise
	 * @throws DatabaseException
	 */
	public boolean insertValueIntoBatch(Value value, Batch batch) throws DatabaseException {
		PreparedStatement stm = null;
		
		try {

			//Set up Query
			String sql = "UPDATE value SET batch_id = ?, project_id = ? WHERE id = ? ";
			stm = db.getConnection().prepareStatement(sql);
			stm.setInt(1, batch.getId());
			stm.setInt(2, batch.getProjectId());
			stm.setInt(3, value.getId());

			//Execute Query
			if (stm.executeUpdate() == 1) {
				value.setBatchId(batch.getId());
				value.setProjectId(batch.getProjectId());
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error inserting Value into Batch: " + e.getLocalizedMessage(), e);
		} 
	}
	
	
	/**
	 * Get a list of all the Values on the provided Batch
	 * @param batch The batch that contains the Values
	 * @return  A list of all the Batches's values.
	 * @throws DatabaseException
	 */
	public List<Value> getAllValuesForBatch(Batch batch)  throws DatabaseException {
		if (batch.getId() <= 0 ) {
			throw new DatabaseException("Invalud ID for Batch");
		}
		
		PreparedStatement stm = null;
		ResultSet result = null;
				
		ArrayList<Value> valueList = new ArrayList<Value>();
		try {			
			//Set up Query
			String sql = "SELECT * FROM value WHERE batch_id = ?";
			stm = db.getConnection().prepareStatement(sql);
			stm.setInt(1, batch.getId());

			//Execute Query
			result = stm.executeQuery();
			
			//Handle Response to create new User
			while (result.next()) {

				String content = result.getString(2);
				int row = result.getInt(3);
				int column = result.getInt(4);
				
				Value newValue = new Value(content, row, column);
				newValue.setId(result.getInt(1));
				
				valueList.add(newValue);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error getting all Values for Batch: "+ e.getLocalizedMessage(), e);
		} 
				
		return valueList;
	}
	
	/**
	 * Update Batch in the Database
	 * @param batch, Batch object containing new information to update
	 * @return true if the Batch was updated successfully, otherwise false
	 */
	public boolean updateBatch(Batch batch) throws DatabaseException {
		PreparedStatement stm = null;
		
		try {

			//Set up Query
			String sql = "UPDATE batch SET file_path = ?, status = ? WHERE id = ? ";
			stm = db.getConnection().prepareStatement(sql);
			stm.setString(1, batch.getFilePath());
			stm.setInt(2, batch.getStatus());
			stm.setInt(3,batch.getId());

			//Execute Query
			if (stm.executeUpdate() == 1) {
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error updating Batch: " + e.getLocalizedMessage(), e);
		} 
	}
	
	/**
	 * Delete Batch by providing the Batch's ID
	 * @param id representing the Batch's ID
	 * @return true if the Batch was deleted successfully, otherwise false
	 */
	public boolean deleteBatchWithId(int id) throws DatabaseException {
		PreparedStatement stm = null;
		
		try {
			//Set up Query
			String sql = "DELETE FROM batch WHERE id = ? ";
			stm = db.getConnection().prepareStatement(sql);
			stm.setInt(1, id);

			//Execute Query
			if (stm.executeUpdate() == 1) {
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error deleting Batch: " + e.getLocalizedMessage(), e);
		}
	}
}
