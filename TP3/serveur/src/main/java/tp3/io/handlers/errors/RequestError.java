package tp3.io.handlers.errors;

import org.jetbrains.annotations.NotNull;

public class RequestError extends Throwable {
	private static final long serialVersionUID = 6953820490700183571L;
	private final int code;
	
	public RequestError(int code, @NotNull String message) {
		super(message);
		this.code = code;
	}
	
	public RequestError(int code, @NotNull String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	@Override
	public String toString() {
		return getCode() + ":" + getMessage();
	}
}