package client.search;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class LoginDialog extends JDialog {
	
	private JButton loginButton;
	private JButton cancelButton;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JTextField hostField;
	private JTextField portField;

	
	public LoginDialog(final SearchFrame owner, boolean modal) {
		super(owner, modal);
		
		this.setSize(new Dimension(400, 200));
		this.setMinimumSize(new Dimension(370, 200));
		this.setMaximumSize(new Dimension(450, 300));
		this.setLayout(new BorderLayout());
		this.setLocationRelativeTo(null);
		
		//Set the Panel for buttons
		JPanel buttonPanel = new JPanel();
		this.loginButton = new JButton("Login");
		this.loginButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				owner.loginButtonPressed(usernameField.getText(), new String(passwordField.getPassword()), hostField.getText(), portField.getText());
			}
		});
		this.cancelButton = new JButton("Cancel");
		this.cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		buttonPanel.add(loginButton);
		buttonPanel.add(cancelButton);
		
		this.add(buttonPanel, BorderLayout.SOUTH);
		
		//Set the inputs panel
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
		this.add(inputPanel, BorderLayout.CENTER);
		
		//Set Server Panel
		JPanel serverInfoPanel = new JPanel();
		JPanel hostPanel = new JPanel();
		JPanel portPanel = new JPanel();
		
		hostPanel.add(new JLabel("Host"));
		hostField = new JTextField(15);
		hostPanel.add(hostField);
		serverInfoPanel.add(hostPanel, BorderLayout.WEST);
		
		portPanel.add(new JLabel("Port"));
		portField = new JTextField(5);
		portPanel.add(portField);
		serverInfoPanel.add(portPanel, BorderLayout.EAST);
		
		inputPanel.add(serverInfoPanel);
		
		//Set Username Panel
		JPanel usernamePanel = new JPanel();
		
		usernamePanel.add(new JLabel("Username"), BorderLayout.WEST);
		usernameField = new JTextField(20);
		usernamePanel.add(usernameField, BorderLayout.EAST);
		
		inputPanel.add(usernamePanel);
		
		//Set Password Panel
		JPanel passwordPanel = new JPanel();
		
		passwordPanel.add(new JLabel("Password"), BorderLayout.WEST);
		passwordField = new JPasswordField(20);
		passwordPanel.add(passwordField, BorderLayout.EAST);
		
		inputPanel.add(passwordPanel);
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		//Window closing
		this.addWindowListener(new WindowAdapter() {
			@Override
		    public void windowClosing(WindowEvent windowEvent) {
		        System.exit(0);
		    }
		});
	}
}
