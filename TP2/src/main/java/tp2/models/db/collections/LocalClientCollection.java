package tp2.models.db.collections;

import org.jetbrains.annotations.NotNull;
import tp2.models.db.documents.ClientModel;
import tp2.models.db.documents.GroupModel;
import tp2.models.db.internals.exceptions.InvalidAttributeException;

import static tp2.models.db.collections.Accessors.getGroupsCollection;

public class LocalClientCollection extends ClientsCollection {
	private final ClientModel localClient;
	
	LocalClientCollection(@NotNull String clientName,
	                      @NotNull String address,
	                      int port) throws InvalidAttributeException {
		this(new ClientModel(clientName, address, port));
	}
	
	private LocalClientCollection(@NotNull ClientModel model) {
		super();
		localClient = model;
		connect();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			final GroupModel grToDel = localClient.getGroup() != null
			                           && query().asList()
			                                     .stream()
			                                     .filter(client -> !client.equals(localClient))
			                                     .map(ClientModel::getGroup)
			                                     .noneMatch(group -> group.equals(localClient.getGroup()))
			                           ? localClient.getGroup() : null;
			disconnect();
			if (grToDel != null)
				getGroupsCollection().delete(grToDel);
		}));
	}
	
	public ClientModel getLocalClient() {
		return this.localClient;
	}
	
	public ClientModel saveLocalClient() {
		return super.save(getLocalClient());
	}
	
	public ClientModel connect() {
		return isConnected(getLocalClient())
		       ? getLocalClient()
		       : saveLocalClient();
	}
	
	public boolean disconnect() {
		return getDatastore().delete(getLocalClient()).wasAcknowledged();
	}
}
