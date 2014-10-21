package shared.communication;

/**
 * Get Sample Image Response object so the client and server can communicate
 * @author Raul Lopez Villalpando
 *
 */
public class GetSampleImage_Response {

	/**
	 * URL where the image is located
	 */
	private String imageURL;
	/**
	 * Error message in case it fails
	 */
	private String errorMessage;
	
	
	public GetSampleImage_Response(String imageURL, String errorMessage) {
		this.imageURL = imageURL;
		this.errorMessage = errorMessage;
	}


	/**
	 * @return the imageURL
	 */
	public String getImageURL() {
		return imageURL;
	}


	/**
	 * @param imageURL the imageURL to set
	 */
	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
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
