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
}
