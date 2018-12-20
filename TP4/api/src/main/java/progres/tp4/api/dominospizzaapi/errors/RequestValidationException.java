package progres.tp4.api.dominospizzaapi.errors;

public class RequestValidationException extends Exception implements IRequestException {
	private static final long serialVersionUID = -6605609441522214672L;
	
	public RequestValidationException(String message) {
		super(message);
	}
	
	public RequestValidationException(String message, Throwable cause) {
		super(message, cause);
	}
}
