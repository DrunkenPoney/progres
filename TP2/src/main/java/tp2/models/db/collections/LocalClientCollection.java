package tp2.models.db.collections;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.jetbrains.annotations.NotNull;
import tp2.models.db.documents.ClientModel;
import tp2.models.db.internals.exceptions.InvalidAttributeException;

import static tp2.models.db.collections.Accessors.getClientsCollection;
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
//		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
//			Flowable.fromIterable(getGroupsCollection().list())
//			        .filter(group -> group.getMembers().contains(getLocalClient()))
//			        .parallel()
//			        .runOn(Schedulers.io())
//			        .doOnNext(group -> group.getMembers().remove(getLocalClient()))
//			        .doOnNext(group -> group.getMembers().removeIf(member -> get(member) == null))
//			        .doOnNext(group -> {
//				        if (group.getMembers().size() > 0)
//					        getGroupsCollection().save(group);
//				        else getGroupsCollection().delete(group);
//			        })
//			        .sequential()
//			        .doOnTerminate(this::disconnect)
//			        .subscribe();
//		}));
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
