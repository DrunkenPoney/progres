package tp2.model.io;

import tp2.model.db.documents.ClientModel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class Receiver {
	private final AsynchronousServerSocketChannel channel;
	private final Queue<MessageHandler> handlers;
	// TODO Add ExecutorService for async dispatch ???
	
	public Receiver() throws IOException {
		handlers = new SynchronousQueue<>();
		channel = AsynchronousServerSocketChannel.open().bind(null);
		channel.accept(null, new ConnectionHandler());
	}
	
	@SuppressWarnings("UnusedReturnValue")
	public boolean addHandler(MessageHandler handler) {
		return handlers.offer(handler);
	}
	
	private void dispatch(String message, AsynchronousSocketChannel client, ClientModel attachment) {
		try {
			final InetSocketAddress clientAddr = (InetSocketAddress) client.getRemoteAddress();
			handlers.forEach(handler -> handler.handle(message, clientAddr, attachment));
		} catch (IOException e) {
			System.err.println("Impossible d'obtenir l'adresse IP du client: " + e.getMessage());
		}
	}
	
	private void onDisconnect(AsynchronousSocketChannel client, ClientModel attachment) {
		try {
			System.out.println("Client déconnecté: " + client.getRemoteAddress());
		// TODO
		} catch(IOException err) {
			err.printStackTrace();
		}
	}
	
	private void onConnect(AsynchronousSocketChannel client, ClientModel attachment) {
		try {
			System.out.println("Client connecté: " + client.getRemoteAddress());
			// TODO
		} catch(IOException err) {
			err.printStackTrace();
		}
	}
	
	public AsynchronousServerSocketChannel getChannel() {
		return channel;
	}
	
	private class ConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, ClientModel> {
		@Override
		public void completed(AsynchronousSocketChannel client, ClientModel attachment) {
			channel.accept(null, this);
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			StringBuilder builder;
			
			if (client != null && client.isOpen()) {
				onConnect(client, attachment);
				while(client.isOpen()) {
					try {
						client.read(buffer).get();
						builder = new StringBuilder();
						buffer.flip();
						while(buffer.hasRemaining()) builder.append(buffer.getChar());
						buffer.compact();
						dispatch(builder.toString(), client, attachment);
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
				}
				onDisconnect(client, attachment);
			}
		}
		
		@Override
		public void failed(Throwable exc, ClientModel attachment) {
			System.err.println("Une connexion entrante a échouée: " + exc.getMessage());
		}
	}
	
	public interface MessageHandler {
		void handle(String message, InetSocketAddress client, ClientModel attachment);
	}
}
