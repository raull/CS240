package shared.communication;

import java.util.ArrayList;
import java.util.List;

import shared.modal.*;

/**
 * Get User Response object so the client and server can communicate
 * @author Raul Lopez Villalpando
 *
 */
public class Get_Projects_Response {
	
	
	//----------------Instance Fields ----------------------
	/**
	 * A list of projects found on the Database
	 */
	private List<Project> projects = new ArrayList<Project>();
	/**
	 * Error Message with the reason of failure
	 */
	private String output = "FAILED";
	
	
	//----------------Constructors -----------------------
	public Get_Projects_Response(List<Project> projects, String output) {
		this.projects = projects;
		this.output = output;
	}

	public Get_Projects_Response() {
		// TODO Auto-generated constructor stub
	}


	//----------------Getters and Setters ----------------------
	
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
	
	//----------------To String ----------------------
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		if(output.equals("TRUE")) {
			for (Project project : projects) {
				builder.append(project.getId() + "\n");
				builder.append(project.getTitle() + "\n");
			}
		} else {
			builder.append(output + "\n");
		}
		
		return builder.toString();
	}
	
}
