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
import shared.modal.Batch;
import shared.modal.Value;

public class BatchDAOTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		Database.initialize();
	}
	
	@AfterClass
	public static void afterClassTeardown() throws Exception {
		return;
	}
	
	
	private Database db;
	private BatchDAO batchDAO;
	private ValueDAO valueDAO;
	
	@Before
	public void setUp() throws Exception {
		db = new Database();
		db.isTest(true);
		db.startTransaction();
		
		List<Batch> allBatches = db.getBatchDAO().getAll();
		List<Value> allValues = db.getValueDAO().getAll();
		
		//Clear out tables
		for (Batch batch : allBatches) {
			db.getBatchDAO().deleteBatchWithId(batch.getId());
		}
		for (Value value : allValues) {
			db.getValueDAO().deleteValueWithId(value.getId());
		}
		
		db.endTransaction(true);
		
		//Prepare Database for test case
		db = new Database();
		db.isTest(true);
		db.startTransaction();
		batchDAO = db.getBatchDAO();
		valueDAO = db.getValueDAO();
	}
	
	@After
	public void teardown() throws Exception{
		db.endTransaction(true);
		db = null;
		batchDAO = null;
	}


	@Test
	public void testGetAll() throws DatabaseException{
		
		List<Batch> allBatchList = batchDAO.getAll();
		assertEquals(0, allBatchList.size());
	}
	
	@Test
	public void testAdd() throws DatabaseException {
		Batch batchTest1 = new Batch("../directory/theFile", 0);
		Batch batchTest2 = new Batch("/anotherDirectory/theFile", -1);
		
		batchDAO.insertNewBatch(batchTest1);
		batchDAO.insertNewBatch(batchTest2);
		
		//Test to see that their id got updated
		assertFalse(batchTest1.getId() <= 0);
		assertFalse(batchTest2.getId() <= 0);
		
		//Test that their id is different
		assertFalse(batchTest1.getId() == batchTest2.getId());
		
		//Test to see they actually got added
		
		List<Batch> allBatches = batchDAO.getAll();
		
		assertEquals(2, allBatches.size());
		
		boolean foundTest1 = false;
		boolean foundTest2 = false;
		
		for (Batch batch : allBatches) {
			assertFalse(batch.getId() <= 0);
			
			if (!foundTest1) {
				foundTest1 = areEqual(batch, batchTest1, true);
			}
			
			if (!foundTest2) {
				foundTest2 = areEqual(batch, batchTest2, true);
			}
		}
		
		//Test to see they actually got found
		assertTrue(foundTest1 && foundTest2);
		
	}
	
	@Test
	public void testAddingValue() throws DatabaseException {
		Batch batchTest1 = new Batch("/theimageFile.png", 0);
		
		batchDAO.insertNewBatch(batchTest1);
		
		Value valueTest1 = new Value("Raul", 1, 2);
		Value valueTest2 = new Value("Lopez", 1, 1);
		
		valueDAO.insertNewValue(valueTest1);
		valueDAO.insertNewValue(valueTest2);
		
		//Check if it got added successfully
		boolean updateCompleted = batchDAO.insertValueIntoBatch(valueTest1, batchTest1);
		assertTrue(updateCompleted);
		
		updateCompleted = batchDAO.insertValueIntoBatch(valueTest2, batchTest1);
		assertTrue(updateCompleted);
		
		//Get all field for project
		List<Value> batchValues = batchDAO.getAllValuesForBatch(batchTest1);
		
		assertEquals(2, batchValues.size());
		
		//Make sure the right one got inserted
		
		boolean foundTest1 = false;
		boolean foundTest2 = false;
		
		for (Value value : batchValues) {
			if (!foundTest1) {
				foundTest1 = (value.getId() == valueTest1.getId());
			}
			if (!foundTest2) {
				foundTest2 = (value.getId() == valueTest2.getId());
			}
			
		}
		
		assertTrue(foundTest1 && foundTest2);
	}
	
	@Test 
	public void testGet() throws DatabaseException{
		Batch batchTest1 = new Batch("../directory/theFile", 0);
		Batch batchTest2 = new Batch("/anotherDirectory/theFile", -1);
		
		batchDAO.insertNewBatch(batchTest1);
		batchDAO.insertNewBatch(batchTest2);
		
		Batch test1 = batchDAO.findById(batchTest1.getId());
		assertTrue(areEqual(batchTest1, test1, true));
		
		Batch test2 = batchDAO.findById(batchTest2.getId());
		assertTrue(areEqual(batchTest2, test2, true));
		
	}
	
	
	@Test
	public void testUpdate() throws DatabaseException {
		Batch batchTest1 = new Batch("../directory/theFile", 0);
		Batch batchTest2 = new Batch("/anotherDirectory/theFile", -1);
		
		batchDAO.insertNewBatch(batchTest1);
		batchDAO.insertNewBatch(batchTest2);
		
		//Update test 1
		
		batchTest1.setFilePath("theFile.txt");
		batchTest1.setStatus(1);

		
		batchDAO.updateBatch(batchTest1);
		
		//Update test 2
		
		int oldID = batchTest2.getId();
		batchTest2.setId(-1);
		
		batchDAO.updateBatch(batchTest2);
		
		//Get all users
		
		List<Batch> allBatches = batchDAO.getAll();
		boolean foundTest1 = false;
		boolean foundTest2 = false;
		
		for (Batch user : allBatches) {
			if (!foundTest1 && user.getId() == batchTest1.getId()) {
				foundTest1 = true;
				//Check that it got updated correctly
				assertEquals(true, areEqual(batchTest1, user, true));
			}
			
			if (!foundTest2 && user.getId() == oldID) {
				foundTest2 = true;
				//Test for the parameters that cannot be changed
				assertTrue(user.getId() != batchTest2.getId());
			}
		}
		
		//Test that they could still be found
		assertTrue(foundTest1 && foundTest2);
		
	}
	
	@Test
	public void testDelete() throws DatabaseException {
		
		Batch batchTest1 = new Batch("../directory/theFile", 0);
		Batch batchTest2 = new Batch("/anotherDirectory/theFile", -1);
		Batch batchTest3 = new Batch("theFile.txt", 1);
		
		batchDAO.insertNewBatch(batchTest1);
		batchDAO.insertNewBatch(batchTest2);
		batchDAO.insertNewBatch(batchTest3);
		
		//Delete User
		
		batchDAO.deleteBatchWithId(batchTest2.getId());
		
		List<Batch> allBatches = batchDAO.getAll();
		assertEquals(2, allBatches.size());
		
		for (Batch batch : allBatches) {
			//Should not find USerTest 2
			assertFalse(batch.getId() == batchTest2.getId());
		}
		
		//Attempt to delete the same user and should still remain the same
		
		batchDAO.deleteBatchWithId(batchTest2.getId());
		
		allBatches = batchDAO.getAll();
		assertEquals(2, allBatches.size());
		
		//Delete all of the users
		
		batchDAO.deleteBatchWithId(batchTest1.getId());
		batchDAO.deleteBatchWithId(batchTest3.getId());
		
		allBatches = batchDAO.getAll();
		assertEquals(0, allBatches.size());
		
		
	}
	
	@Test(expected = DatabaseException.class)
	public void testInvalidFilePathAdd() throws DatabaseException {
		Batch batchTest1 = new Batch(null, 0);
		batchDAO.insertNewBatch(batchTest1);
	}
	
	@Test(expected = DatabaseException.class)
	public void testInvalidFilePathUpdate() throws DatabaseException {
		Batch batchTest1 = new Batch("/directory/anotherDirectory/file.txt", 0);
		batchDAO.insertNewBatch(batchTest1);
		batchTest1.setFilePath(null);
		batchDAO.updateBatch(batchTest1);
	}
	
	@Test(expected = DatabaseException.class)
	public void testEmptyFilePathAdd() throws DatabaseException {
		Batch batchTest1 = new Batch("", 0);
		batchDAO.insertNewBatch(batchTest1);
	}
	
	@Test(expected = DatabaseException.class)
	public void testEmptyFilePathUpdate() throws DatabaseException {
		Batch batchTest1 = new Batch("/directory/anotherDirectory/file.txt", 0);
		batchDAO.insertNewBatch(batchTest1);
		batchTest1.setFilePath("");
		batchDAO.updateBatch(batchTest1);
	}
	
	//Helper Methods
	private boolean areEqual(Batch a, Batch b, boolean compareIDs) {
		if (compareIDs) {
			if (a.getId() != b.getId()) {
				return false;
			}
		}	
		return (safeEquals(a.getFilePath(), b.getFilePath()) &&
				safeEquals(a.getStatus(), b.getStatus()));
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
