package client.login;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import shared.modal.User;
import client.batch.state.BatchState;
import client.facade.ClientFacade;

@SuppressWarnings("serial")
public class LoginDialog extends JDialog{
	
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JButton loginButton;
	private JButton cancelButton;
	
	private ArrayList<LoginListener> listeners = new ArrayList<LoginListener>();
		
	public LoginDialog(Frame owner) {
		super(owner, true);
		
		this.setTitle("Login");
		this.setupGUI();
	}
	
	
	private void setupGUI() {
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		this.setSize(new Dimension(350, 130));
		this.setMaximumSize(this.getSize());
		this.setMinimumSize(this.getSize());
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		//Set JPanel for username
		JPanel usernamePanel = new JPanel();
		usernamePanel.setPreferredSize(new Dimension(this.getSize().width, 20));
		usernamePanel.add(new JLabel("Username"));
		
		usernameField = new JTextField(20);
		usernamePanel.add(usernameField);
		
		//Set JPanel for password
		JPanel passwordPanel = new JPanel();
		passwordPanel.setPreferredSize(new Dimension(this.getSize().width, 20));
		passwordPanel.add(new JLabel("Password"));
		
		passwordField = new JPasswordField(20);
		passwordPanel.add(passwordField);
		
		
		//Set JPanel for buttons
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(this.getSize().width, 30));
		loginButton  = new JButton("Login");
		loginButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					User user = ClientFacade.validateUser(usernameField.getText(), new String(passwordField.getPassword()));
					if (user == null) {
						loginFailed("Wrong Username and Password combination");
					} else {
						for (LoginListener loginListener : listeners) {
							BatchState.load(user.getUsername());
							loginListener.userLoggedIn(user);
						}
					}
					
				} catch (Exception e2) {
					loginFailed(e2.getMessage());
				}
				
				
				
			}
		});
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		buttonPanel.add(loginButton);
		buttonPanel.add(cancelButton);
		
		
		this.add(usernamePanel);
		this.add(passwordPanel);
		this.add(buttonPanel);
		
		//Window closing
		this.addWindowListener(new WindowAdapter() {
			@Override
		    public void windowClosing(WindowEvent windowEvent) {
		        System.exit(0);
		    }
		});
		
	}
	
	private void loginFailed(String message) {
		JOptionPane.showMessageDialog(this, message, "Login Failed", JOptionPane.ERROR_MESSAGE);
	}
	
	public void addLoginListener(LoginListener listener) {
		this.listeners.add(listener);
	}
	
}

