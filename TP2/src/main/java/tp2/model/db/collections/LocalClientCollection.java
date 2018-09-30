package tp2.model.db.collections;

import org.jetbrains.annotations.NotNull;
import tp2.model.db.documents.ClientModel;
import tp2.model.db.internals.exceptions.InvalidAttributeException;

public class LocalClientCollection extends ClientCollection {
	private final ClientModel localClient;
	
	LocalClientCollection(@NotNull String clientName,
	                      @NotNull String address,
	                      int port) throws InvalidAttributeException {
		this(new ClientModel(clientName, address, port));
	}
	
	private LocalClientCollection(ClientModel model) {
		super();
		localClient = model;
		connect();
		Runtime.getRuntime().addShutdownHook(new Thread(this::disconnect));
	}
	
	public ClientModel getLocalClient() {
		return this.localClient;
	}
	
	public void saveLocalClient() {
//		DATASTORE.update().set("name", getLocalClient().getName())
//		        .set("address", getLocalClient().getAddress())
//		        .set("port", getLocalClient().getPort())
//		        .set("attributes", getLocalClient().getAttributes());
		DATASTORE.save(getLocalClient());
	}
	
	public void connect() {
		if(!isConnected(getLocalClient()))
			saveLocalClient();
	}
	
	public void disconnect() {
		DATASTORE.delete(getLocalClient());
	}
}
