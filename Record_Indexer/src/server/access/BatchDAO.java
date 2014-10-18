package server.access;

import java.util.List;
import shared.modal.*;

/**
 * Batch Data Access Object class. For Accessing and persisting to the database concerning Batch operations
 * @author Raul Lopez Villalpando
 *
 */
public class BatchDAO {
	/**
	 * Find all the Batches in the Database
	 * @return A list with all the Batches in the Database
	 * */
	public List<Batch> findAll() {
		return null;
	}
	
	/**
	 * Find a Batch by providing the Batch's ID
	 * @param id of the batch to find
	 * @return A Batch matching the provided ID
	 */
	public Batch findById(int id) {
		return null;
	}
	
	/**
	 * Find all Batches by providing the Project's ID
	 * @param id of the Project in which the Bath is contained
	 * @return A list of Batches matching the provided Project ID
	 */
	public List<Batch> findAllByProjectId(int projectId) {
		return null;
	}
	
	/**
	 * Insert a new Batch to the Database
	 * @param newBatch, a Batch object representing the new Batch to persist
	 * @return true if the Batch was inserted successfully, otherwise false
	 */
	public boolean insertNewBatch(Batch newBatch) {
		return false;
	}
	
	/**
	 * Update Batch in the Database
	 * @param batch, Batch object containing new information to update
	 * @return true if the Batch was updated successfully, otherwise false
	 */
	public boolean updateBatch(Batch batch) {
		return false;
	}
	
	/**
	 * Delete Batch 
	 * @param Batch object representing the Batch to delete
	 * @return true if the Batch was deleted successfully, otherwise false
	 */
	public boolean deleteBatch(Batch batch) {
		return false;
	}
	
	/**
	 * Delete Batch by providing the Batch's ID
	 * @param id representing the Batch's ID
	 * @return true if the Batch was deleted successfully, otherwise false
	 */
	public boolean deleteBatchWithId(int id) {
		return false;
	}
}
