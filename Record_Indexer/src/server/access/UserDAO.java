package server.access;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import server.database.Database;
import server.database.DatabaseException;
import shared.modal.*;

/**
 * User Data Access Object class. For Accessing and persisting to the database concerning User operations
 * */
public class UserDAO {
	
	
	//Instance Fields
	/**
	 * Database object to start and end transactions
	 */
	private Database db;
	
	//-----------------------------------------------------
	//Constructors
	
	public UserDAO(Database db) {
		this.db = db;
	}
	
	//-----------------------------------------------------
	//Methods
	
	public List<User> getAll() throws DatabaseException{
		
		PreparedStatement stm = null;
		ResultSet result = null;
				
		ArrayList<User> userList = new ArrayList<User>();
		try {			
			//Set up Query
			String sql = "SELECT * FROM user";
			stm = db.getConnection().prepareStatement(sql);

			//Execute Query
			result = stm.executeQuery();
			
			//Handle Response to create new User
			while (result.next()) {

				String username = result.getString(2);
				String password = result.getString(3);
				String firstName = result.getString(4);
				String lastName = result.getString(5);
				String email = result.getString(6);
				int recordsCount = result.getInt(7);
				int batchId = result.getInt(8);
				User newUser = new User(username, password, firstName, lastName, email, recordsCount);
				newUser.setId(result.getInt(1));
				newUser.setCurrentBatchId(batchId);
				
				userList.add(newUser);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error getting all User: "+ e.getLocalizedMessage(), e);
		} 
				
		return userList;
	}
	
	/**
	 * Find a User by providing the User's ID
	 * @param id of the user to find
	 * @return A User matching the provided User
	 */
	public User findById(int id) throws DatabaseException{
		
		PreparedStatement stm = null;
		ResultSet result = null;
		
		User user = null;
		
		try {			
			//Set up Query
			String sql = "SELECT * FROM user WHERE id = ?";
			stm = db.getConnection().prepareStatement(sql);
			stm.setInt(1, id);

			//Execute Query
			result = stm.executeQuery();
			
			//Handle Response to create new User
			while (result.next()) {
				int userId = result.getInt(1);
				String username = result.getString(2);
				String password = result.getString(3);
				String firstName = result.getString(4);
				String lastName = result.getString(5);
				String email = result.getString(6);
				int recordsCount = result.getInt(7);
				int batchId = result.getInt(8);
				
				user = new User(username, password, firstName, lastName, email, recordsCount);
				user.setId(userId);
				user.setCurrentBatchId(batchId);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error finding User: " + e.getLocalizedMessage(), e);
		} 
				
		return user;
	}
	
	/**
	 * Find a User by providing the User's username
	 * @param username of the user to find
	 * @return A User matching the provided username
	 */
	public User findByUsername(String username) throws DatabaseException{
		PreparedStatement stm = null;
		ResultSet result = null;
		
		User user = null;
		
		try {
			//Set up Query
			String sql = "SELECT * FROM user WHERE username = ?";
			stm = db.getConnection().prepareStatement(sql);
			stm.setString(1, username);

			//Execute Query
			result = stm.executeQuery();
			
			//Handle Response to create new User
			while (result.next()) {
				int id = result.getInt(1);
				String usernameString = result.getString(2);
				String password = result.getString(3);
				String firstName = result.getString(4);
				String lastName = result.getString(5);
				String email = result.getString(6);
				int recordsCount = result.getInt(7);
				int batchId = result.getInt(8);
				
				user = new User(usernameString, password, firstName, lastName, email, recordsCount);
				user.setId(id);
				user.setCurrentBatchId(batchId);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error finding User: " + e.getLocalizedMessage(), e);
		}
				
		return user;
	}
	
	/**
	 * Insert a new User to the Database
	 * @param newUser, a User object representing the new User to persist
	 * @return true if the User was inserted successfully, otherwise false
	 */
	public boolean insertNewUser(User newUser) throws DatabaseException {
		
		PreparedStatement stm = null;
		ResultSet result = null;
		Statement keyStm = null; 
				
		try {
			//Set up Query
			String sql = "INSERT INTO user (username, password, firstname, lastname, email, records_count) VALUES (?,?,?,?,?,?)";
			stm = db.getConnection().prepareStatement(sql);
			stm.setString(1, newUser.getUsername());
			stm.setString(2, newUser.getPassword());
			stm.setString(3, newUser.getFirstName());
			stm.setString(4, newUser.getLastName());
			stm.setString(5, newUser.getEmail());
			stm.setInt(6, newUser.getRecordCount());
			
			//Execute Query and handle the response to get back the ID
			if (stm.executeUpdate() == 1) {
				keyStm = db.getConnection().createStatement();
				result = keyStm.executeQuery("SELECT last_insert_rowid()");
				result.next();
				newUser.setId(result.getInt(1));
				return true;

			} else {
				return false;
			}
						
		} catch (SQLException e) {
			throw new DatabaseException("Error adding User: " + e.getLocalizedMessage(), e);
		} 
		
	}
	
	/**
	 * Update user in the Database for current batch and for records count
	 * @param user, User object containing new information to update
	 * @return true if the User was updated successfully, otherwise false
	 */
	public boolean updateUser(User user) throws DatabaseException{
		PreparedStatement stm = null;
		
		try {

			//Set up Query
			String sql = "UPDATE user SET firstname = ?, lastname = ?, password = ?, email = ?, current_batch = ?, records_count = ? WHERE id = ? ";
			stm = db.getConnection().prepareStatement(sql);
			stm.setString(1, user.getFirstName());
			stm.setString(2, user.getLastName());
			stm.setString(3, user.getPassword());
			stm.setString(4, user.getEmail());
			stm.setInt(5, user.getCurrentBatchId());
			stm.setInt(6, user.getRecordCount());
			stm.setInt(7,user.getId());

			//Execute Query
			if (stm.executeUpdate() == 1) {
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error updating User: " + e.getLocalizedMessage(), e);
		} 
	}
	
	/**
	 * Delete User by providing the User's ID
	 * @param id representing the User's ID
	 * @return true if the User was deleted successfully, otherwise false
	 */
	public boolean deleteUserWithId(int id) throws DatabaseException{
		
		PreparedStatement stm = null;
				
		try {
			//Set up Query
			String sql = "DELETE FROM user WHERE id = ? ";
			stm = db.getConnection().prepareStatement(sql);
			stm.setInt(1, id);

			//Execute Query
			if (stm.executeUpdate() == 1) {
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error deleting User: " + e.getLocalizedMessage(), e);
		}
		
	}
	
	
	
}
