package server.facade;

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
			throw new ServerException("Server Error: " + e.getLocalizedMessage(), e);
		}
		
	}
}
