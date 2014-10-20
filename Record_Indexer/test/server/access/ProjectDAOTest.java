package server.access;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.Database;
import server.DatabaseException;
import shared.modal.Project;

public class ProjectDAOTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		Database.initialize();
	}
	
	@AfterClass
	public static void afterClassTeardown() throws Exception {
		return;
	}
	
	
	private Database db;
	private ProjectDAO projectDAO;
	
	@Before
	public void setUp() throws Exception {
		db = new Database();
		db.isTest(true);
		db.startTransaction();
		
		List<Project> allProjects = db.getProjectDAO().getAll();
		
		//Clear out User table
		for (Project project : allProjects) {
			db.getProjectDAO().deleteProjectWithId(project.getId());
		}
		
		db.endTransaction(true);
		
		//Prepare Database for test case
		db = new Database();
		db.isTest(true);
		db.startTransaction();
		projectDAO = db.getProjectDAO();
	}
	
	@After
	public void teardown() throws Exception{
		db.endTransaction(true);
		db = null;
		projectDAO = null;
	}

	@Test
	public void testGetAll() throws DatabaseException{
		
		List<Project> allProjectList = projectDAO.getAll();
		assertEquals(0, allProjectList.size());
	}
	
	@Test
	public void testAdd() throws DatabaseException {
		Project projectTest1 = new Project("Project 1930", 20, 0, 50);
		Project projectTest2 = new Project("Project 2000", 50, 10, 40);
		
		projectDAO.insertNewProject(projectTest1);
		projectDAO.insertNewProject(projectTest2);
		
		//Test to see that their id got updated
		assertFalse(projectTest1.getId() <= 0);
		assertFalse(projectTest2.getId() <= 0);
		
		//Test that their id is different
		assertFalse(projectTest1.getId() == projectTest2.getId());
		
		//Test to see they actually got added
		
		List<Project> allProjects = projectDAO.getAll();
		
		assertEquals(2, allProjects.size());
		
		boolean foundTest1 = false;
		boolean foundTest2 = false;
		
		for (Project project : allProjects) {
			assertFalse(project.getId() <= 0);
			
			if (!foundTest1) {
				foundTest1 = areEqual(project, projectTest1, true);
			}
			
			if (!foundTest2) {
				foundTest2 = areEqual(project, projectTest2, true);
			}
		}
		
		//Test to see they actually got found
		assertTrue(foundTest1 && foundTest2);
		
	}
	
	@Test 
	public void testGet() throws DatabaseException{
		Project projectTest1 = new Project("Project 1950", 20, 0, 50);
		Project projectTest2 = new Project("Project 2100", 50, 10, 40);
		
		projectDAO.insertNewProject(projectTest1);
		projectDAO.insertNewProject(projectTest2);
		
		Project test1 = projectDAO.findById(projectTest1.getId());
		assertTrue(areEqual(projectTest1, test1, true));
		
		Project test2 = projectDAO.findById(projectTest2.getId());
		assertTrue(areEqual(projectTest2, test2, true));
		
	}
	
	@Test
	public void testUpdate() throws DatabaseException {
		Project projectTest1 = new Project("Project 1950", 20, 0, 50);
		Project projectTest2 = new Project("Project 2100", 50, 10, 40);
		
		projectDAO.insertNewProject(projectTest1);
		projectDAO.insertNewProject(projectTest2);
		
		//Update test 1
		
		projectTest1.setTitle("The newest Project");
		projectTest1.setRecordsPerBatch(30);
		projectTest1.setFirstYCood(5);
		projectTest1.setRecordHeight(30);
		
		projectDAO.updateProject(projectTest1);
		
		//Update test 2
		
		int oldID = projectTest2.getId();
		projectTest2.setId(-1);
		
		projectDAO.updateProject(projectTest2);
		
		//Get all users
		
		List<Project> allProjects = projectDAO.getAll();
		boolean foundTest1 = false;
		boolean foundTest2 = false;
		
		for (Project user : allProjects) {
			if (!foundTest1 && user.getId() == projectTest1.getId()) {
				foundTest1 = true;
				//Check that it got updated correctly
				assertEquals(true, areEqual(projectTest1, user, true));
			}
			
			if (!foundTest2 && user.getId() == oldID) {
				foundTest2 = true;
				//Test for the parameters that cannot be changed
				assertTrue(user.getId() != projectTest2.getId());
			}
		}
		
		//Test that they could still be found
		assertTrue(foundTest1 && foundTest2);
		
	}
	
	@Test
	public void testDelete() throws DatabaseException {
		
		Project projectTest1 = new Project("Project 1950", 20, 0, 50);
		Project projectTest2 = new Project("Project 2100", 50, 10, 40);
		Project projectTest3 = new Project("Project 1900", 100, 8, 20);
		
		projectDAO.insertNewProject(projectTest1);
		projectDAO.insertNewProject(projectTest2);
		projectDAO.insertNewProject(projectTest3);
		
		//Delete User
		
		projectDAO.deleteProjectWithId(projectTest2.getId());
		
		List<Project> allProjects = projectDAO.getAll();
		assertEquals(2, allProjects.size());
		
		for (Project project : allProjects) {
			//Should not find USerTest 2
			assertFalse(project.getId() == projectTest2.getId());
		}
		
		//Attempt to delete the same user and should still remain the same
		
		projectDAO.deleteProjectWithId(projectTest2.getId());
		
		allProjects = projectDAO.getAll();
		assertEquals(2, allProjects.size());
		
		//Delete all of the users
		
		projectDAO.deleteProjectWithId(projectTest1.getId());
		projectDAO.deleteProjectWithId(projectTest3.getId());
		
		allProjects = projectDAO.getAll();
		assertEquals(0, allProjects.size());
		
		
	}
	
	@Test(expected = DatabaseException.class)
	public void testEmptyTitleAdd() throws DatabaseException {
		Project projectTest1 = new Project("", 10, 20, 30);
		projectDAO.insertNewProject(projectTest1);
	}
	
	@Test(expected = DatabaseException.class)
	public void testInvalidTitleAdd() throws DatabaseException {
		Project projectTest1 = new Project(null, 10, 20, 30);
		projectDAO.insertNewProject(projectTest1);
	}
	
	@Test(expected = DatabaseException.class)
	public void testNegativeRecordsPerBatchAdd() throws DatabaseException {
		Project projectTest1 = new Project("Project 1930", -1, 20, 30);
		projectDAO.insertNewProject(projectTest1);
	}
	
	@Test(expected = DatabaseException.class)
	public void testNegativeFirstCoordinateAdd() throws DatabaseException {
		Project projectTest1 = new Project("Project 1930", 10, -2, 30);
		projectDAO.insertNewProject(projectTest1);
	}
	
	@Test(expected = DatabaseException.class)
	public void testNegativeRecordHeightAdd() throws DatabaseException {
		Project projectTest1 = new Project("Project 1930", 10, 20, -1);
		projectDAO.insertNewProject(projectTest1);
	}
	
	@Test(expected = DatabaseException.class)
	public void testEmptyTitleUpdate() throws DatabaseException {
		Project projectTest1 = new Project("Project 1930", 10, 20, 30);
		projectDAO.insertNewProject(projectTest1);
		projectTest1.setTitle("");
		projectDAO.updateProject(projectTest1);
	}
	
	@Test(expected = DatabaseException.class)
	public void testInvalidTitleUpdate() throws DatabaseException {
		Project projectTest1 = new Project("Project 1930", 10, 20, 30);
		projectDAO.insertNewProject(projectTest1);
		projectTest1.setTitle(null);
		projectDAO.updateProject(projectTest1);
	}
	
	@Test(expected = DatabaseException.class)
	public void testNegativeRecordsPerBatchUpdate() throws DatabaseException {
		Project projectTest1 = new Project("Project 1930", 10, 20, 30);
		projectDAO.insertNewProject(projectTest1);
		projectTest1.setRecordsPerBatch(-1);
		projectDAO.updateProject(projectTest1);
	}
	
	@Test(expected = DatabaseException.class)
	public void testNegativeFirstCoordinateUpdate() throws DatabaseException {
		Project projectTest1 = new Project("Project 1930", 10, 20, 30);
		projectDAO.insertNewProject(projectTest1);
		projectTest1.setFirstYCood(-1);
		projectDAO.updateProject(projectTest1);
	}
	
	@Test(expected = DatabaseException.class)
	public void testNegativeRecordHeightUpdate() throws DatabaseException {
		Project projectTest1 = new Project("Project 1930", 10, 20, 30);
		projectDAO.insertNewProject(projectTest1);
		projectTest1.setRecordHeight(-1);
		projectDAO.updateProject(projectTest1);
	}
	
	
	
	//Helper Methods
		private boolean areEqual(Project a, Project b, boolean compareIDs) {
			if (compareIDs) {
				if (a.getId() != b.getId()) {
					return false;
				}
			}	
			return (safeEquals(a.getTitle(), b.getTitle()) &&
					safeEquals(a.getRecordsPerBatch(), b.getRecordsPerBatch()) &&
					safeEquals(a.getFirstYCood(), b.getFirstYCood()) &&
					safeEquals(a.getRecordHeight(), b.getRecordHeight()));
		}
		
		private boolean safeEquals(Object a, Object b) {
			if (a == null || b == null) {
				return (a == null && b == null);
			}
			else {
				return a.equals(b);
			}
		}

}