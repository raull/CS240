package shared.communication;

import java.util.List;

/**
 * Search response object so the client and the server can communicate
 * @author Raul Lopez Villalpando
 *
 */
public class Search_Response {
	
	/**
	 * Tuple representing the answer, (BATCH_ID, IMAGE_URL, RECORD_NUM, FIELD_ID)
	 */
	private List<String> tuples;
	/**
	 * Error message in case the request failed
	 */
	private String errorMessage;

}
