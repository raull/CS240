package shared.communication;

public class SubmitBatch_Response {
	
	//--------------Instance Fields-----------------
	/**
	 * Request output
	 */
	private String output = "FAILED";
	
	//--------------Constructors-------------------
	public SubmitBatch_Response(String output) {
		this.output = output;
	}
	
	public SubmitBatch_Response() {
		
	}

	//--------------Getters and Setters-------------
	/**
	 * @return the output
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
	
	//--------------To String-----------------------
	@Override
	public String toString() {
		return output;
	}
}
