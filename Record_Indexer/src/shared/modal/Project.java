package shared.modal;

import java.util.ArrayList;

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
	private ArrayList<Batch> batches;
	/** The list of fields in each batch*/
	private ArrayList<Field> fields;
	/** All the values from all the batches */
	private ArrayList<Value> values;
	
	// Getters and Setters
	
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
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
	/**
	 * @return the values
	 */
	public ArrayList<Value> getValues() {
		return values;
	}
	/**
	 * @param values the values to set
	 */
	public void setValues(ArrayList<Value> values) {
		this.values = values;
	}
	
}
