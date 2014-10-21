package shared.communication;

import java.util.List;

import shared.modal.*;

/**
 * Get User Response object so the client and server can communicate
 * @author Raul Lopez Villalpando
 *
 */
public class Get_Projects_Response {
	
	/**
	 * A list of projects found on the Database
	 */
	private List<Project> projects;
	/**
	 * Error Message with the reason of failure
	 */
	private String errorMessage;
	
	
	
	public Get_Projects_Response(List<Project> projects, String errorMessage) {
		this.projects = projects;
		this.errorMessage = errorMessage;
	}



	/**
	 * @return the projects
	 */
	public List<Project> getProjects() {
		return projects;
	}



	/**
	 * @param projects the projects to set
	 */
	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}



	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}



	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
