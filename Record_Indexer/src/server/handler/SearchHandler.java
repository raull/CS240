package server.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import server.facade.ServerFacade;
import shared.communication.Search_Input;
import shared.communication.Search_Response;
import shared.modal.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class SearchHandler implements HttpHandler {

	private XStream xmlStream = new XStream(new DomDriver());
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		Search_Input param = (Search_Input)xmlStream.fromXML(exchange.getRequestBody());
		Search_Response result = new Search_Response();
		
		try {
			//First Validate user
			User user = ServerFacade.validateUser(param.getUsername(), param.getPassword());
			
			ArrayList<String> tuples = new ArrayList<String>();
			
			if (user != null) {
				//If user is valid then make the request				
				for (int fieldId : param.getFields()) {
					for (String value : param.getValues()) {
						tuples.addAll(ServerFacade.search(fieldId, value));
					}
				}
				
				if (tuples.size() > 0) {
					result.setOutput("TRUE");
					result.setTuples(tuples);
				}
				else {
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
