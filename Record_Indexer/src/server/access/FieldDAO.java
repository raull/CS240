package server.access;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import server.database.Database;
import server.database.DatabaseException;
import shared.modal.*;

public class FieldDAO {
	
	//Instance Fields
	/**
	 * Database object to start and end transactions
	 */
	private Database db;
	
	//-----------------------------------------------------
	//Constructors
	
	public FieldDAO(Database db) {
		this.db = db;
	}
	
	//-----------------------------------------------------
	//Methods
	
	/**
	 * Find all the Field in the Database
	 * @return A list with all the Field in the Database
	 * */
	public List<Field> getAll() throws DatabaseException {
		PreparedStatement stm = null;
		ResultSet result = null;
				
		ArrayList<Field> fieldList = new ArrayList<Field>();
		try {			
			//Set up Query
			String sql = "SELECT * FROM field";
			stm = db.getConnection().prepareStatement(sql);

			//Execute Query
			result = stm.executeQuery();
			
			//Handle Response to create new User
			while (result.next()) {

				String title = result.getString(2);
				int xCoord = result.getInt(3);
				int colNumber = result.getInt(4);
				int width = result.getInt(5);
				String helpHTML = result.getString(6);
				String knownData = result.getString(7);
				int project_id = result.getInt(8);
				
				Field newField = new Field(title, xCoord, colNumber, width, helpHTML, knownData);
				newField.setId(result.getInt(1));
				newField.setProject_id(project_id);
				
				fieldList.add(newField);
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error getting all Fields: "+ e.getLocalizedMessage(), e);
		} 
				
		return fieldList;
	}
	
	
	/**
	 * Find a Field by providing the Field's ID
	 * @param id of the field to find
	 * @return A Field matching the provided ID
	 */
	public Field findById(int id) throws DatabaseException {
		PreparedStatement stm = null;
		ResultSet result = null;
		
		Field field = null;
		
		try {			
			//Set up Query
			String sql = "SELECT * FROM field WHERE id = ?";
			stm = db.getConnection().prepareStatement(sql);
			stm.setInt(1, id);

			//Execute Query
			result = stm.executeQuery();
			
			//Handle Response to create new User
			while (result.next()) {
				int fieldId = result.getInt(1);
				String title = result.getString(2);
				int xCoord = result.getInt(3);
				int colNumber = result.getInt(4);
				int width = result.getInt(5);
				String helpHTML = result.getString(6);
				String knownData = result.getString(7);
				int projectId = result.getInt(8);
				
				field = new Field(title, xCoord, colNumber, width, helpHTML, knownData);
				field.setId(fieldId);
				field.setProject_id(projectId);
				
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error finding Field: " + e.getLocalizedMessage(), e);
		} 	
		
		return field;
	}
	
	/**
	 * Insert a new Field to the Database
	 * @param newField, a Field object representing the new Field to persist
	 * @return true if the Field was inserted successfully, otherwise false
	 */
	public boolean insertNewField(Field newField) throws DatabaseException {
		PreparedStatement stm = null;
		ResultSet result = null;
		Statement keyStm = null; 
				
		try {
			//Set up Query
			String sql = "INSERT INTO field (title, x_coord, column_number, width, help_html, known_data) VALUES (?,?,?,?,?,?)";
			stm = db.getConnection().prepareStatement(sql);
			stm.setString(1, newField.getTitle());
			stm.setInt(2, newField.getxCoord());
			stm.setInt(3, newField.getColNumber());
			stm.setInt(4, newField.getWidth());
			stm.setString(5, newField.getHelpHtml());
			stm.setString(6, newField.getKnownData());
			
			//Execute Query and handle the response to get back the ID
			if (stm.executeUpdate() == 1) {
				keyStm = db.getConnection().createStatement();
				result = keyStm.executeQuery("SELECT last_insert_rowid()");
				result.next();
				newField.setId(result.getInt(1));
				
				return true;
			} else {
				return false;
			}
						
		} catch (SQLException e) {
			throw new DatabaseException("Error adding Field: " + e.getLocalizedMessage(), e);
		} 
	}
	
	/**
	 * Update Field in the Database
	 * @param field, Field object containing new information to update
	 * @return true if the Field was updated successfully, otherwise false
	 */
	public boolean updateField(Field field) throws DatabaseException {
		PreparedStatement stm = null;
		
		try {

			//Set up Query
			String sql = "UPDATE field SET title = ?, x_coord = ?, column_number = ?, width = ?, help_html = ?, known_data = ? WHERE id = ? ";
			stm = db.getConnection().prepareStatement(sql);
			stm.setString(1, field.getTitle());
			stm.setInt(2, field.getxCoord());
			stm.setInt(3, field.getColNumber());
			stm.setInt(4, field.getWidth());
			stm.setString(5, field.getHelpHtml());
			stm.setString(6, field.getKnownData());
			stm.setInt(7, field.getId());

			//Execute Query
			if (stm.executeUpdate() == 1) {
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error updating Field: " + e.getLocalizedMessage(), e);
		} 
	}
	
	/**
	 * Delete Field by providing the Field's ID
	 * @param id representing the Field's ID
	 * @return true if the Field was deleted successfully, otherwise false
	 */
	public boolean deleteFieldWithId(int id) throws DatabaseException {
		PreparedStatement stm = null;
		
		try {
			//Set up Query
			String sql = "DELETE FROM field WHERE id = ? ";
			stm = db.getConnection().prepareStatement(sql);
			stm.setInt(1, id);

			//Execute Query
			if (stm.executeUpdate() == 1) {
				return true;
			} else {
				return false;
			}
			
		} catch (SQLException e) {
			throw new DatabaseException("Error deleting Field: " + e.getLocalizedMessage(), e);
		}
	}
}
