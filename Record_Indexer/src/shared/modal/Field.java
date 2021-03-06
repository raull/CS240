package shared.modal;

import java.io.Serializable;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import shared.IndexerData;

/**
 * Field class that represents a column in a batch
 * @author Raul Lopez Villalpando, 2014
 *
 */
@SuppressWarnings("serial")
public class Field implements Serializable{
	
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
	/** The path of the file that contains known words that could help to correct misspelled words*/
	private String knownData;
	/** The project if where the field belongs to*/
	private int project_id;
	
	//Constructors
	
	public Field(String title, int xCoord, int colNumber, int width, String helpHTML, String knownData) {
		this.title = title;
		this.xCoord = xCoord;
		this.colNumber = colNumber;
		this.width = width;
		this.helpHTML = helpHTML;
		this.knownData = knownData;
	}
	
	public Field(Element fieldElement) {
		title = IndexerData.getValue((Element)fieldElement.getElementsByTagName("title").item(0));
		
		xCoord = Integer.parseInt(IndexerData.getValue((Element)fieldElement.getElementsByTagName("xcoord").item(0)));
		
		width = Integer.parseInt(IndexerData.getValue((Element)fieldElement.getElementsByTagName("width").item(0)));
		
		helpHTML = IndexerData.getValue((Element)fieldElement.getElementsByTagName("helphtml").item(0));
		
		//This attribute is optional on the XML
		
		NodeList knownDataList = fieldElement.getElementsByTagName("knowndata");
		
		if (knownDataList != null) {
			knownData = IndexerData.getValue((Element)knownDataList.item(0));
		}
	}
	
	//Methods
	
	public String toString() {
		return "Field:\n\tTitle: " + title + 
				"\n\txCoord: " + xCoord + 
				"\n\tcolNumber: " + colNumber + 
				"\n\tWidth: " + width + 
				"\n\tHelp HTML: " + helpHTML + 
				"\n\tKnown Data: " + knownData;
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

	/**
	 * @param colNumber the colNumber to set
	 */
	public void setColNumber(int colNumber) {
		this.colNumber = colNumber;
	}

	/**
	 * @return the project_id
	 */
	public int getProject_id() {
		return project_id;
	}

	/**
	 * @param project_id the project_id to set
	 */
	public void setProject_id(int project_id) {
		this.project_id = project_id;
	}
	
}
