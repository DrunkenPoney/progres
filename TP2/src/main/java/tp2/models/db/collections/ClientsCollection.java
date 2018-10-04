package tp2.models.db.collections;

import org.jetbrains.annotations.NotNull;
import tp2.models.db.documents.ClientModel;
import tp2.models.db.internals.BaseCollection;

@SuppressWarnings("unused")
public class ClientsCollection extends BaseCollection<ClientModel> {
	
	ClientsCollection() {
		super(ClientModel.class);
	}
	
	public boolean isNameAvailable(@NotNull String name) {
		return query().field("name").equalIgnoreCase(name).count() == 0;
	}
	
	public boolean isConnected(@NotNull ClientModel model) {
		return get(model) != null;
	}
	
}
