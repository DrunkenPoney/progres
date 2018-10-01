package tp2.models.io;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.jetbrains.annotations.NotNull;
import tp2.models.db.documents.ClientModel;
import tp2.models.utils.Constants;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;

import static java.net.NetworkInterface.networkInterfaces;
import static org.apache.commons.lang3.ArrayUtils.add;
import static org.apache.commons.lang3.SerializationUtils.deserialize;
import static tp2.models.utils.Constants.*;
import static tp2.models.utils.SGR.FG_BRIGHT_BLUE;
import static tp2.models.utils.SGR.FG_MAGENTA;
import static tp2.models.utils.Utils.describe;
import static tp2.models.utils.Utils.firstAvailableSocketInRange;

public final class Transmission {
	private static final ExecutorService SENDER_EXECUTION_QUEUE   = Executors.newSingleThreadExecutor();
	private static final ExecutorService RECEIVER_EXECUTION_QUEUE = Executors.newFixedThreadPool(Constants.DFLT_EXECUTION_POOL_SIZE);
	
	private static Transmission instance;
	
	private final Queue<MessageHandler>           handlers;
	private final AsynchronousServerSocketChannel server;
	
	private Transmission() {
		try {
			handlers = new SynchronousQueue<>();
			server = AsynchronousServerSocketChannel
					.open().bind(networkInterfaces()
							             .filter(ni -> {
								             try {
									             return ni.isUp() && !ni.isVirtual() && !ni.isLoopback();
								             } catch (SocketException ignored) {}
								             return false;
							             })
							             .flatMap(NetworkInterface::inetAddresses)
							             .filter(ia -> !ia.isLoopbackAddress())
							             .map(ia -> firstAvailableSocketInRange(ia, MIN_PORT_NUMBER, MAX_PORT_NUMBER))
							             .filter(Objects::nonNull)
							             .filter(ia -> InetAddressValidator.getInstance()
							                                               .isValid(ia.getAddress().getHostAddress()))
							             .peek(ia -> describe(ia.getHostString(), ia))
							             .findAny()
							             .orElse(null));
			server.accept(null, new ConnectionHandler());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Transmission getInstance() {
		if (instance == null) instance = new Transmission();
		return instance;
	}
	
	public Future<?> send(@NotNull Message message) {
		final byte[]           bytes   = SerializationUtils.serialize(message);
		final Set<ClientModel> targets = message.getTarget().getMembers();
		return SENDER_EXECUTION_QUEUE.submit(() -> {
			for (ClientModel target : targets) {
				ByteBuffer buffer = ByteBuffer.wrap(bytes);
				try (AsynchronousSocketChannel channel = AsynchronousSocketChannel.open().bind(null)) {
					channel.connect(target.getSocket()).get(Constants.CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
					while (buffer.hasRemaining())
						channel.write(buffer);
//					buffer.rewind();
				} catch (IOException | InterruptedException | ExecutionException e) {
					// TODO replace with i18n message
					System.err.println(FG_MAGENTA.wrap("Error occured while sending message: \n\t" + e.getMessage()));
					e.printStackTrace();
				} catch (TimeoutException e) {
					// TODO replace with i18n message
					System.err.println(FG_MAGENTA.wrap("Connection timed out! \n\t" + e.getMessage()));
					e.printStackTrace();
				}
			}
		});
	}
	
	public boolean addMessageHandler(final @NotNull MessageHandler handler) {
		return handlers.offer(handler);
	}
	
	@SuppressWarnings("unused")
	public boolean removeMessageHandler(final @NotNull MessageHandler handler) {
		return handlers.remove(handler);
	}
	
	public SocketAddress getLocalAddress() throws IOException {
		return server.getLocalAddress();
	}
	
	private class ConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {
		@Override
		public void completed(AsynchronousSocketChannel client, Void attachment) {
			server.accept(null, this);
			final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
			
			if (client != null && client.isOpen()) {
				byte[] bytes = null;
				int    pos;
				System.out.println(FG_BRIGHT_BLUE.wrap("Connection established!"));
				try {
					do {
						pos = client.read(buffer).get();
						buffer.flip();
						while (buffer.hasRemaining())
							bytes = add(bytes, buffer.get());
						buffer.clear();
					} while (pos != -1);
					
					if (bytes != null && bytes.length > 0) {
						final Message receivedMsg = (Message) deserialize(bytes);
						RECEIVER_EXECUTION_QUEUE.execute(() -> handlers.forEach(handler -> handler.handle(receivedMsg)));
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				} catch (ClassCastException e) {
					System.err.println(FG_MAGENTA.wrap("Invalid message received!"));
				}
			}
		}
		
		@Override
		public void failed(Throwable exc, Void attachment) {
			// TODO replace with i18n message
			System.err.println(FG_MAGENTA.wrap("Incomming connection failed: " + exc.getMessage()));
			exc.printStackTrace();
		}
	}
}
