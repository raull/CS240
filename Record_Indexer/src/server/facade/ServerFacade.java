package server.facade;

import java.util.ArrayList;
import java.util.List;

import server.ServerException;
import server.database.*;
import shared.modal.*;

public class ServerFacade {

	public static void initialize() throws ServerException{
		try {
			Database.initialize();		
		}
		catch (DatabaseException e) {
			throw new ServerException(e.getMessage(), e);
		}	
	}
	
	public static User validateUser(String username, String password) throws ServerException {
		
		Database db = new Database();
		
		try {
			db.startTransaction();
			User user = db.getUserDAO().findByUsername(username);
			db.endTransaction(true);
			
			if (user != null && user.getPassword().equals(password)) {
				return user;
			} else {
				return null;
			}

		} catch (Exception e) {
			try {
				db.endTransaction(false);
				throw new ServerException("Server Error: " + e.getLocalizedMessage(), e);
			} catch (Exception e2) {
				throw new ServerException("Server Error: " + e2.getLocalizedMessage(), e2);
			}
		}
		
	}
	
	public static List<Project> getProjects() throws ServerException {
		
		Database db = new Database();
		List<Project> allProjects;
		
		try {
			db.startTransaction();
			allProjects = db.getProjectDAO().getAll();
			db.endTransaction(true);
		} catch (Exception e) {
			try {
				db.endTransaction(false);
				throw new ServerException("Server Error: " + e.getLocalizedMessage(), e);
			} catch (Exception e2) {
				throw new ServerException("Server Error: " + e2.getLocalizedMessage(), e2);
			}
		}
		
		return allProjects;
	}
	
	public static Project getProject(int projectId) throws ServerException {
		Database db = new Database();
		
		try {
			db.startTransaction();
			Project project = db.getProjectDAO().findById(projectId);
			db.endTransaction(true);
			return project;
		} catch (Exception e) {
			try {
				db.endTransaction(false);
				throw new ServerException("Server Error: " + e.getLocalizedMessage(), e);
			} catch (Exception e2) {
				throw new ServerException("Server Error: " + e2.getLocalizedMessage(), e2);
			}
		}		
	}
	
	public static String getSampleImage(int projectId) throws ServerException {
		
		Database db = new Database();
		List<Batch> allBatches;
		
		try {
			db.startTransaction();
			Project project = new Project();
			project.setId(projectId);
			allBatches = db.getProjectDAO().getAllBatchesForProjet(project);
			db.endTransaction(true);
		} catch (Exception e) {
			try {
				db.endTransaction(false);
				throw new ServerException("Server Error: " + e.getLocalizedMessage(), e);
			} catch (Exception e2) {
				throw new ServerException("Server Error: " + e2.getLocalizedMessage(), e2);
			}
		}
		
		return allBatches.get(0).getFilePath();
	}
	
	public static List<Field> getFields(int projectId) throws ServerException {
		Database db = new Database();
		List<Field> allFields;
		
		try {
			db.startTransaction();
			
			
			if (projectId < 0) {
				allFields = db.getFieldDAO().getAll();
			} else {
				Project project = db.getProjectDAO().findById(projectId);
				
				//Check for being a valid project
				if (project == null) {
					db.endTransaction(false);
					return null;
				}
				allFields = db.getProjectDAO().getAllFieldsForProject(project);
			}
			db.endTransaction(true);
		} catch (Exception e) {
			try {
				db.endTransaction(false);
				throw new ServerException("Server Error: " + e.getLocalizedMessage(), e);
			} catch (Exception e2) {
				throw new ServerException("Server Error: " + e2.getLocalizedMessage(), e2);
			}
		}
		
		return allFields;
	}
	
	public static Batch getAvailableBatch(int projectId, User user) throws ServerException{
		Database db = new Database();
		
		try {
			db.startTransaction();
			Project project = new Project();
			project.setId(projectId);
			//First get the next available batch
			Batch batch = db.getProjectDAO().getBatch(project);
			if (batch != null) {
				//Change the status of the Batch
				batch.setStatus(1);
				db.getBatchDAO().updateBatch(batch);
				//Assign the Batch to the User
				user.setCurrentBatchId(batch.getId());
				db.getUserDAO().updateUser(user);
			} else {
				db.endTransaction(false);
				return null;
			}
			db.endTransaction(true);
			
			return batch;
		} catch (Exception e) {
			try {
				db.endTransaction(false);
				throw new ServerException("Server Error: " + e.getLocalizedMessage(), e);
			} catch (Exception e2) {
				throw new ServerException("Server Error: " + e2.getLocalizedMessage(), e2);
			}
		}	
	}
	
	public static boolean submitBatch(String[] records, int batchId, User user) throws ServerException {
		
		Database db = new Database();
		
		try {
			db.startTransaction();
			//First Get the batch
			Batch batch = db.getBatchDAO().findById(batchId);
			Project project = db.getProjectDAO().findById(batch.getProjectId());
			List<Field> fields = db.getProjectDAO().getAllFieldsForProject(project);
			
			//Check that the amount of records to insert is no more than the allowed amount
			if (records.length != project.getRecordsPerBatch()) {
				db.endTransaction(false);
				return false;
			}
			
			//Insert values into the batch
			
			for (int i = 0; i < records.length; i++) {
				String[] contents = records[i].split(",",-1);
				
				//Check that the records has the same amount of columns as the project
				if (fields.size() != contents.length) {
					db.endTransaction(false);
					return false;
				}
				
				for (int j = 0; j < contents.length; j++) {
					Value newValue = new Value(contents[j], i+1, j+1);
					if(!db.getValueDAO().insertNewValue(newValue) || !db.getBatchDAO().insertValueIntoBatch(newValue, batch)) {
						db.endTransaction(false);
						return false;
					}
				}
			}
			
			//Update User
			user.setCurrentBatchId(0);
			user.setRecordCount(user.getRecordCount() + project.getRecordsPerBatch());
			if(!db.getUserDAO().updateUser(user)) {
				db.endTransaction(false);
				return false;
			}
			
			//Update Batch
			batch.setStatus(2);
			if(!db.getBatchDAO().updateBatch(batch)){
				db.endTransaction(false);
				return false;
			}

			db.endTransaction(true);
			return true;
		} catch (Exception e) {
			try {
				db.endTransaction(false);
				throw new ServerException("Server Error: " + e.getLocalizedMessage(), e);
			} catch (Exception e2) {
				throw new ServerException("Server Error: " + e2.getLocalizedMessage(), e2);
			}
		}	
	}
	
	public static List<String> search(int fieldId, String value) throws ServerException{
		Database db = new Database();
		
		ArrayList<String> tuples = new ArrayList<String>();
		
		try {
			db.startTransaction();
			//get Field
			Field field = db.getFieldDAO().findById(fieldId);
			tuples = (ArrayList<String>)db.getValueDAO().getValueMatching(field, value);
			db.endTransaction(true);
		} catch (Exception e) {
			try {
				db.endTransaction(false);
				throw new ServerException("Server Error: " + e.getLocalizedMessage(), e);
			} catch (Exception e2) {
				throw new ServerException("Server Error: " + e2.getLocalizedMessage(), e2);
			}
		}
		
		return tuples;
	}
}
