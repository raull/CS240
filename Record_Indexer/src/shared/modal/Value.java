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
	
	//Getters and Setters
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
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

	
	
}
