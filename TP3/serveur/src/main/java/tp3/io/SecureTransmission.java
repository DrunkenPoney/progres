package tp3.io;

public class SecureTransmission {
	private static SecureTransmission instance;
	
	private SecureTransmission() {
	}
	
	public static SecureTransmission getInstance() {
		if (instance == null) instance = new SecureTransmission();
		return instance;
	}
}
