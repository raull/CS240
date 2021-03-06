package server.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;

import server.facade.ServerFacade;
import shared.communication.GetFields_Parameter;
import shared.communication.GetFields_Response;
import shared.modal.Field;
import shared.modal.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class GetFieldsHandler implements HttpHandler {
	
	private XStream xmlStream = new XStream(new DomDriver());
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		GetFields_Parameter param = (GetFields_Parameter)xmlStream.fromXML(exchange.getRequestBody());
		GetFields_Response result = new GetFields_Response();
		
		try {
			//First Validate user
			User user = ServerFacade.validateUser(param.getUsername(), param.getPassword());
			
			
			if (user != null) {
				//If user is valid then make the request				
				List<Field> allFields = ServerFacade.getFields(param.getProjectId());
				
				//Check if valid request
				if (allFields != null) {
					result.setFields(allFields);
					result.setOutput("TRUE");
				} else {
					result.setOutput("FAILED");
				}
				
				
			} else {
				result.setOutput("FAILED");
			}
			
			//Send Headers
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
