package tp2.models.db.collections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tp2.models.db.internals.exceptions.InvalidAttributeException;

import java.net.InetSocketAddress;

public final class Accessors {
	private static GroupsCollection  groupsCollection  = new GroupsCollection();
	private static ClientsCollection clientsCollection = new ClientsCollection();
	
	public static boolean isLocalClientInitialized() {
		return clientsCollection instanceof LocalClientCollection;
	}

	public static void initLocalClient(@NotNull String clientName,
	                                   @NotNull InetSocketAddress socket) throws InvalidAttributeException {
		if (isLocalClientInitialized()) {
			((LocalClientCollection) clientsCollection).getLocalClient().setName(clientName);
			((LocalClientCollection) clientsCollection).getLocalClient()
			                                           .setAddress(socket.getAddress().getHostAddress());
			((LocalClientCollection) clientsCollection).getLocalClient().setPort(socket.getPort());
			((LocalClientCollection) clientsCollection).saveLocalClient();
		} else
			clientsCollection = new LocalClientCollection(clientName,
			                                              socket.getAddress().getHostAddress(),
			                                              socket.getPort());
	}
	
	public static ClientsCollection getClientsCollection() {
		return clientsCollection;
	}
	
	@Nullable
	public static LocalClientCollection getLocalClientCollection() {
		return isLocalClientInitialized() ? (LocalClientCollection) clientsCollection : null;
	}
	
	public static GroupsCollection getGroupsCollection() {
		return groupsCollection;
	}
}
