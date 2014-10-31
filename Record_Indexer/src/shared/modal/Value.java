package shared.modal;

import org.w3c.dom.Element;

import shared.IndexerData;

/**
 * Value class represents a value on and specific row and column on a batch
 * @author Raul Lopez Villalpando, 2014
 *
 */
public class Value {
	
	/** The id of the Value represented on the Database */
	private int id;
	/** The content of the value cell*/
	private String content;
	/** The row number in the batch*/
	private int rowNumber;
	/** The column position on the batch*/
	private int colNumber;
	/** The batch where the value belongs to*/
	private int batchId;
	/** The project where the value belongs to*/
	private int projectId;
	
	
	//Constructors
	public Value(String content, int rowNumber, int colNumber) {
		this.content = content;
		this.rowNumber = rowNumber;
		this.colNumber = colNumber;
	}
	
	public Value(Element valueElement) {
		content = IndexerData.getValue(valueElement);
	}
	
	//Methods
	
	public String toString() {
		return "Value:\n\tContent: " + this.content + "\n\tRow: " + this.rowNumber;
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
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * @return the rowNumber
	 */
	public int getRowNumber() {
		return rowNumber;
	}
	/**
	 * @param rowNumber the rowNUmber to set
	 */
	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	/**
	 * @return the colNumber
	 */
	public int getColNumber() {
		return colNumber;
	}

	/**
	 * @param colNumber the colNumber to set
	 */
	public void setColNumber(int colNumber) {
		this.colNumber = colNumber;
	}

	/**
	 * @return the batchId
	 */
	public int getBatchId() {
		return batchId;
	}

	/**
	 * @param batchId the batchId to set
	 */
	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}

	/**
	 * @return the projectId
	 */
	public int getProjectId() {
		return projectId;
	}

	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	
	
}
