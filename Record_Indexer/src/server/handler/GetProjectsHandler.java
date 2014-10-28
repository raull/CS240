package server.handler;

import java.io.IOException;
import java.net.HttpURLConnection;

import server.facade.ServerFacade;
import shared.communication.Get_Projects_Parameter;
import shared.communication.Get_Projects_Response;
import shared.modal.User;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class GetProjectsHandler implements HttpHandler {

	private XStream xmlStream = new XStream(new DomDriver());
	
	@Override
	public void handle(HttpExchange exchange) throws IOException {
		// TODO Auto-generated method stub
		
		Get_Projects_Parameter param = (Get_Projects_Parameter)xmlStream.fromXML(exchange.getRequestBody());
		
		try {
			User user = ServerFacade.validateUser(param.getUsername(), param.getPassword());
			
			Get_Projects_Response result = new Get_Projects_Response();
			
			if (user != null) {
				
			} else {
				result.setOutput("FALSE");
			}
			
		} catch (Exception e) {
			exchange.sendResponseHeaders(HttpURLConnection.HTTP_INTERNAL_ERROR, 0);
			return;
		}
		
	}

}
