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
	
	
	
}
