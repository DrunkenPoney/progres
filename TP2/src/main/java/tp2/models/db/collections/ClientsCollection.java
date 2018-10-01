package tp2.models.db.collections;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tp2.models.db.internals.BaseCollection;
import tp2.models.db.documents.ClientModel;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;

@SuppressWarnings("unused")
public class ClientsCollection extends BaseCollection<ClientModel> {
	
	ClientsCollection() {
		super(ClientModel.class);
	}
	
	public boolean isNameAvailable(@NotNull String name) {
		return query().field("name").equalIgnoreCase(name).count() == 0;
	}
	
	@NotNull
	public List<ClientModel> getOnlineClients() {
		return query().asList();
	}
	
	@Nullable
	public ClientModel getClient(@NotNull String id) {
		return getClient(new ObjectId(id));
	}
	
	@Nullable
	public ClientModel getClient(@NotNull ObjectId id) {
		return query().field("_id").equal(id).get();
	}
	
	public boolean isConnected(@NotNull ObjectId id) {
		return getClient(id) != null;
	}
	
	public boolean isConnected(@NotNull ClientModel model) {
		return isConnected(model.getObjectId());
	}
}