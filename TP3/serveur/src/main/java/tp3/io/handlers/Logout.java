package tp3.io.handlers;

import com.jsoniter.any.Any;
import org.jetbrains.annotations.Nullable;

import javax.net.ssl.SSLSocket;

public class Logout implements IHandler {
	@Override
	public @Nullable Any handle(SSLSocket client, Any data) {
		client.getSession().removeValue("user");
		client.getSession().removeValue("lastQuery");
		client.getSession().invalidate();
		return null;
	}
}
