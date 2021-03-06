package shared.communication;

import java.util.List;

import shared.modal.Field;

/**
 * Get Fields Response object so the client and the server can communicate 
 * @author Raul Lopez Villalpando
 *
 */
public class GetFields_Response {
	
	
	//-------------Instance Fields ------------------
	/**
	 * A list of fields returned by the server
	 */
	private List<Field> fields;
	/**
	 * Error message in case the request fails
	 */
	private String output;
	
	//-------------Constructors----------------------
	
	public GetFields_Response(List<Field> fields, String output) {
		this.fields = fields;
		this.output = output;
	}
	
	public GetFields_Response() {
		
	}

	//------------Getters and Setters----------------
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
	
	//--------------To String --------------
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		if (output.equals("TRUE")) {
			for (Field field : fields) {
				builder.append(field.getProject_id() + "\n");
				builder.append(field.getId() + "\n");
				builder.append(field.getTitle() + "\n");
			}
		} else {
			builder.append(output + "\n");
		}
		
		return builder.toString();
	}
}
