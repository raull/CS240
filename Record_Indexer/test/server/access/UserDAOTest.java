package server.access;

import static org.junit.Assert.*;

import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.access.UserDAO;
import server.database.Database;
import server.database.DatabaseException;
import shared.modal.User;

@RunWith(JUnit4.class)
public class UserDAOTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		Database.initialize();
	}
	
	@AfterClass
	public static void afterClassTeardown() throws Exception {
		return;
	}
	
	
	private Database db;
	private UserDAO userDAO;
	
	@Before
	public void setUp() throws Exception {
		db = new Database();
		db.isTest(true);
		db.startTransaction();
		
		List<User> allUsers = db.getUserDAO().getAll();
		
		//Clear out User table
		for (User user : allUsers) {
			db.getUserDAO().deleteUserWithId(user.getId());
		}
		
		db.endTransaction(true);
		
		//Prepare Database for test case
		db = new Database();
		db.isTest(true);
		db.startTransaction();
		userDAO = db.getUserDAO();
	}
	
	@After
	public void teardown() throws Exception{
		db.endTransaction(true);
		db = null;
		userDAO = null;
	}
	
	@Test
	public void testGetAll() throws DatabaseException{
		
		List<User> allUsersList = userDAO.getAll();
		assertEquals(0, allUsersList.size());
	}
	
	@Test
	public void testAdd() throws DatabaseException {
		User userTest1 = new User("raull", "thepassword", "Raul", "Lopez", "ruffles91@gmail.com",5);
		User userTest2 = new User("cinthia91", "anotherpassword", "Cinthia", "Beltran", "cinthia_latina@hotmail.com",6);
		
		userDAO.insertNewUser(userTest1);
		userDAO.insertNewUser(userTest2);
		
		//Test to see that their id got updated
		assertFalse(userTest1.getId() <= 0);
		assertFalse(userTest2.getId() <= 0);
		
		//Test that their id is different
		assertFalse(userTest1.getId() == userTest2.getId());
		
		//Test to see they actually got added
		
		List<User> allUsers = userDAO.getAll();
		
		assertEquals(2, allUsers.size());
		
		boolean foundTest1 = false;
		boolean foundTest2 = false;
		
		for (User user : allUsers) {
			assertFalse(user.getId() <= 0);
			
			if (!foundTest1) {
				foundTest1 = areEqual(user, userTest1, true);
			}
			
			if (!foundTest2) {
				foundTest2 = areEqual(user, userTest2, true);
			}
		}
		
		//Test to see they actually got found
		assertTrue(foundTest1 && foundTest2);
		
	}
	
	@Test 
	public void testGet() throws DatabaseException{
		User userTest1 = new User("raull", "thepassword", "Raul", "Lopez", "ruffles91@gmail.com", 6);
		User userTest2 = new User("cinthia91", "anotherpassword", "Cinthia", "Beltran", "cinthia_latina@hotmail.com", 5);
		
		userDAO.insertNewUser(userTest1);
		userDAO.insertNewUser(userTest2);
		
		User test1 = userDAO.findById(userTest1.getId());
		assertTrue(areEqual(userTest1, test1, true));
		
		User test2 = userDAO.findById(userTest2.getId());
		assertTrue(areEqual(userTest2, test2, true));
		
	}
	
	@Test
	public void testUpdate() throws DatabaseException {
		User userTest1 = new User("raull91", "lopes", "Raul", "Lopez", "ruffles91@gmail.com", 7);
		User userTest2 = new User("moyoteg", "password", "Raul", "Lopez", "moyoteg@gmail.com", 5);
		
		userDAO.insertNewUser(userTest1);
		userDAO.insertNewUser(userTest2);
		
		//Update test 1
		
		userTest1.setCurrentBatchId(2);
		userTest1.setRecordCount(5);
		userTest1.setFirstName("Juan");
		userTest1.setLastName("Delgado");
		userTest1.setPassword("lopesdel82");
		userTest1.setEmail("lopesdel82@hotmail.com");
		
		userDAO.updateUser(userTest1);
		
		//Update test 2
		
		int oldID = userTest2.getId();
		userTest2.setId(-1);
		String oldUsername = userTest2.getUsername();
		userTest2.setUsername("moyoteg91");
		
		userDAO.updateUser(userTest2);
		
		//Get all users
		
		List<User> allUsers = userDAO.getAll();
		boolean foundTest1 = false;
		boolean foundTest2 = false;
		
		for (User user : allUsers) {
			if (!foundTest1 && user.getId() == userTest1.getId()) {
				foundTest1 = true;
				//Check that it got updated correctly
				assertEquals(true, areEqual(userTest1, user, true));
			}
			
			if (!foundTest2 && user.getId() == oldID) {
				foundTest2 = true;
				//Test for the parameters that cannot be changed
				assertTrue(oldUsername.equals(user.getUsername()) && !userTest2.getUsername().equals(user.getUsername()));
				assertTrue(user.getId() != userTest2.getId());
			}
		}
		
		//Test that they could still be found
		assertTrue(foundTest1 && foundTest2);
		
	}
	
	@Test
	public void testDelete() throws DatabaseException {
		
		User userTest1 = new User("raull91", "lopes", "Raul", "Lopez", "ruffles91@gmail.com", 5);
		User userTest2 = new User("moyoteg", "password", "Raul", "Lopez", "moyoteg@gmail.com", 6);
		User userTest3 = new User("cinthia", "password2", "Cinthia", "Beltran", "cinthia_latina@hotmail.com", 8);
		
		userDAO.insertNewUser(userTest1);
		userDAO.insertNewUser(userTest2);
		userDAO.insertNewUser(userTest3);
		
		//Delete User
		
		userDAO.deleteUserWithId(userTest2.getId());
		
		List<User> allUsers = userDAO.getAll();
		assertEquals(2, allUsers.size());
		
		for (User user : allUsers) {
			//Should not find USerTest 2
			assertFalse(user.getId() == userTest2.getId());
		}
		
		//Attempt to delete the same user and should still remain the same
		
		userDAO.deleteUserWithId(userTest2.getId());
		
		allUsers = userDAO.getAll();
		assertEquals(2, allUsers.size());
		
		//Delete all of the users
		
		userDAO.deleteUserWithId(userTest1.getId());
		userDAO.deleteUserWithId(userTest3.getId());
		
		allUsers = userDAO.getAll();
		assertEquals(0, allUsers.size());
		
		
	}
	
	@Test(expected = DatabaseException.class)
	public void testInvalidAdd() throws DatabaseException{
		User invalidUser1 = new User(null, "password", "Raul", "Lopez", "ruffles91@gmail.com", 7);
		userDAO.insertNewUser(invalidUser1);
		
	}
	
	@Test(expected = DatabaseException.class)
	public void testInvalidUpdate() throws DatabaseException{
		User invalidUser1 = new User("raull91", "password", "Raul", "Lopez", "email", 0);
		userDAO.insertNewUser(invalidUser1);
		invalidUser1.setEmail(null);
		userDAO.updateUser(invalidUser1);
		
	}
	
	
	//Helper Methods
	private boolean areEqual(User a, User b, boolean compareIDs) {
		if (compareIDs) {
			if (a.getId() != b.getId()) {
				return false;
			}
		}	
		return (safeEquals(a.getUsername(), b.getUsername()) &&
				safeEquals(a.getPassword(), b.getPassword()) &&
				safeEquals(a.getFirstName(), b.getFirstName()) &&
				safeEquals(a.getLastName(), b.getLastName()) &&
				safeEquals(a.getEmail(), b.getEmail()) &&
				safeEquals(a.getRecordCount(), b.getRecordCount()) &&
				safeEquals(a.getCurrentBatchId(), b.getCurrentBatchId()));
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
