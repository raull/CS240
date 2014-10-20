package shared.modal;

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
	
	
	//Constructors
	public Value(String content, int rowNumber) {
		this.content = content;
		this.rowNumber = rowNumber;
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

	
	
}
