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
import shared.modal.Value;

public class ValueDAOTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		Database.initialize();
	}
	
	@AfterClass
	public static void afterClassTeardown() throws Exception {
		return;
	}
	
	
	private Database db;
	private ValueDAO valueDAO;
	
	@Before
	public void setUp() throws Exception {
		db = new Database();
		db.isTest(true);
		db.startTransaction();
		
		List<Value> allValuees = db.getValueDAO().getAll();
		
		//Clear out User table
		for (Value value : allValuees) {
			db.getValueDAO().deleteValueWithId(value.getId());
		}
		
		db.endTransaction(true);
		
		//Prepare Database for test case
		db = new Database();
		db.isTest(true);
		db.startTransaction();
		valueDAO = db.getValueDAO();
	}
	
	@After
	public void teardown() throws Exception{
		db.endTransaction(true);
		db = null;
		valueDAO = null;
	}


	@Test
	public void testGetAll() throws DatabaseException{
		
		List<Value> allValueList = valueDAO.getAll();
		assertEquals(0, allValueList.size());
	}
	
	@Test
	public void testAdd() throws DatabaseException {
		Value valueTest1 = new Value("Raul", 1 ,1);
		Value valueTest2 = new Value("15", 2 ,10);
		
		valueDAO.insertNewValue(valueTest1);
		valueDAO.insertNewValue(valueTest2);
		
		//Test to see that their id got updated
		assertFalse(valueTest1.getId() <= 0);
		assertFalse(valueTest2.getId() <= 0);
		
		//Test that their id is different
		assertFalse(valueTest1.getId() == valueTest2.getId());
		
		//Test to see they actually got added
		
		List<Value> allValuees = valueDAO.getAll();
		
		assertEquals(2, allValuees.size());
		
		boolean foundTest1 = false;
		boolean foundTest2 = false;
		
		for (Value value : allValuees) {
			assertFalse(value.getId() <= 0);
			
			if (!foundTest1) {
				foundTest1 = areEqual(value, valueTest1, true);
			}
			
			if (!foundTest2) {
				foundTest2 = areEqual(value, valueTest2, true);
			}
		}
		
		//Test to see they actually got found
		assertTrue(foundTest1 && foundTest2);
		
	}
	
	@Test 
	public void testGet() throws DatabaseException{
		Value valueTest1 = new Value("Raul", 1 ,1);
		Value valueTest2 = new Value("15", 2 ,10);
		
		valueDAO.insertNewValue(valueTest1);
		valueDAO.insertNewValue(valueTest2);
		
		Value test1 = valueDAO.findById(valueTest1.getId());
		assertTrue(areEqual(valueTest1, test1, true));
		
		Value test2 = valueDAO.findById(valueTest2.getId());
		assertTrue(areEqual(valueTest2, test2, true));
		
	}
	
	
	@Test
	public void testUpdate() throws DatabaseException {
		Value valueTest1 = new Value("Raul", 1 ,1);
		Value valueTest2 = new Value("15", 2 ,10);
		
		valueDAO.insertNewValue(valueTest1);
		valueDAO.insertNewValue(valueTest2);
		
		//Update test 1
		
		valueTest1.setContent("Juan");
		valueTest1.setRowNumber(3);

		
		valueDAO.updateValue(valueTest1);
		
		//Update test 2
		
		int oldID = valueTest2.getId();
		valueTest2.setId(-1);
		
		valueDAO.updateValue(valueTest2);
		
		//Get all users
		
		List<Value> allValuees = valueDAO.getAll();
		boolean foundTest1 = false;
		boolean foundTest2 = false;
		
		for (Value user : allValuees) {
			if (!foundTest1 && user.getId() == valueTest1.getId()) {
				foundTest1 = true;
				//Check that it got updated correctly
				assertEquals(true, areEqual(valueTest1, user, true));
			}
			
			if (!foundTest2 && user.getId() == oldID) {
				foundTest2 = true;
				//Test for the parameters that cannot be changed
				assertTrue(user.getId() != valueTest2.getId());
			}
		}
		
		//Test that they could still be found
		assertTrue(foundTest1 && foundTest2);
		
	}
	
	@Test
	public void testDelete() throws DatabaseException {
		
		Value valueTest1 = new Value("Raul", 1 ,1);
		Value valueTest2 = new Value("15", 2 ,10);
		Value valueTest3 = new Value("1930", 3, 3);
		
		valueDAO.insertNewValue(valueTest1);
		valueDAO.insertNewValue(valueTest2);
		valueDAO.insertNewValue(valueTest3);
		
		//Delete User
		
		valueDAO.deleteValueWithId(valueTest2.getId());
		
		List<Value> allValuees = valueDAO.getAll();
		assertEquals(2, allValuees.size());
		
		for (Value value : allValuees) {
			//Should not find USerTest 2
			assertFalse(value.getId() == valueTest2.getId());
		}
		
		//Attempt to delete the same user and should still remain the same
		
		valueDAO.deleteValueWithId(valueTest2.getId());
		
		allValuees = valueDAO.getAll();
		assertEquals(2, allValuees.size());
		
		//Delete all of the users
		
		valueDAO.deleteValueWithId(valueTest1.getId());
		valueDAO.deleteValueWithId(valueTest3.getId());
		
		allValuees = valueDAO.getAll();
		assertEquals(0, allValuees.size());
		
		
	}
	
	@Test(expected = DatabaseException.class)
	public void testNegativeRowNumberAdd() throws DatabaseException {
		Value valueTest1 = new Value("Raul", 1,-1);
		valueDAO.insertNewValue(valueTest1);
	}
	
	@Test(expected = DatabaseException.class)
	public void testNegativeRowNumberUpdate() throws DatabaseException {
		Value valueTest1 = new Value("Raul", 1, 3);
		valueDAO.insertNewValue(valueTest1);
		valueTest1.setRowNumber(-1);
		valueDAO.updateValue(valueTest1);
	}
	
	//Helper Methods
	private boolean areEqual(Value a, Value b, boolean compareIDs) {
		if (compareIDs) {
			if (a.getId() != b.getId()) {
				return false;
			}
		}	
		return (safeEquals(a.getContent(), b.getContent()) &&
				safeEquals(a.getColNumber(), b.getColNumber()) &&
				safeEquals(a.getRowNumber(), b.getRowNumber()));
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
