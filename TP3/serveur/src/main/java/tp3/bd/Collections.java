package tp3.bd;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import tp3.Constants;
import tp3.bd.entities.BaseEntity;

@SuppressWarnings("unused")
final class Collections {
	private static final Datastore DATASTORE = new Morphia()
			.mapPackage(Constants.DOCUMENTS_PACKAGE)
			.createDatastore(new MongoClient(new MongoClientURI(Constants.MONGODB_CONNECTION_STRING)),
			                 Constants.DATABASE);
	
	static {
		DATASTORE.ensureIndexes();
		DATASTORE.enableDocumentValidation();
	}
	
	@Nullable
	@SuppressWarnings("unchecked")
	public static <E extends BaseEntity> Long keyOf(@NotNull E entity) {
		Key<E> key = (Key<E>) getDatastore().exists(entity);
		return key == null ? null : (Long) key.getId();
	}
	
	public static <E extends BaseEntity> Query<E> query(@NotNull Class<E> entityClass) {
		return getDatastore().createQuery(entityClass);
	}
	
	@SuppressWarnings("unchecked")
	public static <E extends BaseEntity> E save(@NotNull E entity) {
		Key<E> key = getDatastore().save(entity);
		return getDatastore().getByKey((Class<E>) entity.getClass(), key);
	}
	
	public static boolean delete(@NotNull BaseEntity entity) {
		return getDatastore().delete(entity).getN() > 0;
	}
	
	private static Datastore getDatastore() {
		return DATASTORE;
	}
}
