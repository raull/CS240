package shared.communication;

/**
 * Validate User response object for the client and server to communicate
 * @author Raul Lopez Villalpando
 *
 */
public class Validate_User_Response {
	
	//--------------Instance Fields------------------
	/**
	 * First name of the User
	 */
	private String firstName = "";
	/**
	 * Last name of the User
	 */
	private String lastName = "";
	/**
	 * The number of records indexed by the User
	 */
	private int recordCount;
	/**
	 * An message with the error cause
	 */
	private String output = "FALSE";
	
	//-----------------Constructors------------------
	
	public Validate_User_Response(String firstName, String lastName, int recordCount, String output){
		this.firstName = firstName;
		this.lastName = lastName;
		this.recordCount = recordCount;
		this.output = output;
	}

	public Validate_User_Response() {
		
	}
	
	//-----------------getters and Setters-----------

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
	public String getOutput() {
		return output;
	}


	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setOutput(String output) {
		this.output = output;
	}
	
	//---------------To String---------------------
	
	@Override
	public String toString() {
		
		StringBuilder builder = new StringBuilder();
		if (output.equals("TRUE")) {
			builder.append(output + "\n");
			builder.append(firstName + "\n");
			builder.append(lastName + "\n");
			builder.append(recordCount + "\n");
		} else {
			builder.append(output + "\n");
		}
		
		return builder.toString();
	}
	
	
}
