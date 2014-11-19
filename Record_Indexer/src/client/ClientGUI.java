package client;

import java.awt.EventQueue;

import javax.swing.JOptionPane;

import shared.modal.User;
import client.communication.ClientCommunicator;
import client.facade.ClientFacade;
import client.login.*;
import client.main.MainFrame;


public class ClientGUI {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
				public void run() {
					
					final String host = "localhost";
					final String port = "8080";
					
					final MainFrame mainFrame = new MainFrame();
					final LoginDialog loginDialog = new LoginDialog(mainFrame);
					loginDialog.addLoginListener(new LoginListener() {
						
						@Override
						public void userLoggedIn(User user) {
							loginDialog.setVisible(false);
							
							String welcomeMessage = "Welcome " + user.getFirstName() + " " 
									+ user.getLastName() + "\nYou have " + user.getRecordCount() + " record indexed";
							JOptionPane.showMessageDialog(mainFrame, welcomeMessage, "Welcome", JOptionPane.PLAIN_MESSAGE);
							mainFrame.setVisible(true);
						}
					});
					
					ClientCommunicator communicator = new ClientCommunicator(host, port);
					ClientFacade.setClientCommunicator(communicator);
					
					loginDialog.setVisible(true);

				}
			}
		);

	}
}