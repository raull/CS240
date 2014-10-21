package shared.communication;

import shared.modal.Batch;
/**
 * Download Batch Object so the client and Server can communicate
 * @author Raul Lopez Villalpando
 *
 */
public class DownloadBatch_Response {
	
	/**
	 * Batch returned from the Database, containing the fields as well
	 */
	private Batch batch;
	/**
	 * Message for the reason of the error
	 */
	private String errorMessage;
	
	
	public DownloadBatch_Response(Batch batch, String errorMessage) {
		this.batch = batch;
		this.errorMessage = errorMessage;
	}
	
	
	/**
	 * @return the batch
	 */
	public Batch getBatch() {
		return batch;
	}
	/**
	 * @param batch the batch to set
	 */
	public void setBatch(Batch batch) {
		this.batch = batch;
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
