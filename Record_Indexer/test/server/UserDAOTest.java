package server.access;

import static org.junit.Assert.*;

import java.awt.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.Database;
import server.DatabaseException;
import server.access.UserDAO;
import shared.modal.User;

public class UserDAOTest {
	
	@BeforeClass
	public void setUpBeforeClass() throws Exception{
		Database.initialize();
	}
	
	@AfterClass
	public void afterClassTeardown() throws Exception {
		return;
	}
	
	
	private Database db;
	private UserDAO userDAO;
	
	@Before
	public void setUp() throws Exception {
		db = new Database();
		db.isTest();
		db.startTransaction();
		
		List<User> allUsers = db.getUserDAO().getAll();
		
		//Clear out User table
		for (User user : allUsers) {
			db.getUserDAO().deleteUserWithId(user.getId());
		}
		
		db.endTransaction(true);
		
		//Prepare Database for test case
		db = new Database();
		db.isTest();
		db.startTransaction();
		userDAO = db.getUserDAO();
	}
	
	@After
	public void teardown() {
		db.endTransaction(true);
		db = null;
		userDAO = null;
	}
	
	@Test
	public void testGetAll() {
		
		List<User> allUsersList = userDAO.getAll();
		assertEquals(0, allUsersList.size());
	}

}
