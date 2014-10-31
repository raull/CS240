package shared.communication;

import java.util.ArrayList;
import java.util.List;

/**
 * Search Input object so the client and server can communicate
 * @author Raul Lopez Villalpando
 *
 */
public class Search_Input {
	
	private String username;
	private String password;
	private List<Integer> fields;
	private List<String> values;
	
	
	public Search_Input(String username, String password, List<Integer> fields, List<String> values) {
		this.username = username;
		this.password = password;
		this.fields = fields;
		this.values = values;
	}
	
	public Search_Input(String username, String password, String fields, String values) {
		this.username = username;
		this.password = password;
		
		String[] valueList =  values.split(",");
		ArrayList<String> valueArrayList = new ArrayList<String>();
		for (String string : valueList) {
			valueArrayList.add(string);
		}
		
		this.values = valueArrayList;
		
		String[] fieldList =  fields.split(",");
		ArrayList<Integer> fieldArray = new ArrayList<Integer>();
		for (String string : fieldList) {
			fieldArray.add(Integer.parseInt(string));
		}
		
		this.fields = fieldArray;
	}


	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}


	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}


	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}


	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}


	/**
	 * @return the fields
	 */
	public List<Integer> getFields() {
		return fields;
	}


	/**
	 * @param fields the fields to set
	 */
	public void setFields(List<Integer> fields) {
		this.fields = fields;
	}


	/**
	 * @return the values
	 */
	public List<String> getValues() {
		return values;
	}


	/**
	 * @param values the values to set
	 */
	public void setValues(List<String> values) {
		this.values = values;
	}

}
