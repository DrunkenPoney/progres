package tp3.io.handlers.errors;

public class InvalidCredentials extends RequestError {
	private static final long serialVersionUID = -7259168508158493960L;
	public static final int ERROR_CODE = 2;
	
	public InvalidCredentials() {
		super(ERROR_CODE, "Invalid Password/Username");
	}
}
