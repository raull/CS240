package server.handler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import server.facade.ServerFacade;
import shared.communication.SubmitBatch_Parameter;
import shared.communication.SubmitBatch_Response;
import shared.modal.User;
import shared.modal.Value;

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
			
			if (user != null) {
				//If user is valid then parse the string for all the values				
				String[] allRecords = param.getValues().split(";");
				ArrayList<Value> values = new ArrayList<Value>();
				
				for (int i = 0; i < allRecords.length; i++) {
					String[] contents = allRecords[i].split(",");
					for (int j = 0; j < contents.length; j++) {
						Value newValue = new Value(contents[j], i+1, j+1);
						values.add(newValue);
					}
				}
				
				//Submit Batch
				if(ServerFacade.submitBatch(values, param.getBatchId() , user)){
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
