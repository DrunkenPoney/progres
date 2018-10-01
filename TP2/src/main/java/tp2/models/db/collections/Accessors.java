package tp2.models.db.collections;

import org.jetbrains.annotations.NotNull;
import tp2.models.db.internals.exceptions.InvalidAttributeException;
import tp2.models.utils.Utils;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutionException;

public final class Accessors {
	// TODO single thread pool => prevent db tasks from executing simultaneously
	
	private static GroupsCollection  groupsCollection  = new GroupsCollection();
	private static ClientsCollection clientsCollection = new ClientsCollection();
	
	public static boolean isLocalClientInitialized() {
		return clientsCollection instanceof LocalClientCollection;
	}

	public static void initLocalClient(@NotNull String clientName,
	                                   @NotNull InetSocketAddress socket) throws InvalidAttributeException, ExecutionException, InterruptedException {
		Utils.describe(clientName + " (" + socket.getAddress().getHostAddress() + ")", socket.getAddress());
		if (isLocalClientInitialized()) {
			((LocalClientCollection) clientsCollection).getLocalClient().setName(clientName);
			((LocalClientCollection) clientsCollection).getLocalClient()
			                                           .setAddress(socket.getAddress().getHostAddress());
			((LocalClientCollection) clientsCollection).getLocalClient().setPort(socket.getPort());
			((LocalClientCollection) clientsCollection).saveLocalClient().get();
		} else
			clientsCollection = new LocalClientCollection(clientName,
			                                              socket.getAddress().getHostAddress(),
			                                              socket.getPort());
	}
	
	public static ClientsCollection getClientsCollection() {
		return clientsCollection;
	}
	
	public static GroupsCollection getGroupsCollection() {
		return groupsCollection;
	}
}
