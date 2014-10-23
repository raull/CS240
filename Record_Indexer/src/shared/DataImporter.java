package shared;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import server.Database;
import server.DatabaseException;
import shared.modal.*;

public class DataImporter {
	
	//----------------Instance Fields------------------
	private Database db;
	
	//----------------Constructors---------------------
	public DataImporter(Database db) {
		this.db = db;
	}
	
	//---------------Static Methods--------------------
	public static String getValue(Element element) {
		if (element == null) {
			return null;
		}
		
		String result = "";
		Node child = element.getFirstChild();
		try {
			result = child.getNodeValue();
		} catch (DOMException e) {
			return null;
		}
		
		return result;
	}
	
	public static ArrayList<Element> getChildElements(Node node) {
		ArrayList<Element> result = new ArrayList<Element>();
		
		NodeList children = node.getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if(child.getNodeType() == Node.ELEMENT_NODE){
				result.add((Element)child);
			}
		}
		return result;
	}
	
	//-----------------Private Methods------------------------
	private void clearAllTables() throws DatabaseException{
		
		if (db.getConnection() == null) {
			throw new DatabaseException("No connection found");
		}
		
		try {
			//Clear Tables
			List<User> allUsers = db.getUserDAO().getAll();
			List<Project> allProjects = db.getProjectDAO().getAll();
			List<Field> allFields = db.getFieldDAO().getAll();
			List<Batch> allBatches = db.getBatchDAO().getAll();
			List<Value> allValues = db.getValueDAO().getAll();
			
			for (User user : allUsers) {
				db.getUserDAO().deleteUserWithId(user.getId());
			}
			
			for (Project project : allProjects) {
				db.getProjectDAO().deleteProjectWithId(project.getId());
			}
			
			for (Field field : allFields) {
				db.getFieldDAO().deleteFieldWithId(field.getId());
			}
			
			for (Batch batch : allBatches) {
				db.getBatchDAO().deleteBatchWithId(batch.getId());
			}
			
			for (Value value : allValues) {
				db.getValueDAO().deleteValueWithId(value.getId());
			}
		} catch (Exception e) {
			throw new DatabaseException(e);
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
			throw new DatabaseException(e);
		}
	}
	
	private void importUser(User user) throws DatabaseException {
		if (db.getConnection() == null) {
			throw new DatabaseException("No connection found");
		}
		
		try {
			db.getUserDAO().insertNewUser(user);
		} catch (Exception e) {
			throw new DatabaseException(e);
		}
	}
	
	//------------------------Main Method------------------------------------
	public static void main(String[] args) {
		File xmlFile = new File("Records.xml");
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			
			doc.getDocumentElement().normalize();
			
			Element root = doc.getDocumentElement();
			
			//Parse XML
			IndexerData indexer = new IndexerData(root);
			
			//Start importing process
			Database db = new Database();

			try {
				Database.initialize();
				
				DataImporter importer = new DataImporter(db);
				
				db.startTransaction();
				
				//Clear Tables
				importer.clearAllTables();
				
				//Add All Users
				
				for (User user : indexer.getUsers()) {
					importer.importUser(user);
				}
				
				//Add All Projects
				for (Project project : indexer.getProjects()) {
					importer.importProject(project);
				}
				
				
				db.endTransaction(true);
			} catch (Exception e) {
				db.endTransaction(false);
				System.out.println("Something went wrong while importing");
			}
			
			
		} catch (ParserConfigurationException e) {
			System.out.println("Error creating DocumentBuilder");
		} catch (Exception e) {
			System.out.println("Error with XML file: + " + e.getLocalizedMessage());
		}
		

	}

}