package client.search;

import java.util.ArrayList;

import shared.communication.GetFields_Parameter;
import shared.communication.GetFields_Response;
import shared.communication.Get_Projects_Parameter;
import shared.communication.Get_Projects_Response;
import shared.communication.Search_Input;
import shared.communication.Search_Response;
import shared.communication.Validate_User_Parameter;
import shared.communication.Validate_User_Response;
import shared.modal.Field;
import shared.modal.Project;
import client.communication.ClientCommunicator;

public class SearchController {
	
	private ClientCommunicator clientCommunicator;
	private String username;
	private String password;
	private SearchFrame view;
	private ArrayList<Project> projects;
	private Project selectedProject;
	private ArrayList<Field> selectedFields;
	
	public SearchController() {
		this.view = new SearchFrame(this);
		this.view.presedLoginDialog();
		this.view.setVisible(true);
	}
	
	public void loginUser(String username, String password, String host, String port) {
		
		try {
			//Start Client Communicator
			this.clientCommunicator = new ClientCommunicator(host, port);
		} catch (Exception e) {
			view.loginFailed("Error connecting, please check host and port and try again");
			return;
		}
		
		try {
			
			//Validate User
			Validate_User_Parameter param = new Validate_User_Parameter(username, password);
			Validate_User_Response response = clientCommunicator.validateUser(param);
			if (response.getOutput().equals("FALSE")) {
				view.loginFailed("Wrong username and password");
			} else if (response.getOutput().equals("FAILED")) {
				view.loginFailed("Error: please try again later");
			} else {
				this.username = username;
				this.password = password;
				
				view.loginSuccesfull();
				
				getProjects();
			}
		} catch (Exception e) {
			view.loginFailed("Error: please try again later");
		}
	}
	
	public void getProjects() {
		
		try {
			Get_Projects_Parameter param = new Get_Projects_Parameter(this.username, this.password);
			Get_Projects_Response response = clientCommunicator.getProjects(param);
			
			if (!response.getOutput().equals("FAILED")) {
				String[] projectNames = new String[response.getProjects().size()];
				projects = (ArrayList<Project>)response.getProjects();
				
				for (int i=0; i<projects.size(); i++) {
					projectNames[i] = projects.get(i).getTitle();
				}
				
				view.updateProjects(projectNames);
			}
			
		} catch (Exception e) {
			System.out.println("Get Projects Failed");
		}
		
	}
	
	public void projectListValueChanged(int index) {
		Project project = projects.get(index);
		selectedProject = project;
		if (project.getFields().size() == 0) {
			try {
				GetFields_Parameter param = new GetFields_Parameter(username, password, project.getId());
				GetFields_Response response = clientCommunicator.getFields(param);
				
				if (!response.getOutput().equals("FAILED")) {
					
					ArrayList<Field> fields = (ArrayList<Field>)response.getFields();
					project.setFields(fields);

					String[] fieldNames = new String[fields.size()];
					
					for (int i=0; i<fields.size(); i++) {
						fieldNames[i] = fields.get(i).getTitle();
					}
					
					view.updateFields(fieldNames);
				}
				
			} catch (Exception e) {
				System.out.println("Get Projects Failed");
			}
		} else {
			ArrayList<Field> fields = project.getFields();

			String[] fieldNames = new String[fields.size()];
			
			for (int i=0; i<fields.size(); i++) {
				fieldNames[i] = fields.get(i).getTitle();
			}
			
			view.updateFields(fieldNames);
		}
	}
	
	public void fieldListValueChanged(int[] indices) {
		ArrayList<Field> projectFields = selectedProject.getFields();
		ArrayList<Field> selectedFields = new ArrayList<Field>();
		
		for (int i = 0; i < indices.length; i++) {
			selectedFields.add(projectFields.get(indices[i]));
		}
				
		this.selectedFields = selectedFields;
	}
	
	public void searchWords(String values) {
		
		try {
			StringBuilder fields = new StringBuilder();
			for (Field field : this.selectedFields) {
				fields.append(field.getId() + ",");
			}
			Search_Input param = new Search_Input(username, password, fields.toString(), values);
			Search_Response response = clientCommunicator.search(param);
			
			
			ArrayList<String> results = (ArrayList<String>)response.getTuples();

			String[] resultArray = new String[results.size()];
			
			for (int i=0; i<results.size(); i++) {
				resultArray[i] = results.get(i);
			}
			
			view.updateResults(resultArray);
						
		} catch (Exception e) {
			System.out.println("Search Failed");
		}
		
	}
	//Getters and Setters
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
}
