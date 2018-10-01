package tp2.models.db.collections;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import tp2.models.db.documents.ClientModel;
import tp2.models.db.internals.exceptions.InvalidAttributeException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class LocalClientCollection extends ClientsCollection {
	private final ClientModel localClient;
	
	LocalClientCollection(@NotNull String clientName,
	                      @NotNull String address,
	                      int port) throws InvalidAttributeException, ExecutionException, InterruptedException {
		this(new ClientModel(clientName, address, port));
	}
	
	private LocalClientCollection(@NotNull ClientModel model) throws ExecutionException, InterruptedException {
		super();
		localClient = model;
		connect().get();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				disconnect().get();
			} catch (InterruptedException | ExecutionException e) {
				throw new RuntimeException(e);
			}
		}));
	}
	
	public ClientModel getLocalClient() {
		return this.localClient;
	}
	
	public Future<ObjectId> saveLocalClient() {
		return publish(getLocalClient());
	}
	
	public Future<?> connect() {
		if (!isConnected(getLocalClient()))
			return saveLocalClient();
		return new FutureTask<>(this::getLocalClient);
	}
	
	public Future<?> disconnect() {
		return drop(getLocalClient());
	}
}
