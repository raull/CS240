package server.handler;

import java.io.IOException;
import java.net.HttpURLConnection;

import server.facade.ServerFacade;
import shared.communication.GetSampleImage_Parameter;
import shared.communication.GetSampleImage_Response;
import shared.modal.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class GetSampleImageHandler implements HttpHandler {

	private XStream xmlStream = new XStream(new DomDriver());
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		GetSampleImage_Parameter param = (GetSampleImage_Parameter)xmlStream.fromXML(exchange.getRequestBody());
		
		GetSampleImage_Response result = new GetSampleImage_Response();
		
		try {
			//Validate User first
			User user = ServerFacade.validateUser(param.getUsername(), param.getPassword());
			
			if (user != null) {
				//If valid user then get Batch Image
				String path  = ServerFacade.getSampleImage(param.getProjectId());
				result.setOutput("TRUE");
				result.setImageURL(path);
				
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
