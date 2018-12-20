package tp3.io.handlers.errors;

public class UserAlreadyExists extends RequestError {
	private static final int ERROR_CODE = 3;
	
	public UserAlreadyExists() {
		super(ERROR_CODE, "Username Not Available");
	}
}
