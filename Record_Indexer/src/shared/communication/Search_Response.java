package shared.communication;

import java.util.ArrayList;
import java.util.List;

/**
 * Search response object so the client and the server can communicate
 * @author Raul Lopez Villalpando
 *
 */
public class Search_Response {
	
	
	//-----------------------Instance Fields--------------------------------
	/**
	 * Tuple representing the answer, (BATCH_ID, IMAGE_URL, RECORD_NUM, FIELD_ID)
	 */
	private List<String> tuples;
	/**
	 * Error message in case the request failed
	 */
	private String output;
	
	//----------------------Instance Fields----------------------------------
	public Search_Response(List<String> tuples, String output) {
		this.tuples = tuples;
		this.output = output;
	}
	
	public Search_Response() {
		this.tuples = new ArrayList<>();
	}

	//----------------------Getters and Setters------------------------------
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
	
	//-------------------To String--------------
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		
		if (output.equals("TRUE")) {
			for (String tuple : tuples) {
				String[] params = tuple.split(",");
				for (String string : params) {
					builder.append(string + "\n");
				}
			}
		} else {
			builder.append(output + "\n");
		}
		
		return builder.toString();
	}
}
