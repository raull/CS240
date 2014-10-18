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
}
