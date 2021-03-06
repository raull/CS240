package client.communication;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import shared.communication.*;

public class ClientCommunicator {
	
	private static String SERVER_HOST = "localhost";
	private static int SERVER_PORT = 8080;
	private static String URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
	private static final String HTTP_POST = "POST";

	private XStream xmlStream;

	public ClientCommunicator() {
		xmlStream = new XStream(new DomDriver());
	}
	
	public ClientCommunicator(String host, String port) {
		xmlStream = new XStream(new DomDriver());
		ClientCommunicator.SERVER_HOST = host;
		ClientCommunicator.SERVER_PORT = Integer.parseInt(port);
		URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
	}
	
	
	/**
	 * Request the server for an available Batch from a Project
	 * @param input describing the user and project
	 * @return The available batch for the user
	 */
	public DownloadBatch_Response downloadBatch(DownloadBatch_Parameter input) throws ClientCommunicatorException {
		if (input == null) {
			throw new ClientCommunicatorException("Client Error: Input cannot be null");
		}
		DownloadBatch_Response response = (DownloadBatch_Response)doPost("/DownloadBatch", input);
		response.attachURLToOutput(URL_PREFIX);
		return response; 
	}
	
	/**
	 * Submit a batch with a set of values
	 * @param input describing the user and the values to submit
	 * @return A string with an error message, otherwise is null
	 */
	public SubmitBatch_Response submitBatch(SubmitBatch_Parameter input) throws ClientCommunicatorException{
		if (input == null) {
			throw new ClientCommunicatorException("Client Error: Input cannot be null");
		}
		
		return (SubmitBatch_Response)doPost("/SubmitBatch", input);
	}
	
	/**
	 * Requests the server for a list of projects related to the user provided
	 * @param input to validate and get projects
	 * @return A list of projects encapsulated on an object
	 */
	public Get_Projects_Response getProjects(Get_Projects_Parameter input) throws ClientCommunicatorException{
		if (input == null) {
			throw new ClientCommunicatorException("Client Error: Input cannot be null");
		}
		
		return (Get_Projects_Response)doPost("/GetProjects", input);
	}
	
	/**
	 * Request the server to return an image from the specified project
	 * @param input object representing the project and user
	 * @return A URL encapsulated on an object
	 */
	public GetSampleImage_Response getSampleImage(GetSampleImage_Parameter input) throws ClientCommunicatorException{
		if (input == null) {
			throw new ClientCommunicatorException("Client Error: Input cannot be null");
		}
		GetSampleImage_Response response = (GetSampleImage_Response)doPost("/GetSampleImage", input);
		response.setImageURL(URL_PREFIX + File.separator + response.getImageURL());
		return response;
	}
	
	/**
	 * Request the server for the fields of an specific project or all fields
	 * @param input describing user and project
	 * @return An object containing all the fields requested
	 */
	public GetFields_Response getFields(GetFields_Parameter input) throws ClientCommunicatorException{
		if (input == null) {
			throw new ClientCommunicatorException("Client Error: Input cannot be null");
		}
		return (GetFields_Response)doPost("/GetFields", input);
	}
	
	/**
	 * Request the server to search for the specified values and fields
	 * @param input that describes the fields and values to search
	 * @return An object containing an array of tuples
	 */
	public Search_Response search(Search_Input input) throws ClientCommunicatorException {
		if (input == null) {
			throw new ClientCommunicatorException("Client Error: Input cannot be null");
		}
		
		Search_Response response = (Search_Response)doPost("/Search", input);
		response.attachURLToImagePath(URL_PREFIX);
		return response;
	}
	
	/**
	 * Validates if the username and password match and exist in the Database
	 * @param input object to validate
	 * @return An object encapsulating the validation 
	 */
	public Validate_User_Response validateUser(Validate_User_Parameter input) throws ClientCommunicatorException {		
		if (input == null) {
			throw new ClientCommunicatorException("Client Error: Input cannot be null");
		}
		
		return (Validate_User_Response)doPost("/ValidateUser", input);
	}
	
	//------------------------HTTP Methods---------------------
	
	private Object doPost(String urlPath, Object postData) throws ClientCommunicatorException {
		try {
			URL url = new URL(URL_PREFIX + urlPath);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(HTTP_POST);
			connection.setDoOutput(true);
			connection.connect();
			xmlStream.toXML(postData, connection.getOutputStream());
			connection.getOutputStream().close();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				Object result = xmlStream.fromXML(connection.getInputStream());
				return result;
			} else {
				throw new ClientCommunicatorException(String.format("doPost failed: %s (http code %d)",
						urlPath, connection.getResponseCode()));
			}
		}
		catch (IOException e) {
			throw new ClientCommunicatorException(String.format("doPost failed: %s", e.getMessage()), e);
		}
	}
	
	
}
