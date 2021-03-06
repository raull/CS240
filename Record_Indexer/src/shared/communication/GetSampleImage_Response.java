package shared.communication;

/**
 * Get Sample Image Response object so the client and server can communicate
 * @author Raul Lopez Villalpando
 *
 */
public class GetSampleImage_Response {

	//------------------Instance Fields-------------------------
	/**
	 * URL where the image is located
	 */
	private String imageURL = "";
	/**
	 * Error message in case it fails
	 */
	private String output = "FAILED";
	
	//------------------Constructors-------------------------
	public GetSampleImage_Response(String imageURL, String output) {
		this.imageURL = imageURL;
		this.output = output;
	}
	
	public GetSampleImage_Response() {
	}


	//------------------Getters and Setters-------------------------
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
	
	//------------------To String-------------------------
	
	@Override
	public String toString() {
		if (output.equals("TRUE")) {
			return imageURL;
		} else {
			return output + "\n";
		}
	}
}
