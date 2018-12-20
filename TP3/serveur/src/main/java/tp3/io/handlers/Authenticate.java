package tp3.io.handlers;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import org.jetbrains.annotations.Nullable;
import tp3.bd.Users;
import tp3.bd.entities.User;
import tp3.io.handlers.errors.InvalidCredentials;
import tp3.io.handlers.errors.RequestError;

import javax.net.ssl.SSLSocket;
import java.time.Instant;

import static tp3.bd.Users.login;

public class Authenticate implements IHandler {
	@Nullable
	@Override
	public Any handle(SSLSocket client, Any data) throws RequestError {
		String username = data.get("username").toString();
		String password = data.get("password").toString();
		User   user     = login(username, password);
		if (user == null) throw new InvalidCredentials();
		client.getSession().putValue("user", user);
		client.getSession().putValue("lastQuery", Instant.now());
		return Any.wrap(new String(client.getSession().getId()));
	}
}
