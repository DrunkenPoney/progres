package tp2.model.db.collections;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tp2.model.db.internals.BaseCollection;
import tp2.model.utils.Constants;
import tp2.model.db.documents.ClientModel;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.mongodb.client.model.Filters.eq;

@SuppressWarnings("unused")
public class ClientCollection extends BaseCollection<ClientModel> {
	
	ClientCollection() {
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
