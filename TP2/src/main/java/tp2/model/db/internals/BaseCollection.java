package tp2.model.db.internals;

import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.jetbrains.annotations.NotNull;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import tp2.model.db.CollectionChangeListener;
import tp2.model.utils.Constants;

import java.util.PriorityQueue;
import java.util.Queue;

public abstract class BaseCollection<TDocument extends BaseDocumentModel> {
	protected static final Datastore DATASTORE = new Morphia()
			.mapPackage(Constants.DOCUMENTS_PACKAGE)
			.createDatastore(new MongoClient(new MongoClientURI(Constants.MONGODB_CONNECTION_STRING)), Constants.DATABASE);
	
	static {
		DATASTORE.ensureIndexes();
		DATASTORE.enableDocumentValidation();
	}

//	private static final MongoDatabase DATABASE = MongoClients
//			.create(Constants.MONGODB_CONNECTION_STRING)
//			.getDatabase(Constants.DATABASE)
//			.withCodecRegistry(fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
//			                                  fromProviders(PojoCodecProvider.builder().automatic(true).build())));
	
	protected final DBCollection                               collection;
	private final   Queue<CollectionChangeListener<TDocument>> listeners;
	private final Class<TDocument> modelClass;
	
	protected BaseCollection(@NotNull final Class<TDocument> modelClass) {
		this.modelClass = modelClass;
		this.listeners = new PriorityQueue<>();
		this.collection = DATASTORE.getCollection(modelClass);
//		this.collection = DATABASE
//				.getCollection(collectionName)
//				.withDocumentClass(modelClass);
	}
	
	protected UpdateOperations<TDocument> update() {
		return DATASTORE.createUpdateOperations(modelClass);
	}
	
	protected Query<TDocument> query() {
		return DATASTORE.createQuery(modelClass);
	}
	
	// TODO watcher
	
	public boolean addCollectionListener(CollectionChangeListener<TDocument> listener) {
		return listeners.offer(listener);
	}
	
	public boolean removeCollectionListener(CollectionChangeListener<TDocument> listener) {
		return listeners.remove(listener);
	}
}
