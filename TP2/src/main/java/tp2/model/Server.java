package tp2.model;

import java.util.Properties;

public class Server {
	private static Server instance;
	
	private Server() {
	
	}
	
	public static Server getInstance() {
		if (instance == null)
			instance = new Server();
		return instance;
	}
	
}
