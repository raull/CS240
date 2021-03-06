package client.search;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


@SuppressWarnings("serial")
public class SearchFrame extends JFrame {

	private LoginDialog loginDialog;
	private SearchController controller;
	
	private JList<String> projectList;
	private JList<String> fieldList;
	
	private JTextArea wordArea;
	
	private JList<String> resultList;
	private JLabel imageResultLabel;
	private JPanel imagePanel;
	
	public SearchFrame(final SearchController controller) {
		
		super("Search GUI");
	
		this.controller = controller;
		
		this.setSize(new Dimension(400,600));
		this.setMinimumSize(new Dimension(400, 600));
		this.setMaximumSize(new Dimension(400, 900));
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		
		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		
		this.add(contentPanel);
		
		//Set Search Input Panel
		
		JPanel searchInputPanel = new JPanel();
		searchInputPanel.setLayout(new BoxLayout(searchInputPanel, BoxLayout.X_AXIS));
		searchInputPanel.setPreferredSize(new Dimension(400, 200));
		
		//Project List
		JPanel projectPanel = new JPanel();
		projectPanel.setLayout(new BoxLayout(projectPanel, BoxLayout.Y_AXIS));
		
		projectList = new JList<String>(new String[]{});
		projectList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		projectList.addListSelectionListener( new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				controller.projectListValueChanged(projectList.getSelectedIndex());
			}
		});
		JScrollPane projectScrollPane = new JScrollPane(projectList);
		projectScrollPane.setSize(200, 150);
		
		projectPanel.add(new JLabel("Select Project"));
		projectPanel.add(projectScrollPane);
		searchInputPanel.add(projectPanel);
		
		//Fields List
		JPanel fieldPanel = new JPanel();
		fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
		
		fieldList = new JList<String>(new String[]{});
		fieldList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		fieldList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				if (!e.getValueIsAdjusting()) {
					int[] selectedIndexes = fieldList.getSelectedIndices();
					
					System.out.println("Number of fields selected: " + selectedIndexes.length);
					System.out.println("Selected Fields: ");
					for (int i = 0; i < selectedIndexes.length; i++) {
						System.out.println(selectedIndexes[i]);
					}
					
					controller.fieldListValueChanged(selectedIndexes);
				}
				
			}
		});
		JScrollPane fieldScrollPane = new JScrollPane(fieldList);
		fieldScrollPane.setSize(200, 150);
		
		fieldPanel.add(new JLabel("Select Fields"));
		fieldPanel.add(fieldScrollPane);
		searchInputPanel.add(fieldPanel);
		
		// Word list
		JPanel wordPanel = new JPanel();
		wordPanel.setLayout(new BoxLayout(wordPanel, BoxLayout.Y_AXIS));
		
		wordArea = new JTextArea(10,20);
		JButton searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.searchWords(wordArea.getText());
			}
		});
		
		wordPanel.add(new JLabel("Words (Comma separated)"));
		wordPanel.add(wordArea);
		wordPanel.add(searchButton);
		searchInputPanel.add(wordPanel);
		
		//Set Result Panel
		
		JPanel resultPanel = new JPanel();
		resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
		resultPanel.setPreferredSize(new Dimension(400, 600));
		
		//Set Result List
		JPanel resultListPanel = new JPanel();
		resultListPanel.setLayout(new BoxLayout(resultListPanel, BoxLayout.Y_AXIS));
		
		resultListPanel.add(new JLabel("Results:"));
		resultList = new JList<String>(new String[]{});
		resultList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resultList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					String selectedValue =  resultList.getSelectedValue();
					String[] results = selectedValue.split("\n");
					updateResultImageWithPath(results[1]);
				}
			}
		});
		JScrollPane resultScrollPane = new JScrollPane(resultList);
		resultScrollPane.setSize(200, 100);
		
		resultListPanel.add(resultScrollPane);
		resultPanel.add(resultListPanel);
		
		//Set Image Panel
		imagePanel = new JPanel();
		imagePanel.setSize(300, 300);
		JScrollPane imageScrollPane = new JScrollPane(imagePanel);
		imageResultLabel = new JLabel();
		imagePanel.add(imageResultLabel);
		imageScrollPane.setSize(300,300);
		resultPanel.add(imageScrollPane);

		
		contentPanel.add(searchInputPanel);
		contentPanel.add(resultPanel);
		
	}
	
	public void loginSuccesfull() {
		this.loginDialog.setVisible(false);
		this.loginDialog = null;
		JOptionPane.showMessageDialog(this, "Welcome");
	}
	
	public void loginFailed(String message) {
		JOptionPane.showMessageDialog(this.loginDialog, message, "Login Failed", JOptionPane.ERROR_MESSAGE);
	}
	
	public void loginButtonPressed(String username, String password, String host, String port) {
		System.out.println("Login pressed on Dialog");
		controller.loginUser(username, password, host, port);
	}
	
	public void presedLoginDialog() {
		if (loginDialog == null) {
			loginDialog = new LoginDialog(this, true);
			loginDialog.setVisible(true);
		}
	}
	
	public void updateProjects(String[] projects) {
		projectList.setListData(projects);
	}
	
	public void updateFields(String[] fields) {
		fieldList.setListData(fields);
	}
	
	public void updateResults(String[] results) {
		resultList.setListData(results);
	}
	
	public void updateResultImageWithPath(String path) {
		System.out.println("Will display: " + path);
		try {
			URL url = new URL(path);
			BufferedImage image = ImageIO.read(url);
			imageResultLabel.setIcon(new ImageIcon(image));
		} catch (Exception e) {
			System.out.println("Error loading Image");
		}
		
	}
	
	//Getters and Setters
	public void setController(SearchController controller) {
		this.controller = controller;
	}
}
