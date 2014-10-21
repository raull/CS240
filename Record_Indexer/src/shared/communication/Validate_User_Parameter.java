package shared.communication;

/**
 * Validate User parameter object so the client and server can communicate
 * @author Raul Lopez Villalpando
 *
 */
public class Validate_User_Parameter {
	
	/**
	 * The username of the User to validate
	 */
	private String username;
	/**
	 * The password of the User to validate
	 */
	private String password;
	
	
	
	public Validate_User_Parameter(String username, String password) {
		this.username = username;
		this.password = password;
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
}
