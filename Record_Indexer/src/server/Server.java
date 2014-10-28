package server;

import java.io.IOException;
import java.net.InetSocketAddress;

import server.facade.ServerFacade;
import server.handler.DownloadBatchHandler;
import server.handler.GetProjectsHandler;
import server.handler.ValidateUserHandler;

import com.sun.net.httpserver.*;

public class Server {
	
	private static final int SERVER_PORT_NUMBER = 8080;
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
		server.createContext("/DownloadBatch", downloadBatchHandler);

		System.out.println("Starting HTTP Server");

		server.start();
	}
	
	//Handlers
	private HttpHandler validateUserHandler = new ValidateUserHandler();
	private HttpHandler getProjectsHandler = new GetProjectsHandler();
	private HttpHandler downloadBatchHandler = new DownloadBatchHandler();
	
	
	public static void main(String[] args) {
		
		Server newServer = new Server();
		newServer.run();
	}
}
