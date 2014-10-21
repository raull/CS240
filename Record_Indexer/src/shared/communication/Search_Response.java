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
	
	
	public Search_Response(List<String> tuples, String errorMessage) {
		this.tuples = tuples;
		this.errorMessage = errorMessage;
	}


	/**
	 * @return the tuples
	 */
	public List<String> getTuples() {
		return tuples;
	}


	/**
	 * @param tuples the tuples to set
	 */
	public void setTuples(List<String> tuples) {
		this.tuples = tuples;
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
