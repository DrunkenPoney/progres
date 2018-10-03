package tp2.models.io;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.validator.routines.InetAddressValidator;
import org.jetbrains.annotations.NotNull;
import tp2.models.db.documents.ClientModel;

import java.io.IOException;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.lang.Math.min;
import static java.lang.String.format;
import static java.net.NetworkInterface.networkInterfaces;
import static org.apache.commons.lang3.ArrayUtils.add;
import static org.apache.commons.lang3.SerializationUtils.deserialize;
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
	
	public Disposable send(@NotNull TransmissionData message) {
		final byte[]           bytes   = SerializationUtils.serialize(message);
		final Set<ClientModel> targets = message.getTarget().getMembers();
		return Flowable.fromIterable(targets)
		               .parallel(min(targets.size(), MAX_PARALLEL_CHANNELS))
		               .runOn(Schedulers.io())
		               .map(ClientModel::getSocket)
		               .doOnNext(socket -> {
			               ByteBuffer buffer = ByteBuffer.wrap(bytes);
			               try (AsynchronousSocketChannel channel = AsynchronousSocketChannel.open()) {
				               channel.connect(socket).get(CONNECTION_TIMEOUT, TimeUnit.MILLISECONDS);
				               while (buffer.hasRemaining())
					               channel.write(buffer);
				               buffer.clear();
			               }
		               })
		               .doOnError(throwable -> {
			               if (throwable instanceof TimeoutException) { // TODO replace by i18n messages
				               System.err.println(FG_MAGENTA.wrap("Connection timed out! \n\t" + throwable.getMessage()));
				               throwable.printStackTrace();
			               } else {
				               System.err.println(FG_MAGENTA.wrap("Error occured while sending message: \n\t" + throwable.getMessage()));
				               throwable.printStackTrace();
			               }
		               })
		               .sequential()
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
			if (client != null && client.isOpen()) {
				byte[] bytes = null;
				int    pos;
				try {
					do {
						pos = client.read(buffer).get();
						buffer.flip();
						while (buffer.hasRemaining())
							bytes = add(bytes, buffer.get());
						buffer.clear();
					} while (pos != -1);
					if (bytes != null && bytes.length > 0) {
						final TransmissionData data = (TransmissionData) deserialize(bytes);
						Flowable.fromIterable(handlers)
						        .subscribeOn(Schedulers.computation())
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
