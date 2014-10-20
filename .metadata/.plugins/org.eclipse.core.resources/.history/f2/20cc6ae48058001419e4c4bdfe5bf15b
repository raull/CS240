package shared.modal;

import java.util.ArrayList;

/**
 * Batch class represents a table like image representing the records to be indexed
 * @author Raul Lopez Villalpando, 2014
 *
 */
public class Batch {
	
	/** The id representing the Batch on the databases*/
	private int id;
	/** The path where the Batch is located*/
	private String filePath;
	/** The status of the batch represented as an integer, -1 = not started, 0 = started, 1 = completed */
	private int status;
	/** The fields representing the columns of that batch*/
	private ArrayList<Field> fields;
	/** The values on this batch */
	private ArrayList<Value> values;
	
	//Constructor
	
	public Batch(String filePath, int status) {
		this.filePath = filePath;
		this.status = status;
	}
	
	
	//Getters and Setters
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
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
	/**
	 * @return the batch status 
	 * */
	public int getStatus() {
		return this.status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}
}
