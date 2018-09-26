package tp2.model;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class MessageReceiver {
	private final AsynchronousServerSocketChannel channel;
	
	public MessageReceiver() throws IOException {
		channel = AsynchronousServerSocketChannel.open().bind(null);
		channel.accept(null, new CompletionHandler<AsynchronousSocketChannel, Void>() {
			@Override
			public void completed(AsynchronousSocketChannel client, Void attachment) {
				channel.accept(null, this);
				
				if (client != null && client.isOpen()) {
					while(true) {
						ByteBuffer buffer = ByteBuffer.allocate(32);
						try {
							client.read(buffer).get();
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			@Override
			public void failed(Throwable exc, Void attachment) {
				System.err.println("Une connexion entrante a échouée: " + exc.getMessage());
			}
		});
	}
	
	private void exec() {
	
	}
	
	public interface MessageHandler {
	
	}
	
	public static class ConnectedClient {
		private final UUID uuid;
		
		private ConnectedClient(UUID uuid, AsynchronousSocketChannel clientChannel) {
			this.uuid = uuid;
			
		}
	}
}
