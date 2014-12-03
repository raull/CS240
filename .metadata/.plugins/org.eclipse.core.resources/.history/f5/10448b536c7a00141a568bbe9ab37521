package client.facade;


import java.util.ArrayList;
import java.util.List;

import client.communication.ClientCommunicator;
import client.communication.ClientCommunicatorException;
import shared.communication.DownloadBatch_Parameter;
import shared.communication.DownloadBatch_Response;
import shared.communication.GetSampleImage_Parameter;
import shared.communication.GetSampleImage_Response;
import shared.communication.Get_Projects_Parameter;
import shared.communication.Get_Projects_Response;
import shared.communication.Validate_User_Parameter;
import shared.communication.Validate_User_Response;
import shared.modal.Batch;
import shared.modal.Field;
import shared.modal.Project;
import shared.modal.User;

public class ClientFacade {

	private static ClientCommunicator communicator;
	
	private static String username;
	private static String password;
	
	
	public static User validateUser(String username, String password) throws ClientCommunicatorException {
		
		try {
			
			//Validate User
			Validate_User_Parameter param = new Validate_User_Parameter(username, password);
			Validate_User_Response response = communicator.validateUser(param);
			if (response.getOutput().equals("FALSE")) {
				return null;
			} else if (response.getOutput().equals("FAILED")) {
				throw new ClientCommunicatorException("Failed, please try again later");
			} else {
				ClientFacade.username = username;
				ClientFacade.password = password;
				
				User user = new User(username, password, response.getFirstName(), response.getLastName(), null, response.getRecordCount());
				
				return user;
			}
		} catch (ClientCommunicatorException e) {
			throw e;
		}
		
	}
	
	public static List<Project> getProjects() throws ClientCommunicatorException {
		try {
			
			Get_Projects_Parameter parameter = new Get_Projects_Parameter(ClientFacade.username, ClientFacade.password);
			Get_Projects_Response response = communicator.getProjects(parameter);
			
			if (response.getOutput().equals("FAILED")) {
				throw new ClientCommunicatorException("Failed, please try again later");
			} else {
				return response.getProjects();
			}
			
		} catch (ClientCommunicatorException e) {
			throw e;
		}
		
	}
	
	public static String getSampleImage(Project project) throws ClientCommunicatorException{
		
		try {
			
			GetSampleImage_Parameter parameter = new GetSampleImage_Parameter(ClientFacade.username, ClientFacade.password, project.getId());
			GetSampleImage_Response response = communicator.getSampleImage(parameter);
			
			if (response.getOutput().equals("FAILED")) {
				throw new ClientCommunicatorException("Failed, please try again later");
			} else {
				return response.getImageURL();
			}
			
		} catch (ClientCommunicatorException e) {
			throw e;
		}
		
	}
	
	public static Batch downloadBatch(Project project) throws ClientCommunicatorException {
		
		try {
			
			DownloadBatch_Parameter parameter = new DownloadBatch_Parameter(ClientFacade.username, ClientFacade.password, project.getId());
			DownloadBatch_Response response = communicator.downloadBatch(parameter);
			
			if (response.getOutput().equals("FAILED")) {
				throw new ClientCommunicatorException("Failed, please try again later");
			} else {
				
				ArrayList<Field> fields = (ArrayList<Field>)response.getFields();
				project.setFields(fields);
				return response.getBatch();
			}
			
		} catch (ClientCommunicatorException e) {
			throw e;
		}
	}
	
	//Getters and Setters
	
	public static void setClientCommunicator(ClientCommunicator communicator) {
		ClientFacade.communicator = communicator;
	}
	
}
