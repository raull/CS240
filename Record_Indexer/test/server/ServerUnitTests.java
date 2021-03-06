package server;

import org.junit.* ;

import server.database.Database;
import shared.DataImporter;
import shared.communication.DownloadBatch_Parameter;
import shared.communication.DownloadBatch_Response;
import shared.communication.GetFields_Parameter;
import shared.communication.GetFields_Response;
import shared.communication.GetSampleImage_Parameter;
import shared.communication.GetSampleImage_Response;
import shared.communication.Get_Projects_Parameter;
import shared.communication.Get_Projects_Response;
import shared.communication.Search_Input;
import shared.communication.Search_Response;
import shared.communication.SubmitBatch_Parameter;
import shared.communication.SubmitBatch_Response;
import shared.communication.Validate_User_Parameter;
import shared.communication.Validate_User_Response;
import shared.modal.Field;
import client.communication.ClientCommunicator;
import client.communication.ClientCommunicatorException;
import static org.junit.Assert.* ;

public class ServerUnitTests {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		//Import the Database Controller and initialize a Database instance
		Database.initialize();
		Database db = new Database();
		
		//Create an importer
		DataImporter importer = new DataImporter(db);
		//Import Data from file
		importer.importData("../record-indexer/Records/Records.xml");
	}
	
	@Before
	public void setup() {
	}
	
	@After
	public void teardown() {
	}
	
	@Test
	public void validateUserTest() throws ClientCommunicatorException{		
		ClientCommunicator communicator = new ClientCommunicator();
		Validate_User_Parameter parameter = new Validate_User_Parameter("sheila", "parker");
		Validate_User_Response response = communicator.validateUser(parameter);
		
		assertEquals("Sheila", response.getFirstName());
		assertEquals("Parker", response.getLastName());
		assertEquals("TRUE", response.getOutput());
		
		//Invalid User
		parameter = new Validate_User_Parameter("sheil", "parker");
		response = communicator.validateUser(parameter);
		assertEquals("FALSE", response.getOutput());
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
		
		GetSampleImage_Parameter imageParameter = new GetSampleImage_Parameter("sheila", "parker", projectsResponse.getProjects().get(0).getId());
		GetSampleImage_Response imageResponse = communicator.getSampleImage(imageParameter);
		
		assertEquals("TRUE", imageResponse.getOutput());
		assertTrue(!imageResponse.getImageURL().equals(""));
	}
	
	@Test
	public void downloadBatchTest() throws ClientCommunicatorException {
		ClientCommunicator communicator = new ClientCommunicator();
		Get_Projects_Parameter projectsParameter = new Get_Projects_Parameter("sheila", "parker");
		Get_Projects_Response projectsResponse = communicator.getProjects(projectsParameter);
		
		DownloadBatch_Parameter parameter = new DownloadBatch_Parameter("sheila", "parker", projectsResponse.getProjects().get(0).getId());
		DownloadBatch_Response response = communicator.downloadBatch(parameter);
		
		assertEquals("TRUE", response.getOutput());
		assertEquals(1,response.getBatch().getStatus());
		assertTrue(response.getFields().size() != 0);
	}
	
	@Test
	public void getFieldsFromProjectTest() throws ClientCommunicatorException {
		ClientCommunicator communicator = new ClientCommunicator();
		Get_Projects_Parameter projectsParameter = new Get_Projects_Parameter("sheila", "parker");
		Get_Projects_Response projectsResponse = communicator.getProjects(projectsParameter);
		
		int projectId = projectsResponse.getProjects().get(0).getId();
		
		GetFields_Parameter parameter = new GetFields_Parameter("sheila", "parker", projectId);
		GetFields_Response response = communicator.getFields(parameter);
		
		assertEquals("TRUE", response.getOutput());
		assertTrue(response.getFields().size() != 0);
		
		for (Field field : response.getFields()) {
			assertTrue(field.getProject_id() == projectId);
		}
		
		//Invalid Project Id
		parameter = new GetFields_Parameter("sheila", "parker", 0);
		response = communicator.getFields(parameter);
		assertEquals("FAILED", response.getOutput());

	}
	
	@Test 
	public void getAllFieldsTest() throws ClientCommunicatorException {
		ClientCommunicator communicator = new ClientCommunicator();

		GetFields_Parameter parameter = new GetFields_Parameter("sheila", "parker", -1);
		GetFields_Response response = communicator.getFields(parameter);
		
		assertEquals("TRUE", response.getOutput());
		assertEquals(13, response.getFields().size());
	}
	
	@Test
	public void submitBatchTest() throws ClientCommunicatorException {
		ClientCommunicator communicator = new ClientCommunicator();
		
		//Doesn't own the batch 
		SubmitBatch_Parameter parameter = new SubmitBatch_Parameter("test1", "test1", "Beltran,Cinthia,Female,23;Gutierrez,Moi,Male,27;Lopez,Raul,Male,23;Lopez,Raul,Male,23;Villalpando,Martha,Female,43;,,,;,,,;,,,", 121);
		SubmitBatch_Response response = communicator.submitBatch(parameter);
		
		assertEquals("FAILED", response.getOutput());
		
		//Missing a Value
		parameter = new SubmitBatch_Parameter("sheila", "parker", "Beltran,Cinthia,Female;Gutierrez,Moi,Male,27;Lopez,Raul,Male,23;Lopez,Raul,Male,23;Villalpando,Martha,Female,43;,,,;,,,;,,,", 121);
		response = communicator.submitBatch(parameter);
		
		assertEquals("FAILED", response.getOutput());
		
		//Missing a Record
		parameter = new SubmitBatch_Parameter("sheila", "parker", "Beltran,Cinthia,Female,23;Gutierrez,Moi,Male,27;Lopez,Raul,Male,23;Lopez,Raul,Male,23;Villalpando,Martha,Female,43;,,,;,,,", 121);
		response = communicator.submitBatch(parameter);
		
		assertEquals("FAILED", response.getOutput());
		
		//Should work
		parameter = new SubmitBatch_Parameter("sheila", "parker", "Beltran,Cinthia,Female,23;Gutierrez,Moi,Male,27;Lopez,Raul,Male,23;Lopez,Raul,Male,23;Villalpando,Martha,Female,43;,,,;,,,;,,,", 121);
		response = communicator.submitBatch(parameter);
		
		assertEquals("TRUE", response.getOutput());
		
	}
	
	@Test
	public void searchTest() throws ClientCommunicatorException {
		ClientCommunicator communicator = new ClientCommunicator();

		Search_Input parameter = new Search_Input("sheila", "parker", "40,41", "Raul,Cinthia");
		Search_Response response = communicator.search(parameter);
		
		assertEquals("TRUE", response.getOutput());
		assertEquals(3, response.getTuples().size());
	}
	
	// Main Method
	
	public static void main(String[] args) {
		
		String[] testClasses = new String[] {
				"server.access.UserDAOTest",
				"server.access.ProjectDAOTest",
				"server.access.FieldDAOTest",
				"server.access.BatchDAOTest",
				"server.access.ValueDAOTest",
				"server.ServerUnitTests"
		};

		org.junit.runner.JUnitCore.main(testClasses);
	}
	
	
}

