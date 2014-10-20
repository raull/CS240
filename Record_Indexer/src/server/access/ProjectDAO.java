package server.access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import server.Database;
import server.DatabaseException;
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