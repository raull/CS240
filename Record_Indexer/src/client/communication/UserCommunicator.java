package client.communication;

import client.ClientCommunicatorException;
import shared.communication.*;

/**
 * User Client Communication class that contains methods to integrate with the server
 * @author Raul Lopez Villalpando
 *
 */
public class UserCommunicator {

	
	/**
	 * Validates if the username and password match and exist in the Database
	 * @param input object to validate
	 * @return An object encapsulating the validation 
	 */
	public static Validate_User_Response validateUser(Validate_User_Parameter input) throws ClientCommunicatorException {
		return null;
	}
}