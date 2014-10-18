package server.access;

import java.util.ArrayList;
import java.util.List;
import java.sql.*;

import server.Database;
import server.DatabaseException;
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

				String username = result.getString(1);
				String password = result.getString(2);
				String firstName = result.getString(3);
				String lastName = result.getString(4);
				String email = result.getString(5);
				int recordsCount = result.getInt(6);
				int batchId = result.getInt(7);
				User newUser = new User(username, password, firstName, lastName, email);
				newUser.setId(result.getInt(0));
				newUser.setRecordCount(recordsCount);
				newUser.setCurrentBatchId(batchId);
				
				userList.add(newUser);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error finding User", e);
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
			String sql = "SELECT username, password, firstname, lastname, email FROM user WHERE id = ?";
			stm = db.getConnection().prepareStatement(sql);
			stm.setInt(1, id);

			//Execute Query
			result = stm.executeQuery();
			
			//Handle Response to create new User
			while (result.next()) {
				String username = result.getString(0);
				String password = result.getString(1);
				String firstName = result.getString(2);
				String lastName = result.getString(3);
				String email = result.getString(4);
				
				user = new User(username, password, firstName, lastName, email);
				user.setId(id);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error finding User", e);
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
			String sql = "SELECT id, password, firstname, lastname, email FROM user WHERE username = ?";
			stm = db.getConnection().prepareStatement(sql);
			stm.setString(1, username);

			//Execute Query
			result = stm.executeQuery();
			
			//Handle Response to create new User
			while (result.next()) {
				int id = result.getInt(0);
				String password = result.getString(1);
				String firstName = result.getString(2);
				String lastName = result.getString(3);
				String email = result.getString(4);
				
				user = new User(username, password, firstName, lastName, email);
				user.setId(id);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error finding User", e);
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
			String sql = "INSERT INTO user (username, password, firstname, lastname, email) VALUES (?,?,?,?,?)";
			stm = db.getConnection().prepareStatement(sql);
			stm.setString(1, newUser.getUsername());
			stm.setString(2, newUser.getPassword());
			stm.setString(3, newUser.getFirstName());
			stm.setString(4, newUser.getLastName());
			stm.setString(5, newUser.getEmail());
			
			//Execute Query and handle the response to get back the ID
			if (stm.executeUpdate() == 1) {
				keyStm = db.getConnection().createStatement();
				result = keyStm.executeQuery("SELECT last_insert_rowid()");
				result.next();
				newUser.setId(result.getInt(1));
			}		
						
		} catch (SQLException e) {
			throw new DatabaseException("Error adding User", e);
		} 
		
		return true;
	}
	
	/**
	 * Update user in the Database
	 * @param user, User object containing new information to update
	 * @return true if the User was updated successfully, otherwise false
	 */
	public boolean updateUser(User user) throws DatabaseException{
		PreparedStatement stm = null;
		
		try {

			//Set up Query
			String sql = "UPDATE user SET current_batch = ?, records_count = ? WHERE id = ? ";
			stm = db.getConnection().prepareStatement(sql);
			stm.setInt(1, user.getCurrentBatchId());
			stm.setInt(2, user.getRecordCount());
			stm.setInt(3,user.getId());

			//Execute Query
			if (stm.executeUpdate() == 1) {
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error finding User", e);
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
			throw new DatabaseException("Error finding User", e);
		}
		
	}
	
	//-------------------------------
	//Testing
	
	public static void main(String[] args) {
		
		try {
			Database.initialize();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		
		Database db = new Database();
		
		User newUser = new User("raull91", "lopesdel91", "Raul", "Lopez", "raullopezvil@gmail.com");
		
		try {
			db.getUserDAO().insertNewUser(newUser);
			System.out.println("Added Succesfully with id " + newUser.getId() + " and username: " + newUser.getUsername());
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
}
