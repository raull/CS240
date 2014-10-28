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
	private String output;
	
	
	
	public Get_Projects_Response(List<Project> projects, String output) {
		this.projects = projects;
		this.output = output;
	}

	public Get_Projects_Response() {
		// TODO Auto-generated constructor stub
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
	 * @return the output
	 */
	public String getOutput() {
		return output;
	}



	/**
	 * @param output the output to set
	 */
	public void setOutput(String output) {
		this.output = output;
	}
	
}
