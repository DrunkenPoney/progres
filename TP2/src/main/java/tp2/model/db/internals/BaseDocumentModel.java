package tp2.model.db.internals;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.mongodb.morphia.annotations.Id;

public abstract class BaseDocumentModel {
	
	@Id
	private final ObjectId id;
	
	protected BaseDocumentModel(@NotNull ObjectId id) {
		this.id = id;
	}
	
	public BaseDocumentModel() {
		this.id = null;
	}
	
	public ObjectId getObjectId() {
		return this.id;
	}
	
	public String getId() {
		return getObjectId().toHexString();
	}
	
	
	@Override
	@SuppressWarnings("ConstantConditions")
	public boolean equals(Object obj) {
		if (obj == null) return false;
		return getClass().isInstance(obj) && ((BaseDocumentModel) obj).getId().equals(getId());
	}
}
