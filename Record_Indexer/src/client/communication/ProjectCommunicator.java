package client.communication;

import client.ClientCommunicatorException;
import shared.communication.*;


/**
 * Project Client Communication class that contains methods to integrate with the server
 * @author Raul Lopez Villalpando
 *
 */
public class ProjectCommunicator {
	
	/**
	 * Requests the server for a list of projects related to the user provided
	 * @param input to validate and get projects
	 * @return A list of projects encapsulated on an object
	 */
	public static Get_Projects_Response getProjects(Validate_User_Parameter input) throws ClientCommunicatorException{
		return null;
	}
	
	/**
	 * Request the server to return an image from the specified project
	 * @param input object representing the project and user
	 * @return A URL encapsulated on an object
	 */
	public static GetSampleImage_Response getSampleImage(User_Project_Parameter input) throws ClientCommunicatorException{
		return null;
	}
	
	/**
	 * Request the server for the fields of an specific project or all fields
	 * @param input describing user and project
	 * @return An object containing all the fields requested
	 */
	public static GetFields_Response getFields(User_Project_Parameter input) throws ClientCommunicatorException{
		return null;
	}
	
	/**
	 * Request the server to search for the specified values and fields
	 * @param input that describes the fields and values to search
	 * @return An object containing an array of tuples
	 */
	public static Search_Response search(Search_Input input) {
		return null;
	}
}