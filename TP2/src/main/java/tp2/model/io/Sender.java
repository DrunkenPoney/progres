package tp2.model.io;

import org.jetbrains.annotations.NotNull;
import tp2.model.db.documents.ClientModel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.*;

public class Sender {
	private final ClientModel model;
	
	public Sender(@NotNull ClientModel model) {
		this.model = model;
	}
	
	public void send(@NotNull final String message, @NotNull final InetSocketAddress target) throws IOException {
		// TODO
		AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
		channel.connect(target, model, new CompletionHandler<>() {
			@Override
			public void completed(Void result, ClientModel attachment) {
				ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
				try {
					channel.write(buffer).get();
					buffer.clear();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void failed(Throwable exc, ClientModel attachment) {
				System.err.println("Impossible de se connecter à la cible: " + exc.getMessage());
			}
		});
	}
}
