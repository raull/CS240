package server.handler;

import java.io.IOException;
import java.net.HttpURLConnection;

import server.facade.ServerFacade;
import shared.communication.Validate_User_Parameter;
import shared.communication.Validate_User_Response;
import shared.modal.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ValidateUserHandler implements HttpHandler {
	
	private XStream xmlStream = new XStream(new DomDriver());

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		
		Validate_User_Parameter param = (Validate_User_Parameter)xmlStream.fromXML(exchange.getRequestBody());
		
		Validate_User_Response result = new Validate_User_Response();
		
		try {
			User user = ServerFacade.validateUser(param.getUsername(), param.getPassword());
			
			if (user != null) {
				result.setFirstName(user.getFirstName());
				result.setLastName(user.getLastName());
				result.setRecordCount(user.getRecordCount());
				result.setOutput("TRUE");
			} else {
				result.setOutput("FALSE");
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
