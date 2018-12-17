package progres.tp4.api.dominospizzaapi.errors;

public class RequiredParamException extends Exception implements IRequestException {
	private static final long serialVersionUID = -6360271460225535146L;
	
	public RequiredParamException(String message) {
		super(message);
	}
}
