package tp2.models.io;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.jetbrains.annotations.NotNull;
import tp2.models.io.data.TransmissionData;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static java.lang.String.format;
import static java.net.NetworkInterface.networkInterfaces;
import static java.nio.ByteBuffer.allocateDirect;
import static org.apache.commons.lang3.ArrayUtils.add;
import static org.apache.commons.lang3.ArrayUtils.addAll;
import static org.apache.commons.lang3.SerializationUtils.deserialize;
import static tp2.models.db.collections.Accessors.getGroupsCollection;
import static tp2.models.utils.Constants.*;
import static tp2.models.utils.I18n.messages;
import static tp2.models.utils.SGR.FG_MAGENTA;
import static tp2.models.utils.Utils.describe;
import static tp2.models.utils.Utils.firstAvailableSocketInRange;

public final class Transmission {
	private static Transmission instance;
	
	private final List<TransmissionDataHandler>   handlers;
	private final AsynchronousServerSocketChannel server;
	
	private Transmission() {
		try {
			handlers = new ArrayList<>();
			server = AsynchronousServerSocketChannel.open().bind(
					networkInterfaces()
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
	
	public Disposable send(final @NotNull TransmissionData data) {
		final byte[] bytes = SerializationUtils.serialize(data);
		return Observable.fromIterable(getGroupsCollection().membersOf(data.getTarget()))
		                 .doOnNext(client -> {
			                 ByteBuffer buffer = allocateDirect(bytes.length).put(bytes).flip();
			                 try (SocketChannel channel = SocketChannel.open().bind(null)) {
				                 channel.connect(client.getSocket());
				                 while (buffer.hasRemaining())
					                 channel.write(buffer);
			                 }
		                 })
		                 .subscribe();
	}
	
	public boolean addDataHandler(final @NotNull TransmissionDataHandler handler) {
		return handlers.add(handler);
	}
	
	@SuppressWarnings("unused")
	public boolean removeDataHandler(final @NotNull TransmissionDataHandler handler) {
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
			byte[]           bytes  = null;
			int              pos;
			
			if (client != null && client.isOpen()) {
				try {
					do {
						pos = client.read(buffer).get();
						buffer.flip();
						if (buffer.hasArray())
							bytes = addAll(bytes, buffer.array());
						else while (buffer.hasRemaining())
							bytes = add(bytes, buffer.get());
						buffer.clear();
					} while (pos != -1);
					
					if (bytes != null && bytes.length > 0) {
						final TransmissionData data = (TransmissionData) deserialize(bytes);
						Flowable.fromIterable(handlers)
						        .subscribeOn(Schedulers.single())
						        .parallel()
						        .runOn(Schedulers.computation())
						        .doOnNext(handler -> handler.handle(data))
						        .sequential()
						        .subscribe();
					}
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				} catch (ClassCastException e) {
					System.err.println(FG_MAGENTA.wrap(messages().get("error.invalid.data.received")));
				}
			}
		}
		
		@Override
		public void failed(Throwable exc, Void attachment) {
			// TODO replace with i18n message
			System.err.println(FG_MAGENTA.wrap(format(messages().get("error.incoming.connection.failed"),
			                                          exc.getMessage())));
			exc.printStackTrace();
		}
	}
}
