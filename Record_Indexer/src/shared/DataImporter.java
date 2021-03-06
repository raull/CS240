package shared;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.*;

import org.apache.commons.io.*;
import org.w3c.dom.*;

import server.database.Database;
import server.database.DatabaseException;
import shared.modal.*;

public class DataImporter {
	
	//----------------Instance Fields------------------
	private Database db;
	
	//----------------Constructors---------------------
	public DataImporter(Database db) {
		this.db = db;
	}
	
	//----------------Methods-------------------------
	
	public void importData(String filename) throws Exception
	{
		//DocumentBuilder to parse the XML file
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		File file = new File(filename);
		File dest = new File("Records");
		
		File emptydb = new File("db" + File.separator + "empty" + File.separator + "indexerDB.sqlite");
		File currentdb = new File("db" + File.separator + "indexerDB.sqlite");
		
		/*
		 * (**APACHE**)
		 */
		try
		{
			//	We make sure that the directory we are copying is not the the destination
			//	directory.  Otherwise, we delete the directories we are about to copy.
			if(!file.getParentFile().getCanonicalPath().equals(dest.getCanonicalPath()))
				FileUtils.deleteDirectory(dest);
				
			//	Copy the directories (recursively) from our source to our destination.
			FileUtils.copyDirectory(file.getParentFile(), dest);
			
			//	Overwrite my existing *.sqlite database with an empty one.  Now, my
			//	database is completely empty and ready to load with data.
			FileUtils.copyFile(emptydb, currentdb);
		}
		catch (IOException e)
		{
			throw new Exception("Unable to deal with the IOException thrown: " + e.getLocalizedMessage(), e);
		}
		/*
		 * (**APACHE**)
		 */
		
		File parsefile = new File(dest.getPath() + File.separator + file.getName());
		Document doc = builder.parse(parsefile);
		
		doc.getDocumentElement().normalize();
		
		Element root = doc.getDocumentElement();
		
		//Parse XML to convert it to Project objects and User objects
		IndexerData indexer = new IndexerData(root);
		
		//Import data to Database
		try {
			importDataToDatabaseWithIndexer(indexer);
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	//-----------------Private Methods------------------------
	
	private void importDataToDatabaseWithIndexer(IndexerData indexer) throws DatabaseException {
		
		//Start importing process
		try {			
			//Start Transaction
			db.startTransaction();
						
			//Add All Users
			
			for (User user : indexer.getUsers()) {
				importUser(user);
			}
			
			//Add All Projects
			for (Project project : indexer.getProjects()) {
				importProject(project);
			}
			
			//End Transaction
			db.endTransaction(true);
			
		} catch (DatabaseException e) {
			db.endTransaction(false);
			System.out.println("Something went wrong while importing");
			throw e;
		}
		
	}
	
	private void importProject(Project project) throws DatabaseException {
		if (db.getConnection() == null) {
			throw new DatabaseException("No connection found");
		}
		
		try {
			//Persist the project to the Database
			db.getProjectDAO().insertNewProject(project);
			
			//Persist the fields to the Database
			for (Field field : project.getFields()) {
				db.getFieldDAO().insertNewField(field);
				db.getProjectDAO().insertNewFieldIntoProject(field, project);
			}
			
			//Persist the batches to the database
			
			for (Batch batch : project.getBatches()) {
				db.getBatchDAO().insertNewBatch(batch);
				//Add the relation between batch and project
				db.getProjectDAO().insertNewBatchIntoProject(batch, project);
				
				//Persist all values for this batch
				for (Value value : batch.getValues()) {
					db.getValueDAO().insertNewValue(value);
					//Add the relation between value and batch
					db.getBatchDAO().insertValueIntoBatch(value, batch);
				}
			}
			
		} catch (Exception e) {
			throw e;
		}
	}
	
	private void importUser(User user) throws DatabaseException {
		if (db.getConnection() == null) {
			throw new DatabaseException("No connection found");
		}
		
		try {
			db.getUserDAO().insertNewUser(user);
		} catch (Exception e) {
			throw e;
		}
	}
	
	//------------------------Main Method------------------------------------
	public static void main(String[] args) {
		try {
			//Import the Database Controller and initialize a Database instance
			Database.initialize();
			Database db = new Database();
			
			//Create an importer
			DataImporter importer = new DataImporter(db);
			//Import Data from file
			importer.importData(args[0]);
			
		} catch (Exception e) {
			System.out.println("Error: " + e.getLocalizedMessage());
		}
		

	}

}
