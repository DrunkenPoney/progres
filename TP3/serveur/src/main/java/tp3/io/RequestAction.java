package tp3.io;

import com.jsoniter.any.Any;
import tp3.io.handlers.Authenticate;
import tp3.io.handlers.Logout;
import tp3.io.handlers.Register;
import tp3.io.handlers.errors.RequestError;
import tp3.io.handlers.IHandler;

import javax.net.ssl.SSLSocket;

public enum RequestAction {
	REGISTER(new Register()),
	LOGIN(new Authenticate()),
	LOGOUT(new Logout())
	;
	private final IHandler handler;
	RequestAction(IHandler handler) {
		this.handler = handler;
	}
	
	public Any handle(SSLSocket client, Any data) throws RequestError {
		return handler.handle(client, data);
	}
}
