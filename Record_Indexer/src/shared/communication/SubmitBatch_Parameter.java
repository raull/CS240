package shared.communication;

import java.util.List;
import shared.modal.*;

public class SubmitBatch_Parameter {
	
	/**
	 * Values to submit to the batch (Batch ID inside Value object as well as the field ID)
	 */
	private List<Value> values;
	/**
	 * Username of the user submitting 
	 */
	private String username;
	/**
	 * Password of the user submitting
	 */
	private String password;
	
	
	
	public SubmitBatch_Parameter(List<Value> values, String username, String password) {
		this.values = values;
		this.username = username;
		this.password = password;
	}



	/**
	 * @return the values
	 */
	public List<Value> getValues() {
		return values;
	}



	/**
	 * @param values the values to set
	 */
	public void setValues(List<Value> values) {
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
}
