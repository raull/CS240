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
	
	
}
