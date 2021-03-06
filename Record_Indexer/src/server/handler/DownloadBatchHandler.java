package server.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import server.facade.ServerFacade;
import shared.communication.DownloadBatch_Parameter;
import shared.communication.DownloadBatch_Response;
import shared.modal.Batch;
import shared.modal.Field;
import shared.modal.Project;
import shared.modal.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class DownloadBatchHandler implements HttpHandler {

	private XStream xmlStream = new XStream(new DomDriver());
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		DownloadBatch_Parameter param = (DownloadBatch_Parameter)xmlStream.fromXML(exchange.getRequestBody());
		DownloadBatch_Response result = new DownloadBatch_Response();		
		
		try {
			//Validate User first
			User user = ServerFacade.validateUser(param.getUsername(), param.getPassword());
			
			if (user != null && user.getCurrentBatchId() <= 0) {
				//If valid user then get all the Fields for the project
				Project project = ServerFacade.getProject(param.getProjectId());
				List<Field> allFields = ServerFacade.getFields(project.getId());
				Batch batch = ServerFacade.getAvailableBatch(project.getId(), user);
				
				if (batch != null) {
					result.setBatch(batch);
					result.setFields(allFields);
					result.setProject(project);
					result.setOutput("TRUE");
				} else {
					result.setOutput("FAILED");
				}
				
			} else {
				result.setOutput("FAILED");
			}
			
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
			
		} catch (Exception e) {
			result.setOutput("FAILED");
			System.out.println("Request Failed: " + e.getLocalizedMessage());
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, -1);
		}
		
		xmlStream.toXML(result, exchange.getResponseBody());
		exchange.getResponseBody().close();

	}

}
