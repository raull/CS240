package shared;

import java.util.ArrayList;

import org.w3c.dom.Element;

import shared.modal.Project;
import shared.modal.User;

public class IndexerData {
	
	//-------------------------------------Instance Fields---------------------------------
	
	private ArrayList<User> users = new ArrayList<User>();
	private ArrayList<Project> projects = new ArrayList<Project>();
	
	//-------------------------------------Constructors------------------------------------
	public IndexerData(Element root) {
		
		ArrayList<Element> rootElements = DataImporter.getChildElements(root);
		
		ArrayList<Element> userElements = DataImporter.getChildElements(rootElements.get(0));
		for(Element userElement : userElements) {
			users.add(new User(userElement));
		}
		
		ArrayList<Element> projectElements = DataImporter.getChildElements(rootElements.get(1));
		for(Element projectElement : projectElements) {
			projects.add(new Project(projectElement));
		}

	}
	
	//---------------------------------------Getters and Setters -----------------------------
	
	/**
	 * @return the users
	 */
	public ArrayList<User> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(ArrayList<User> users) {
		this.users = users;
	}

	/**
	 * @return the projects
	 */
	public ArrayList<Project> getProjects() {
		return projects;
	}

	/**
	 * @param projects the projects to set
	 */
	public void setProjects(ArrayList<Project> projects) {
		this.projects = projects;
	}
}
