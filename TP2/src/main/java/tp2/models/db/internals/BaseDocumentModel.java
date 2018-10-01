package tp2.models.db.internals;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;

public abstract class BaseDocumentModel implements Serializable {
	private static final long serialVersionUID = 8120070238924755152L;
	
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
