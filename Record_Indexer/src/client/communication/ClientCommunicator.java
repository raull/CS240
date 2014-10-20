package client.communication;

import shared.communication.*;
import client.ClientCommunicatorException;

public class ClientCommunicator {

	/**
	 * Request the server for an available Batch from a Project
	 * @param input describing the user and project
	 * @return The available batch for the user
	 */
	public static DownloadBatch_Response downloadBatch(User_Project_Parameter input) throws ClientCommunicatorException {
		return null;
	}
	
	/**
	 * Submit a batch with a set of values
	 * @param input describing the user and the values to submit
	 * @return A string with an error message, otherwise is null
	 */
	public static String submitBatch(SubmitBatch_Parameter input) throws ClientCommunicatorException{
		return null;
	}
	
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
	
	/**
	 * Validates if the username and password match and exist in the Database
	 * @param input object to validate
	 * @return An object encapsulating the validation 
	 */
	public static Validate_User_Response validateUser(Validate_User_Parameter input) throws ClientCommunicatorException {
		return null;
	}
}
