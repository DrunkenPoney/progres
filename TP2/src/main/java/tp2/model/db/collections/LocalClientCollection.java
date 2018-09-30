package tp2.model.db.collections;

import org.jetbrains.annotations.NotNull;
import tp2.model.db.documents.ClientModel;
import tp2.model.db.internals.exceptions.InvalidAttributeException;

public class LocalClientCollection extends ClientsCollection {
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
		publish(getLocalClient());
	}
	
	public void connect() {
		if (!isConnected(getLocalClient()))
			saveLocalClient();
	}
	
	public void disconnect() {
		drop(getLocalClient());
	}
}
