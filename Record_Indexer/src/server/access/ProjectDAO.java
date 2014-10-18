package server.access;

import java.util.List;
import shared.modal.*;

/**
 * Project Data Access Object class. For Accessing and persisting to the database concerning Project operations
 * @author Raul Lopez Villalpando
 *
 */
public class ProjectDAO {

	/**
	 * Find all the Projects in the Database
	 * @return A list with all the Projects in the Database
	 * */
	public List<Project> findAll() {
		return null;
	}
	
	/**
	 * Find a Project by providing the Project's ID
	 * @param id of the project to find
	 * @return A Project matching the provided ID
	 */
	public Project findById(int id) {
		return null;
	}
	
	/**
	 * Find a Project by providing the Project's title
	 * @param title of the Project to find
	 * @return A Project matching the provided title
	 */
	public Project findByTitle(String title) {
		return null;
	}
	
	/**
	 * Insert a new Project to the Database
	 * @param newProject, a Project object representing the new Project to persist
	 * @return true if the Project was inserted successfully, otherwise false
	 */
	public boolean insertNewProject(Project newProject) {
		return false;
	}
	
	/**
	 * Update Project in the Database
	 * @param project, Project object containing new information to update
	 * @return true if the Project was updated successfully, otherwise false
	 */
	public boolean updateProject(Project project) {
		return false;
	}
	
	/**
	 * Delete Project 
	 * @param Project object representing the Project to delete
	 * @return true if the Project was deleted successfully, otherwise false
	 */
	public boolean deleteProject(Project project) {
		return false;
	}
	
	/**
	 * Delete Project by providing the Project's ID
	 * @param id representing the Project's ID
	 * @return true if the Project was deleted successfully, otherwise false
	 */
	public boolean deleteProjectWithId(int id) {
		return false;
	}
}
