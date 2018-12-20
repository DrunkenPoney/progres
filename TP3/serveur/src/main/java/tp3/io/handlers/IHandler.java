package tp3.io.handlers;

import com.jsoniter.any.Any;
import org.jetbrains.annotations.Nullable;
import tp3.io.handlers.errors.RequestError;

import javax.net.ssl.SSLSocket;

public interface IHandler {
	@Nullable
	Any handle(SSLSocket client, Any data) throws RequestError;
}
