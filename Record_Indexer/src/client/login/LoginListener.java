package client.login;

import shared.modal.User;

public interface LoginListener {
	
	/**
	 * Notifies that a user has been logged in
	 * @param user
	 */
	public void userLoggedIn(User user);
			
}
