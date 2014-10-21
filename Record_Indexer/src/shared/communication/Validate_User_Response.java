package shared.communication;

/**
 * Validate User response object for the client and server to communicate
 * @author Raul Lopez Villalpando
 *
 */
public class Validate_User_Response {
	
	/**
	 * Boolean that indicates whether the user is valid or not
	 */
	private boolean validUser;
	/**
	 * First name of the User
	 */
	private String firstName;
	/**
	 * Last name of the User
	 */
	private String lastName;
	/**
	 * The number of records indexed by the User
	 */
	private int recordCount;
	/**
	 * An message with the error cause
	 */
	private String errorMessage;
	
	
	public Validate_User_Response(boolean validUser, String firstName, String lastName, int recordCount, String errorMessage){
		this.validUser = validUser;
		this.firstName = firstName;
		this.lastName = lastName;
		this.recordCount = recordCount;
		this.errorMessage = errorMessage;
	}


	/**
	 * @return the validUser
	 */
	public boolean isValidUser() {
		return validUser;
	}


	/**
	 * @param validUser the validUser to set
	 */
	public void setValidUser(boolean validUser) {
		this.validUser = validUser;
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
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}


	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
	
	
}
