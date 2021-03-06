package shared;

import java.util.ArrayList;

import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import shared.modal.Project;
import shared.modal.User;

public class IndexerData {
	
	//-------------------------------------Instance Fields---------------------------------
	
	private ArrayList<User> users = new ArrayList<User>();
	private ArrayList<Project> projects = new ArrayList<Project>();
	
	//-------------------------------------Constructors------------------------------------
	public IndexerData(Element root) {
		
		ArrayList<Element> rootElements = IndexerData.getChildElements(root);
		
		ArrayList<Element> userElements = IndexerData.getChildElements(rootElements.get(0));
		for(Element userElement : userElements) {
			users.add(new User(userElement));
		}
		
		ArrayList<Element> projectElements = IndexerData.getChildElements(rootElements.get(1));
		for(Element projectElement : projectElements) {
			projects.add(new Project(projectElement));
		}

	}
	
	//-------------------------------------Static Methods----------------------------------
	public static String getValue(Element element) {
		if (element == null) {
			return null;
		}
		
		String result = "";
		Node child = element.getFirstChild();
		try {
			result = child.getNodeValue();
		} catch (DOMException e) {
			return null;
		}
		
		return result;
	}
	
	
	public static ArrayList<Element> getChildElements(Node node) {
		ArrayList<Element> result = new ArrayList<Element>();
		
		NodeList children = node.getChildNodes();
		for(int i = 0; i < children.getLength(); i++) {
			Node child = children.item(i);
			if(child.getNodeType() == Node.ELEMENT_NODE){
				result.add((Element)child);
			}
		}
		return result;
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
