package shared.modal;

import org.w3c.dom.Element;

import shared.IndexerData;

/**
 * User class representing users volunteering for record indexing
 * @author Raul Lopez Villalpando
 *
 */
public class User {
	
	/** The id of the USer represented in the database*/
	private int id;
	/** The username of the user*/
	private String username;
	/** The password of the user*/
	private String password;
	/** First name of the user*/
	private String firstName;
	/** Last name of the user*/
	private String lastName;
	/** User's email*/
	private String email;
	/** Number of records indexed by the user*/
	private int recordCount;
	/** Current batch that the user last worked on*/
	private int currentBatchId;
	
	//Constructors
	
	public User(String username, String password, String firstName, String lastName, String email, int recordCount) {
		this.id = -1;
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.recordCount = recordCount;
		this.currentBatchId = 0;
	}
	
	public User(Element userElement) {
		username = IndexerData.getValue((Element)userElement.getElementsByTagName("username").item(0));
		password = IndexerData.getValue((Element)userElement.getElementsByTagName("password").item(0));
		firstName = IndexerData.getValue((Element)userElement.getElementsByTagName("firstname").item(0));
		lastName = IndexerData.getValue((Element)userElement.getElementsByTagName("lastname").item(0));
		email = IndexerData.getValue((Element)userElement.getElementsByTagName("email").item(0));
		recordCount = Integer.parseInt(IndexerData.getValue((Element)userElement.getElementsByTagName("indexedrecords").item(0)));
		currentBatchId = 0;
	}
	
	
	//Getters and Setters
	
	/**
	 * @category Getters and Setters
	 */
	
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	public void setId(int newId) {
		this.id = newId;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the recordCount
	 */
	public int getRecordCount() {
		return recordCount;
	}
	/**
	 * @param recordCount the recordCount to set
	 */
	public void setRecordCount(int recordCount) {
		this.recordCount = recordCount;
	}
	/**
	 * @return the currentBatch
	 */
	public int getCurrentBatchId() {
		return currentBatchId;
	}
	/**
	 * @param currentBatch the currentBatch to set
	 */
	public void setCurrentBatchId(int currentBatchId) {
		this.currentBatchId = currentBatchId;
	}
	
	
}
