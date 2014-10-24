package shared.modal;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import shared.IndexerData;

/**
 * The project class which represents a set of batches
 * @author Raul Lopez Villalpando
 *
 */
public class Project {
	/** The id representing the project in the database*/
	private int id;
	/** The title of the project*/
	private String title;
	/** The number of records(rows) per batches*/
	private int recordsPerBatch;
	/** The first y-coordinate of the project*/
	private int firstYCood;
	/** The height of each record in the project*/
	private int recordHeight;
	/** The list of batches of the project*/
	private ArrayList<Batch> batches = new ArrayList<Batch>();
	/** The list of fields in each batch*/
	private ArrayList<Field> fields = new ArrayList<Field>();
	
	//Constructors
	
	public Project(String title, int recordsPerBatch, int firstYCood, int recordHeight) {
		this.title = title;
		this.recordsPerBatch = recordsPerBatch;
		this.firstYCood = firstYCood;
		this.recordHeight = recordHeight;
	}
	
	public Project(Element projectElement) {
		
		title = IndexerData.getValue((Element)projectElement.getElementsByTagName("title").item(0));
		
		recordsPerBatch = Integer.parseInt(IndexerData.getValue((Element)projectElement.getElementsByTagName("recordsperimage").item(0)));
		
		firstYCood = Integer.parseInt(IndexerData.getValue((Element)projectElement.getElementsByTagName("firstycoord").item(0)));
		
		recordHeight = Integer.parseInt(IndexerData.getValue((Element)projectElement.getElementsByTagName("recordheight").item(0)));
		
		Element fieldsElement = (Element)projectElement.getElementsByTagName("fields").item(0);
		NodeList fieldElements = fieldsElement.getElementsByTagName("field");
		 
		for(int i = 0; i < fieldElements.getLength(); i++) {
			Field newField = new Field((Element)fieldElements.item(i));
			newField.setColNumber(i+1);
			fields.add(newField);
		}
		 
		Element batchesElement = (Element)projectElement.getElementsByTagName("images").item(0);
		NodeList allBatches = batchesElement.getElementsByTagName("image");
		 
		for(int i = 0; i < allBatches.getLength(); i++) {
			batches.add(new Batch((Element)allBatches.item(i)));
		}
	}
	
	//Methods 
	
	public String toString() {
		return "Project:\n\tTitle: " + title +
				"\n\tRecords Per Batch: " + recordsPerBatch + 
				"\n\tFirst Y Coord: " + firstYCood + 
				"\n\tRecord Height: " + recordHeight +
				"\n\tFields: \n" + fields + 
				"\n\tBatches: \n" + batches;
	}
	
	// Getters and Setters
	
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * @return the recordsPerBatch
	 */
	public int getRecordsPerBatch() {
		return recordsPerBatch;
	}
	/**
	 * @param recordsPerBatch the recordsPerBatch to set
	 */
	public void setRecordsPerBatch(int recordsPerBatch) {
		this.recordsPerBatch = recordsPerBatch;
	}
	/**
	 * @return the firstYCood
	 */
	public int getFirstYCood() {
		return firstYCood;
	}
	/**
	 * @param firstYCood the firstYCood to set
	 */
	public void setFirstYCood(int firstYCood) {
		this.firstYCood = firstYCood;
	}
	/**
	 * @return the recordHeight
	 */
	public int getRecordHeight() {
		return recordHeight;
	}
	/**
	 * @param recordHeight the recordHeight to set
	 */
	public void setRecordHeight(int recordHeight) {
		this.recordHeight = recordHeight;
	}
	/**
	 * @return the batches
	 */
	public ArrayList<Batch> getBatches() {
		return batches;
	}
	/**
	 * @param batches the batches to set
	 */
	public void setBatches(ArrayList<Batch> batches) {
		this.batches = batches;
	}
	/**
	 * @return the fields
	 */
	public ArrayList<Field> getFields() {
		return fields;
	}
	/**
	 * @param fields the fields to set
	 */
	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}

	
}
