package client;

import java.awt.EventQueue;

import javax.swing.JOptionPane;

import shared.modal.User;
import client.communication.ClientCommunicator;
import client.facade.ClientFacade;
import client.login.*;
import client.main.MainFrame;
import client.main.MainFrameListener;


public class ClientGUI {

	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
				public void run() {
					
					String host; 
					String port; 
					
					if (args.length >= 2) {
						host = args[0];
						port = args[1];
					} else {
						host = "localhost";
						port = "8080";
					}
					
					
					
					final MainFrame mainFrame = new MainFrame();
					final LoginDialog loginDialog = new LoginDialog(mainFrame);

					mainFrame.addMainFrameListener(new MainFrameListener() {
						
						@Override
						public void logoutPressed() {
							mainFrame.setVisible(false);
							loginDialog.setVisible(true);
						}
					});
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
