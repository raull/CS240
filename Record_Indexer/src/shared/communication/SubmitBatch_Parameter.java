package shared.communication;

public class SubmitBatch_Parameter {
	
	/**
	 * Values to submit to the batch 
	 */
	private String values;
	/**
	 * Username of the user submitting 
	 */
	private String username;
	/**
	 * Password of the user submitting
	 */
	private String password;
	/**
	 * The batch id where the values will be inserted
	 */
	private int batchId;
	
	
	
	public SubmitBatch_Parameter(String username, String password, String values, int batchId) {
		this.values = values;
		this.username = username;
		this.password = password;
		this.batchId = batchId;
	}
	


	/**
	 * @return the values
	 */
	public String getValues() {
		return values;
	}



	/**
	 * @param values the values to set
	 */
	public void setValues(String values) {
		this.values = values;
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
}
