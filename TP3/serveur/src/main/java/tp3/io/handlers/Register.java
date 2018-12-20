package tp3.io.handlers;

import com.jsoniter.any.Any;
import org.jetbrains.annotations.Nullable;
import tp3.io.handlers.errors.RequestError;
import tp3.io.handlers.errors.UserAlreadyExists;

import javax.net.ssl.SSLSocket;
import java.time.Instant;

import static tp3.bd.Users.find;
import static tp3.bd.Users.login;

public class Register implements IHandler {
	@Nullable
	@Override
	public Any handle(SSLSocket client, Any data) throws RequestError {
		final String username = data.get("username").toString();
		final String password = data.get("password").toString();
		if (find(username) != null) throw new UserAlreadyExists();
		client.getSession().putValue("user", login(username,password));
		client.getSession().putValue("lastQuery", Instant.now());
		return Any.wrap(new String(client.getSession().getId()));
	}
}
