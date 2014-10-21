package shared.communication;

import java.util.List;

import shared.modal.Field;

/**
 * Get Fields Response object so the client and the server can communicate 
 * @author Raul Lopez Villalpando
 *
 */
public class GetFields_Response {
	
	/**
	 * A list of fields returned by the server
	 */
	private List<Field> fields;
	/**
	 * Error message in case the request fails
	 */
	private String errorMessage;
	
	
	public GetFields_Response(List<Field> fields, String errorMessage) {
		this.fields = fields;
		this.errorMessage = errorMessage;
	}


	/**
	 * @return the fields
	 */
	public List<Field> getFields() {
		return fields;
	}


	/**
	 * @param fields the fields to set
	 */
	public void setFields(List<Field> fields) {
		this.fields = fields;
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
