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
	
}