package client.facade;


import client.communication.ClientCommunicator;
import client.communication.ClientCommunicatorException;
import shared.communication.Validate_User_Parameter;
import shared.communication.Validate_User_Response;
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
	
	//Getters and Setters
	
	
	public static void setClientCommunicator(ClientCommunicator communicator) {
		ClientFacade.communicator = communicator;
	}
}
