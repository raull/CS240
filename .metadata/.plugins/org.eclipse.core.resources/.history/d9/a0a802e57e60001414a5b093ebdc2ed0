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
	private String output;
	
	
	public GetFields_Response(List<Field> fields, String output) {
		this.fields = fields;
		this.output = output;
	}
	
	public GetFields_Response() {
		
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
	public String getOutput() {
		return output;
	}


	/**
	 * @param output the output to set
	 */
	public void setOutput(String output) {
		this.output = output;
	}
}
