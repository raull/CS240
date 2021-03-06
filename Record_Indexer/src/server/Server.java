
package server;

import java.io.IOException;
import java.net.InetSocketAddress;

import server.facade.ServerFacade;
import server.handler.DownloadBatchHandler;
import server.handler.DownloadFileHandler;
import server.handler.GetFieldsHandler;
import server.handler.GetProjectsHandler;
import server.handler.GetSampleImageHandler;
import server.handler.SearchHandler;
import server.handler.SubmitBatchHandler;
import server.handler.ValidateUserHandler;

import com.sun.net.httpserver.*;

public class Server {
	
	private static int SERVER_PORT_NUMBER = 8080;
	private static final int MAX_WAITING_CONNECTIONS = 10;
	
	private HttpServer server;
	
	private Server() {
		return;
	}
	
	private void run() {
		
		try {
			ServerFacade.initialize();		
		}
		catch (ServerException e) {
			System.out.println("Server Facade failed to initialize");
			return;
		}
		
		System.out.println("Initializing HTTP Server");
		
		try {
			server = HttpServer.create(new InetSocketAddress(SERVER_PORT_NUMBER),
											MAX_WAITING_CONNECTIONS);
		} 
		catch (IOException e) {
			System.out.println("Error: " + e.getLocalizedMessage());
			return;
		}

		server.setExecutor(null); // use the default executor
		
		server.createContext("/ValidateUser", validateUserHandler);
		server.createContext("/GetProjects", getProjectsHandler);
		server.createContext("/GetSampleImage", getSampleImageHandler);
		server.createContext("/DownloadBatch", downloadBatchHandler);
		server.createContext("/SubmitBatch", submitBatchHandler);
		server.createContext("/GetFields", getFieldsHandler);
		server.createContext("/", downloadFileHandler);
		server.createContext("/Search", searchHandler);

		System.out.println("Starting HTTP Server");

		server.start();
	}
	
	//Handlers
	private HttpHandler validateUserHandler = new ValidateUserHandler();
	private HttpHandler getProjectsHandler = new GetProjectsHandler();
	private HttpHandler getSampleImageHandler = new GetSampleImageHandler();
	private HttpHandler downloadBatchHandler = new DownloadBatchHandler();
	private HttpHandler submitBatchHandler = new SubmitBatchHandler();
	private HttpHandler getFieldsHandler = new GetFieldsHandler();
	private HttpHandler downloadFileHandler = new DownloadFileHandler();
	private HttpHandler searchHandler = new SearchHandler();
	
	public static void main(String[] args) {
		
		Server newServer = new Server();
		if (args.length == 1) {
			Server.SERVER_PORT_NUMBER = Integer.parseInt(args[0]);
		}
		newServer.run();
	}
}
