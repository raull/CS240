package servertester.controllers;

import java.util.*;

import client.communication.ClientCommunicator;
import servertester.views.*;
import shared.communication.DownloadBatch_Parameter;
import shared.communication.DownloadBatch_Response;
import shared.communication.GetFields_Parameter;
import shared.communication.GetFields_Response;
import shared.communication.GetSampleImage_Parameter;
import shared.communication.GetSampleImage_Response;
import shared.communication.Get_Projects_Parameter;
import shared.communication.Get_Projects_Response;
import shared.communication.Search_Input;
import shared.communication.Search_Response;
import shared.communication.SubmitBatch_Parameter;
import shared.communication.SubmitBatch_Response;
import shared.communication.Validate_User_Parameter;
import shared.communication.Validate_User_Response;

public class Controller implements IController {

	private IView _view;
	
	public Controller() {
		return;
	}
	
	public IView getView() {
		return _view;
	}
	
	public void setView(IView value) {
		_view = value;
	}
	
	// IController methods
	//
	
	@Override
	public void initialize() {
		getView().setHost("localhost");
		getView().setPort("39640");
		operationSelected();
	}

	@Override
	public void operationSelected() {
		ArrayList<String> paramNames = new ArrayList<String>();
		paramNames.add("User");
		paramNames.add("Password");
		
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			break;
		case GET_PROJECTS:
			break;
		case GET_SAMPLE_IMAGE:
			paramNames.add("Project");
			break;
		case DOWNLOAD_BATCH:
			paramNames.add("Project");
			break;
		case GET_FIELDS:
			paramNames.add("Project");
			break;
		case SUBMIT_BATCH:
			paramNames.add("Batch");
			paramNames.add("Record Values");
			break;
		case SEARCH:
			paramNames.add("Fields");
			paramNames.add("Search Values");
			break;
		default:
			assert false;
			break;
		}
		
		getView().setRequest("");
		getView().setResponse("");
		getView().setParameterNames(paramNames.toArray(new String[paramNames.size()]));
	}

	@Override
	public void executeOperation() {
		switch (getView().getOperation()) {
		case VALIDATE_USER:
			validateUser();
			break;
		case GET_PROJECTS:
			getProjects();
			break;
		case GET_SAMPLE_IMAGE:
			getSampleImage();
			break;
		case DOWNLOAD_BATCH:
			downloadBatch();
			break;
		case GET_FIELDS:
			getFields();
			break;
		case SUBMIT_BATCH:
			submitBatch();
			break;
		case SEARCH:
			search();
			break;
		default:
			assert false;
			break;
		}
	}
	
	private void validateUser() {
		
		try {
			ClientCommunicator communicator = new ClientCommunicator(getView().getHost(), getView().getPort());
			String[] params = getView().getParameterValues();
			Validate_User_Parameter userParameter = new Validate_User_Parameter(params[0], params[1]);
			
			Validate_User_Response response = communicator.validateUser(userParameter);
			
			getView().setResponse(response.toString());
			
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
	
	private void getProjects() {
		
		try {
			ClientCommunicator communicator = new ClientCommunicator(getView().getHost(), getView().getPort());
			String[] params = getView().getParameterValues();
			 Get_Projects_Parameter parameter = new Get_Projects_Parameter(params[0], params[1]);
			
			Get_Projects_Response response = communicator.getProjects(parameter);
			
			getView().setResponse(response.toString());
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
		
	}
	
	private void getSampleImage() {
		try {
			ClientCommunicator communicator = new ClientCommunicator(getView().getHost(), getView().getPort());
			String[] params = getView().getParameterValues();
			GetSampleImage_Parameter parameter = new GetSampleImage_Parameter(params[0], params[1], Integer.parseInt(params[2]));
			
			GetSampleImage_Response response = communicator.getSampleImage(parameter);
			
			getView().setResponse(response.toString());
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
	
	private void downloadBatch() {
		try {
			ClientCommunicator communicator = new ClientCommunicator(getView().getHost(), getView().getPort());
			String[] params = getView().getParameterValues();
			DownloadBatch_Parameter parameter = new DownloadBatch_Parameter(params[0], params[1], Integer.parseInt(params[2]));
			
			DownloadBatch_Response response = communicator.downloadBatch(parameter);
			
			getView().setResponse(response.toString());
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
	
	private void getFields() {
		
		try {
			ClientCommunicator communicator = new ClientCommunicator(getView().getHost(), getView().getPort());
			String[] params = getView().getParameterValues();
			GetFields_Parameter parameter;
			if (params[2].equals("")) {
				parameter = new GetFields_Parameter(params[0], params[1], 0);
			} else {
				parameter = new GetFields_Parameter(params[0], params[1], Integer.parseInt(params[2]));
			}
			
			
			GetFields_Response response = communicator.getFields(parameter);
			
			getView().setResponse(response.toString());
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
	
	private void submitBatch() {
		try {
			ClientCommunicator communicator = new ClientCommunicator(getView().getHost(), getView().getPort());
			String[] params = getView().getParameterValues();
			SubmitBatch_Parameter parameter = new SubmitBatch_Parameter(params[0], params[1], params[3], Integer.parseInt(params[2]));
			
			SubmitBatch_Response response = communicator.submitBatch(parameter);
			
			getView().setResponse(response.toString());
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
	
	private void search() {
		try {
			ClientCommunicator communicator = new ClientCommunicator(getView().getHost(), getView().getPort());
			String[] params = getView().getParameterValues();
			Search_Input parameter = new Search_Input(params[0], params[1], params[2], params[3]);
			
			Search_Response response = communicator.search(parameter);
			
			getView().setResponse(response.toString());
		} catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}

}

