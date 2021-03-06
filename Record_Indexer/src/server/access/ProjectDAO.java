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
 * Project Data Access Object class. For Accessing and persisting to the database concerning Project operations
 * @author Raul Lopez Villalpando
 *
 */
public class ProjectDAO {
	
	//Instance Fields
	/**
	 * Database object to start and end transactions
	 */
	private Database db;
	
	//-----------------------------------------------------
	//Constructors
	
	public ProjectDAO(Database db) {
		this.db = db;
	}
	
	//-----------------------------------------------------
	//Methods
	
	/**
	 * Find all the Projects in the Database
	 * @return A list with all the Projects in the Database
	 * */
	public List<Project> getAll() throws DatabaseException{
		
		PreparedStatement stm = null;
		ResultSet result = null;
				
		ArrayList<Project> projectList = new ArrayList<Project>();
		try {			
			//Set up Query
			String sql = "SELECT * FROM project";
			stm = db.getConnection().prepareStatement(sql);

			//Execute Query
			result = stm.executeQuery();
			
			//Handle Response to create new User
			while (result.next()) {

				String title = result.getString(2);
				int recordsPerBatch = result.getInt(3);
				int firstYCood = result.getInt(4);
				int recordHeight = result.getInt(5);
				
				Project newProject = new Project(title, recordsPerBatch, firstYCood, recordHeight);
				newProject.setId(result.getInt(1));
				
				projectList.add(newProject);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error getting all Projects: "+ e.getLocalizedMessage(), e);
		} 
				
		return projectList;
	}
	
	/**
	 * Find a Project by providing the Project's ID
	 * @param id of the project to find
	 * @return A Project matching the provided ID
	 */
	public Project findById(int id) throws DatabaseException{
		PreparedStatement stm = null;
		ResultSet result = null;
		
		Project project = null;
		
		try {			
			//Set up Query
			String sql = "SELECT * FROM project WHERE id = ?";
			stm = db.getConnection().prepareStatement(sql);
			stm.setInt(1, id);

			//Execute Query
			result = stm.executeQuery();
			
			//Handle Response to create new User
			while (result.next()) {
				int projectId = result.getInt(1);
				String title = result.getString(2);
				int recordsPerBatch = result.getInt(3);
				int firstYCoord = result.getInt(4);
				int recordHeight = result.getInt(5);
				
				project = new Project(title, recordsPerBatch, firstYCoord, recordHeight);
				project.setId(projectId);
				
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error finding Project: " + e.getLocalizedMessage(), e);
		} 	
		
		return project;
	}
	
	
	/**
	 * Insert a new Project to the Database
	 * @param newProject, a Project object representing the new Project to persist
	 * @return true if the Project was inserted successfully, otherwise false
	 */
	public boolean insertNewProject(Project newProject) throws DatabaseException {
		
		PreparedStatement stm = null;
		ResultSet result = null;
		Statement keyStm = null; 
				
		try {
			//Set up Query
			String sql = "INSERT INTO project (title, records_image_count, first_y_coord, record_height) VALUES (?,?,?,?)";
			stm = db.getConnection().prepareStatement(sql);
			stm.setString(1, newProject.getTitle());
			stm.setInt(2, newProject.getRecordsPerBatch());
			stm.setInt(3, newProject.getFirstYCood());
			stm.setInt(4, newProject.getRecordHeight());
			
			//Execute Query and handle the response to get back the ID
			if (stm.executeUpdate() == 1) {
				keyStm = db.getConnection().createStatement();
				result = keyStm.executeQuery("SELECT last_insert_rowid()");
				result.next();
				newProject.setId(result.getInt(1));
				
				return true;
			} else {
				return false;
			}
						
		} catch (SQLException e) {
			throw new DatabaseException("Error adding Project: " + e.getLocalizedMessage(), e);
		} 
				
	}
	
	/**
	 * Insert a new Field into the specified Project. It creates the relationship between the two on the database.However does not update the Objects provided
	 * @param field The Field to add into the new Project (ID cannot be zero or negative) 
	 * @param project The project the new field will be added to (ID cannot be zero or negative)
	 * @return True if the field was added successfully, otherwise false
	 * @throws DatabaseException
	 */
	public boolean insertNewFieldIntoProject(Field field, Project project) throws DatabaseException {
		
		//Check if the objects send have a valid ID
		if (project.getId() <= 0) {
			throw new DatabaseException("Project ID is not valid");
		} 
		if (field.getId() <= 0) {
			throw new DatabaseException("Field ID is not valid");
		}
		
		PreparedStatement stm = null;

		try {

			//Set up Query
			String sql = "UPDATE field SET project_id = ? WHERE id = ? ";
			stm = db.getConnection().prepareStatement(sql);
			stm.setInt(1, project.getId());
			stm.setInt(2, field.getId());

			//Execute Query
			if (stm.executeUpdate() == 1) {
				field.setProject_id(project.getId());
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error adding new Field to Project: " + e.getLocalizedMessage(), e);
		} 
		
	}
	
	/**
	 * Insert a new Batch into the specified Project. It creates the relationship between the two on the database.However does not update the Objects provided
	 * @param batch The batch to be inserted
	 * @param project The project where the batch will be inserted
	 * @return true if the batch was inserted successfully, false otherwise
	 * @throws DatabaseException 
	 */
	public boolean insertNewBatchIntoProject(Batch batch, Project project) throws DatabaseException {
		PreparedStatement stm = null;
		
		try {

			//Set up Query
			String sql = "UPDATE batch SET project_id = ? WHERE id = ? ";
			stm = db.getConnection().prepareStatement(sql);
			stm.setInt(1, project.getId());
			stm.setInt(2,batch.getId());

			//Execute Query
			if (stm.executeUpdate() == 1) {
				batch.setProjectId(project.getId());
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error inserting Batch into Project: " + e.getLocalizedMessage(), e);
		} 
	}
	
	/**
	 * Get available batch from project and assign it to the user
	 * @param project project where the batch belongs to
	 * @param user user to assign the batch to
	 * @return the available batch from the given project
	 */
	public Batch getBatch(Project project) throws DatabaseException {
		if (project.getId() <= 0) {
			throw new DatabaseException("Invalid ID for Project");
		}
		
		
		PreparedStatement stm = null;
		ResultSet result = null;
				
		ArrayList<Batch> batchList = new ArrayList<Batch>();
		try {			
			//Set up Query
			String sql = "SELECT * FROM batch WHERE status = '0' AND project_id = ?";
			stm = db.getConnection().prepareStatement(sql);
			stm.setInt(1, project.getId());

			//Execute Query
			result = stm.executeQuery();
			
			//Handle Response to get the batches
			while (result.next()) {

				String filePath = result.getString(2);
				int status = result.getInt(3);
				int projectId = result.getInt(4);
				
				Batch newBatch = new Batch(filePath, status);
				newBatch.setId(result.getInt(1));
				newBatch.setProjectId(projectId);
				
				batchList.add(newBatch);
			}
			
			if(batchList.size() == 0) {
				return null;
			}			
			
		} catch (SQLException e) {
			throw new DatabaseException("Error getting all Batches for Project: "+ e.getLocalizedMessage(), e);
		} 
				
		return batchList.get(0);
	}
	
	/**
	 * Get a list of all the Batches on the provided Project
	 * @param project The project that contains the Batches
	 * @return  A list of all the Project's batches.
	 * @throws DatabaseException
	 */
	public List<Batch> getAllBatchesForProjet(Project project)  throws DatabaseException {
		if (project.getId() <= 0 ) {
			throw new DatabaseException("Invalid ID for Project");
		}
		
		PreparedStatement stm = null;
		ResultSet result = null;
				
		ArrayList<Batch> batchList = new ArrayList<Batch>();
		try {			
			//Set up Query
			String sql = "SELECT * FROM batch WHERE project_id = ?";
			stm = db.getConnection().prepareStatement(sql);
			stm.setInt(1, project.getId());

			//Execute Query
			result = stm.executeQuery();
			
			//Handle Response to get all batches
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
			throw new DatabaseException("Error getting all Batches for Project: "+ e.getLocalizedMessage(), e);
		} 
				
		return batchList;
	}
	
	/**
	 * Get a list of all the Fields on the provided Project
	 * @param project The project that contains the Batches
	 * @return A list of all the Project's batches.
	 * @throws DatabaseException
	 */
	public List<Field> getAllFieldsForProject(Project project) throws DatabaseException {
		if (project.getId() <= 0 ) {
			throw new DatabaseException("Inavlid ID for Project");
		}
		
		PreparedStatement stm = null;
		ResultSet result = null;
				
		ArrayList<Field> fieldList = new ArrayList<Field>();
		try {			
			//Set up Query
			String sql = "SELECT * FROM field WHERE project_id = ?";
			stm = db.getConnection().prepareStatement(sql);
			stm.setInt(1, project.getId());

			//Execute Query
			result = stm.executeQuery();
			
			//Handle Response to create new User
			while (result.next()) {

				String title = result.getString(2);
				int xCoord = result.getInt(3);
				int colNumber = result.getInt(4);
				int width = result.getInt(5);
				String helpHTML = result.getString(6);
				String knownData = result.getString(7);
				int projectId = result.getInt(8);
				
				Field newField = new Field(title, xCoord, colNumber, width, helpHTML, knownData);
				newField.setId(result.getInt(1));
				newField.setProject_id(projectId);
				
				fieldList.add(newField);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error getting all Fields for Project: "+ e.getLocalizedMessage(), e);
		} 
				
		return fieldList;
	}
	
	/**
	 * Update Project in the Database
	 * @param project, Project object containing new information to update
	 * @return true if the Project was updated successfully, otherwise false
	 */
	public boolean updateProject(Project project) throws DatabaseException{
		PreparedStatement stm = null;
		
		try {

			//Set up Query
			String sql = "UPDATE project SET title = ?, records_image_count = ?, first_y_coord = ?, record_height = ? WHERE id = ? ";
			stm = db.getConnection().prepareStatement(sql);
			stm.setString(1, project.getTitle());
			stm.setInt(2, project.getRecordsPerBatch());
			stm.setInt(3, project.getFirstYCood());
			stm.setInt(4, project.getRecordHeight());
			stm.setInt(5,project.getId());

			//Execute Query
			if (stm.executeUpdate() == 1) {
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error updating Project: " + e.getLocalizedMessage(), e);
		} 
	}
	
	/**
	 * Delete Project by providing the Project's ID
	 * @param id representing the Project's ID
	 * @return true if the Project was deleted successfully, otherwise false
	 */
	public boolean deleteProjectWithId(int id) throws DatabaseException{
		PreparedStatement stm = null;
		
		try {
			//Set up Query
			String sql = "DELETE FROM project WHERE id = ? ";
			stm = db.getConnection().prepareStatement(sql);
			stm.setInt(1, id);

			//Execute Query
			if (stm.executeUpdate() == 1) {
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error deleting Project: " + e.getLocalizedMessage(), e);
		}
	}
}
