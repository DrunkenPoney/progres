package tp3.io;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;
import com.jsoniter.output.JsonStream;
import tp3.io.handlers.errors.RequestError;

import javax.net.ssl.SSLSocket;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.function.Consumer;

public class ConnectionHandler implements Consumer<SSLSocket> {
	@Override
	public void accept(SSLSocket client) {
		client.addHandshakeCompletedListener(ev -> {
			try {
				byte[]               bytes  = new BufferedInputStream(client.getInputStream()).readAllBytes();
				BufferedOutputStream writer = new BufferedOutputStream(client.getOutputStream());
				String[]             data   = new String(bytes).split(":", 2);
				RequestAction        action;
				Any                  result;
				String               response;
				
				try (client) {
					try {
						action = RequestAction.valueOf(data[0]);
						result = action.handle(client, JsonIterator.deserialize(data[1]));
						response = "1:"; // = SUCCESS
						if (result != null) response += JsonStream.serialize(result.object());
						writer.write(response.getBytes());
						writer.flush();
					} catch (IllegalArgumentException iae) {
						throw new RequestError(1, "Invalid Request Action", iae);
					}
				} catch (RequestError e) {
					// 0 = FAIL
					writer.write(("0:" + e).getBytes());
					writer.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}