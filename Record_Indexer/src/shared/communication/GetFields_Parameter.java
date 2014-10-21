package shared.communication;

public class GetFields_Parameter {
	
	/**
	 * Username
	 */
	private String username;
	/**
	 * Password
	 */
	private String password;
	/**
	 * Project ID
	 */
	private int projectId;
	/**
	 * Empty String
	 */
	private String projectString;
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
	 * @return the projectId
	 */
	public int getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	/**
	 * @return the projectString
	 */
	public String getProjectString() {
		return projectString;
	}
	/**
	 * @param projectString the projectString to set
	 */
	public void setProjectString(String projectString) {
		this.projectString = projectString;
	}

}
