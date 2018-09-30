package tp2.model.db.collections;

import org.jetbrains.annotations.NotNull;
import tp2.model.db.internals.exceptions.InvalidAttributeException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.AsynchronousServerSocketChannel;

public final class Accessors {
	private static ClientCollection clientCollection = new ClientCollection();
	
	public static boolean isLocalClientInitialized() {
		return clientCollection instanceof LocalClientCollection;
	}
	
	public static void initLocalClient(@NotNull String clientName,
	                                   @NotNull AsynchronousServerSocketChannel channel) throws IOException, InvalidAttributeException {
		setLocalClient(clientName, (InetSocketAddress) channel.getLocalAddress());
	}
	
	public static void initLocalClient(@NotNull String clientName,
	                                   @NotNull ServerSocket socket) throws InvalidAttributeException {
		setLocalClient(clientName, (InetSocketAddress) socket.getLocalSocketAddress());
	}
	
	private static void setLocalClient(@NotNull String clientName, @NotNull InetSocketAddress socket) throws InvalidAttributeException {
		if (isLocalClientInitialized()) {
			((LocalClientCollection) clientCollection).getLocalClient().setName(clientName);
			((LocalClientCollection) clientCollection).getLocalClient()
					.setAddress(socket.getAddress().getHostAddress());
			((LocalClientCollection) clientCollection).getLocalClient().setPort(socket.getPort());
			((LocalClientCollection) clientCollection).saveLocalClient();
		} else
			clientCollection = new LocalClientCollection(clientName,
			                                             socket.getAddress().getHostAddress(),
			                                             socket.getPort());
	}
	
	public static ClientCollection getClientCollection() {
		return clientCollection;
	}
}
