package client;

import org.junit.*;

import shared.communication.Validate_User_Parameter;
import shared.communication.Validate_User_Response;
import client.communication.ClientCommunicator;
import static org.junit.Assert.*;

public class ClientUnitTests {
	
	@Test
	public void validateUserTest() throws ClientCommunicatorException{		
		ClientCommunicator communicator = new ClientCommunicator();
		Validate_User_Parameter parameter = new Validate_User_Parameter("sheila", "parker");
		Validate_User_Response response = communicator.validateUser(parameter);
		
		assertEquals("Sheila", response.getFirstName());
		assertEquals("Parker", response.getLastName());
		assertEquals(0, response.getRecordCount());
		assertEquals("TRUE", response.getOutput());
	}
	
	@Test
	public void invalidUserTest() throws ClientCommunicatorException {
		ClientCommunicator communicator = new ClientCommunicator();
		Validate_User_Parameter parameter = new Validate_User_Parameter("notAUsername", "notAPassword");
		Validate_User_Response response = communicator.validateUser(parameter);
		
		assertEquals("FALSE", response.getOutput());
	}
	
	@Test(expected=ClientCommunicatorException.class)
	public void invalidInputUserTest() throws ClientCommunicatorException{
		ClientCommunicator communicator = new ClientCommunicator();
		communicator.validateUser(null);
	}
	

}

