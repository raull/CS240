package server.facade;

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
			Project project = new Project();
			project.setId(projectId);
			if (projectId <= 0) {
				allFields = db.getFieldDAO().getAll();
			} else {
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
			Batch batch = db.getProjectDAO().getBatch(project, user);
			if (batch != null) {
				//Change the status of the Batch
				batch.setStatus(1);
				db.getBatchDAO().updateBatch(batch);
				//Assign the Batch to the User
				user.setCurrentBatchId(batch.getId());
				db.getUserDAO().updateUser(user);
			} else {
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
}
