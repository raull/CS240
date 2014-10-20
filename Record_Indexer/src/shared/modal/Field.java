package shared.modal;

/**
 * Field class that represents a column in a batch
 * @author Raul Lopez Villalpando, 2014
 *
 */
public class Field {
	
	/** The id of the Field represented in the Database*/
	private int id;
	/** The title of the field*/
	private String title;
	/** The x-coordinate of the field where it starts on the batch*/
	private int xCoord;
	/** The column number in the batch*/
	private int colNumber;
	/** The width in pixels of the field*/
	private int width;
	/** The path of the HTML link that contains info about the field*/
	private String helpHTML;
	/** The path of the file that contains known words that could help to correct mispelled words*/
	private String knownData;
	
	//Constructors
	
	public Field(String title, int xCoord, int colNumber, int width, String helpHTML, String knownData) {
		this.title = title;
		this.xCoord = xCoord;
		this.colNumber = colNumber;
		this.width = width;
		this.helpHTML = helpHTML;
		this.knownData = knownData;
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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * @return the xCoord
	 */
	public int getxCoord() {
		return xCoord;
	}
	/**
	 * @return the colNumber
	 */
	public int getColNumber() {
		return colNumber;
	}
	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	/**
	 * @return the helpHtml
	 */
	public String getHelpHtml() {
		return helpHTML;
	}
	/**
	 * @param helpHtml the helpHtml to set
	 */
	public void setHelpHtml(String helpHtml) {
		this.helpHTML = helpHtml;
	}
	/**
	 * @return the knownData
	 */
	public String getKnownData() {
		return knownData;
	}
	/**
	 * @param knownData the knownData to set
	 */
	public void setKnownData(String knownData) {
		this.knownData = knownData;
	}
	
}
