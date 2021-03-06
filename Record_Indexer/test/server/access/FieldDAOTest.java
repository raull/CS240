package server.access;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import server.database.Database;
import server.database.DatabaseException;
import shared.modal.Field;

public class FieldDAOTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		Database.initialize();
	}
	
	@AfterClass
	public static void afterClassTeardown() throws Exception {
		return;
	}
	
	
	private Database db;
	private FieldDAO fieldDAO;
	
	@Before
	public void setUp() throws Exception {
		db = new Database();
		db.isTest(true);
		db.startTransaction();
		
		List<Field> allFields = db.getFieldDAO().getAll();
		
		//Clear out User table
		for (Field field : allFields) {
			db.getFieldDAO().deleteFieldWithId(field.getId());
		}
		
		db.endTransaction(true);
		
		//Prepare Database for test case
		db = new Database();
		db.isTest(true);
		db.startTransaction();
		fieldDAO = db.getFieldDAO();
	}
	
	@After
	public void teardown() throws Exception{
		db.endTransaction(true);
		db = null;
		fieldDAO = null;
	}


	@Test
	public void testGetAll() throws DatabaseException{
		
		List<Field> allBatchList = fieldDAO.getAll();
		assertEquals(0, allBatchList.size());
	}
	
	@Test
	public void testAdd() throws DatabaseException {
		Field fieldTest1 = new Field("Name", 0, 1, 20, "http://theurl.is", "/theFile.txt");
		Field fieldTest2 = new Field("Last Name", 15, 2, 20, "http://theOtherURL.is", "/theAnotherFile.txt");
		
		fieldDAO.insertNewField(fieldTest1);
		fieldDAO.insertNewField(fieldTest2);
		
		//Test to see that their id got updated
		assertFalse(fieldTest1.getId() <= 0);
		assertFalse(fieldTest2.getId() <= 0);
		
		//Test that their id is different
		assertFalse(fieldTest1.getId() == fieldTest2.getId());
		
		//Test to see they actually got added
		
		List<Field> allFields = fieldDAO.getAll();
		
		assertEquals(2, allFields.size());
		
		boolean foundTest1 = false;
		boolean foundTest2 = false;
		
		for (Field field : allFields) {
			assertFalse(field.getId() <= 0);
			
			if (!foundTest1) {
				foundTest1 = areEqual(field, fieldTest1, true);
			}
			
			if (!foundTest2) {
				foundTest2 = areEqual(field, fieldTest2, true);
			}
		}
		
		//Test to see they actually got found
		assertTrue(foundTest1 && foundTest2);
		
	}
	
	@Test 
	public void testGet() throws DatabaseException{
		Field fieldTest1 = new Field("Name", 0, 1, 20, "http://theurl.is", "/theFile.txt");
		Field fieldTest2 = new Field("Last Name", 15, 2, 20, "http://theOtherURL.is", "/theAnotherFile.txt");
		
		fieldDAO.insertNewField(fieldTest1);
		fieldDAO.insertNewField(fieldTest2);
		
		Field test1 = fieldDAO.findById(fieldTest1.getId());
		assertTrue(areEqual(fieldTest1, test1, true));
		
		Field test2 = fieldDAO.findById(fieldTest2.getId());
		assertTrue(areEqual(fieldTest2, test2, true));
		
	}
	
	@Test
	public void testUpdate() throws DatabaseException {
		Field fieldTest1 = new Field("Name", 0, 1, 20, "http://theurl.is", "/theFile.txt");
		Field fieldTest2 = new Field("Last Name", 15, 2, 20, "http://theOtherURL.is", "/theAnotherFile.txt");
		
		fieldDAO.insertNewField(fieldTest1);
		fieldDAO.insertNewField(fieldTest2);
		
		//Update test 1
		
		fieldTest1.setHelpHtml("http://yes.awesome");;
		fieldTest1.setKnownData("dictionary.txt");

		
		fieldDAO.updateField(fieldTest1);
		
		//Update test 2
		
		int oldID = fieldTest2.getId();
		fieldTest2.setId(-1);
		
		fieldDAO.updateField(fieldTest2);
		
		//Get all users
		
		List<Field> allFields = fieldDAO.getAll();
		boolean foundTest1 = false;
		boolean foundTest2 = false;
		
		for (Field field : allFields) {
			if (!foundTest1 && field.getId() == fieldTest1.getId()) {
				foundTest1 = true;
				//Check that it got updated correctly
				assertEquals(true, areEqual(fieldTest1, field, true));
			}
			
			if (!foundTest2 && field.getId() == oldID) {
				foundTest2 = true;
				//Test for the parameters that cannot be changed
				assertTrue(field.getId() != fieldTest2.getId());
			}
		}
		
		//Test that they could still be found
		assertTrue(foundTest1 && foundTest2);
		
	}
	
	@Test
	public void testDelete() throws DatabaseException {
		
		Field fieldTest1 = new Field("Name", 0, 1, 20, "http://theurl.is", "/theFile.txt");
		Field fieldTest2 = new Field("Last Name", 15, 2, 20, "http://theOtherURL.is", "/theAnotherFile.txt");
		Field fieldTest3 = new Field("Year", 30, 3, 20, "http://theURL.not", "/theFileis.txt");
		
		fieldDAO.insertNewField(fieldTest1);
		fieldDAO.insertNewField(fieldTest2);
		fieldDAO.insertNewField(fieldTest3);
		
		//Delete User
		
		fieldDAO.deleteFieldWithId(fieldTest2.getId());
		
		List<Field> allFields = fieldDAO.getAll();
		assertEquals(2, allFields.size());
		
		for (Field field : allFields) {
			//Should not find USerTest 2
			assertFalse(field.getId() == fieldTest2.getId());
		}
		
		//Attempt to delete the same user and should still remain the same
		
		fieldDAO.deleteFieldWithId(fieldTest2.getId());
		
		allFields = fieldDAO.getAll();
		assertEquals(2, allFields.size());
		
		//Delete all of the users
		
		fieldDAO.deleteFieldWithId(fieldTest1.getId());
		fieldDAO.deleteFieldWithId(fieldTest3.getId());
		
		allFields = fieldDAO.getAll();
		assertEquals(0, allFields.size());
		
		
	}
	
	@Test(expected = DatabaseException.class)
	public void testInvalidTiTleAdd() throws DatabaseException {
		Field fieldTest1 = new Field(null, 0, 1, 20, "http://theurl.is", "/theFile.txt");
		fieldDAO.insertNewField(fieldTest1);
	}
	
	@Test(expected = DatabaseException.class)
	public void testNegativeXCoordAdd() throws DatabaseException {
		Field fieldTest1 = new Field("Name", -1, 1, 20, "http://theurl.is", "/theFile.txt");
		fieldDAO.insertNewField(fieldTest1);
	}
	
	@Test(expected = DatabaseException.class)
	public void testNegativeColumnNumberAdd() throws DatabaseException {
		Field fieldTest1 = new Field("Name", 15, -3, 20, "http://theurl.is", "/theFile.txt");
		fieldDAO.insertNewField(fieldTest1);
	}
	
	@Test(expected = DatabaseException.class)
	public void testNegativeWidthAdd() throws DatabaseException {
		Field fieldTest1 = new Field("Name", 15, 2, -20, "http://theurl.is", "/theFile.txt");
		fieldDAO.insertNewField(fieldTest1);
	}
	
	@Test(expected = DatabaseException.class)
	public void testInvalidHelpHTMLUpdate() throws DatabaseException {
		Field fieldTest1 = new Field("Name", 0, 1, 20, "http://theurl.is", "/theFile.txt");
		fieldDAO.insertNewField(fieldTest1);
		fieldTest1.setHelpHtml(null);
		fieldDAO.updateField(fieldTest1);
	}
	
	//Helper Methods
	private boolean areEqual(Field a, Field b, boolean compareIDs) {
		if (compareIDs) {
			if (a.getId() != b.getId()) {
				return false;
			}
		}	
		return (safeEquals(a.getTitle(), b.getTitle()) &&
				safeEquals(a.getxCoord(), b.getxCoord()) &&
				safeEquals(a.getColNumber(), b.getColNumber()) &&
				safeEquals(a.getWidth(), b.getWidth()) &&
				safeEquals(a.getHelpHtml(), b.getHelpHtml()) &&
				safeEquals(a.getKnownData(), b.getKnownData()));
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
