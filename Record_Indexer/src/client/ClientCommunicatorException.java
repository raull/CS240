package client;

@SuppressWarnings("serial")
public class ClientCommunicatorException extends Exception {
	
	public ClientCommunicatorException() {
		return;
	}

	public ClientCommunicatorException(String message) {
		super(message);
	}

	public ClientCommunicatorException(Throwable cause) {
		super(cause);

	}

	public ClientCommunicatorException(String message, Throwable cause) {
		super(message, cause);
	}
}
