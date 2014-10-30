package client;

import org.junit.*;

import shared.communication.GetSampleImage_Parameter;
import shared.communication.GetSampleImage_Response;
import shared.communication.Get_Projects_Parameter;
import shared.communication.Get_Projects_Response;
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
	
	@Test
	public void getProjectsTest() throws ClientCommunicatorException {
		ClientCommunicator communicator = new ClientCommunicator();
		Get_Projects_Parameter parameter = new Get_Projects_Parameter("sheila", "parker");
		Get_Projects_Response response = communicator.getProjects(parameter);
		
		assertEquals("TRUE", response.getOutput());
		assertEquals(3, response.getProjects().size());
	}
	
	@Test(expected=ClientCommunicatorException.class)
	public void getProjectsInvalidTest() throws ClientCommunicatorException {
		ClientCommunicator communicator = new ClientCommunicator();
		communicator.getProjects(null);
	}
	
	@Test
	public void getSampleImageTest() throws ClientCommunicatorException{
		ClientCommunicator communicator = new ClientCommunicator();
		Get_Projects_Parameter projectsParameter = new Get_Projects_Parameter("sheila", "parker");
		Get_Projects_Response projectsResponse = communicator.getProjects(projectsParameter);
		
		GetSampleImage_Parameter imageParameter = new GetSampleImage_Parameter("sheila", "parker",projectsResponse.getProjects().get(0).getId());
		GetSampleImage_Response imageResponse = communicator.getSampleImage(imageParameter);
		
		assertEquals("TRUE", imageResponse.getOutput());
		assertTrue(!imageResponse.getImageURL().equals(""));
	}
	

}

