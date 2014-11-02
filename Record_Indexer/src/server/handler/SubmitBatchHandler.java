package server.handler;

import java.io.IOException;
import java.net.HttpURLConnection;

import server.facade.ServerFacade;
import shared.communication.SubmitBatch_Parameter;
import shared.communication.SubmitBatch_Response;
import shared.modal.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class SubmitBatchHandler implements HttpHandler {

	private XStream xmlStream = new XStream(new DomDriver());
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		SubmitBatch_Parameter param = (SubmitBatch_Parameter)xmlStream.fromXML(exchange.getRequestBody());
		SubmitBatch_Response result = new SubmitBatch_Response();
		
		try {
			//First Validate user
			User user = ServerFacade.validateUser(param.getUsername(), param.getPassword());
			
			if (user != null && user.getCurrentBatchId() == param.getBatchId()) {
				//If user is valid then parse the string for all the values				
				String[] allRecords = param.getValues().split(";");
				
				//Submit Batch
				if(ServerFacade.submitBatch(allRecords, param.getBatchId() , user)){
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
