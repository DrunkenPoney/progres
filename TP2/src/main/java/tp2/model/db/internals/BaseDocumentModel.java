package tp2.model.db.internals;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.mongodb.morphia.annotations.Id;

public abstract class BaseDocumentModel {
	
	@Id
	private final ObjectId id;
	
	protected BaseDocumentModel(ObjectId id) {
		this.id = id;
	}
	
	public ObjectId getObjectId() {
		return this.id;
	}
	
	public String getId() {
		return getObjectId().toHexString();
	}
	
	protected abstract <C extends BaseDocumentModel> C parse(@NotNull Document document) throws Exception;
	
	@Override
	@SuppressWarnings("ConstantConditions")
	public boolean equals(Object obj) {
		if (obj == null) return false;
		
		if (obj instanceof Document) {
			try {
				obj = parse((Document) obj);
			} catch (Exception ignored) {
			}
		}
		
		return getClass().isInstance(obj) && ((BaseDocumentModel) obj).getId().equals(getId());
	}
}
