package shared.communication;

public class SubmitBatch_Response {
	
	/**
	 * Request Response
	 */
	private String response;
	
	
	public SubmitBatch_Response(String response) {
		this.response = response;
	}


	/**
	 * @return the response
	 */
	public String getResponse() {
		return response;
	}


	/**
	 * @param response the response to set
	 */
	public void setResponse(String response) {
		this.response = response;
	}
	
	
}
