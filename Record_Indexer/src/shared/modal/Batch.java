package shared.modal;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import shared.IndexerData;

/**
 * Batch class represents a table like image representing the records to be indexed
 * @author Raul Lopez Villalpando, 2014
 *
 */
public class Batch {
	
	/** The id representing the Batch on the databases*/
	private int id;
	/** The path where the Batch is located*/
	private String filePath = "";
	/** The status of the batch represented as an integer, -1 = not started, 0 = started, 1 = completed */
	private int status;
	/** The values on this batch */
	private transient ArrayList<Value> values = new ArrayList<Value>();
	/** The project id that the batch belongs to*/
	private int projectId;
	
	//Constructor
	public Batch() {
		
	}
	
	
	public Batch(String filePath, int status) {
		this.filePath = filePath;
		this.status = status;
	}
	
	public Batch(Element batchElement) {
		filePath = IndexerData.getValue((Element)batchElement.getElementsByTagName("file").item(0));
		
		//Records are optional so check if they exist
		NodeList recordsElementNodeList = batchElement.getElementsByTagName("records");
		
		if (recordsElementNodeList.getLength() > 0) {
			Element recordsElement = (Element)recordsElementNodeList.item(0);
			NodeList allRecords = recordsElement.getElementsByTagName("record");
						
			for (int i = 0; i < allRecords.getLength(); i++) {
				Element recordElement = (Element)allRecords.item(i);
				
				Element valuesElement = (Element)(recordElement.getElementsByTagName("values").item(0));
				NodeList allValuesForRecord = valuesElement.getElementsByTagName("value");
				
				for (int j = 0; j < allValuesForRecord.getLength(); j++) {
					Value newValue = new Value((Element)allValuesForRecord.item(j));
					newValue.setRowNumber(i+1);
					newValue.setColNumber(j+1);
					values.add(newValue);
				}
			}
			this.status = 2;
		}
	}
	
	//Methods
	
	public String toString() {
		return "Batch:\n\tFile Path: " + filePath + 
				"\n\tValues: \n" + values;
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
