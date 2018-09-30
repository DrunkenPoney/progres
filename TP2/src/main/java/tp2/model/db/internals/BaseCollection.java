package tp2.model.db.internals;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.jetbrains.annotations.NotNull;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import tp2.model.utils.Constants;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

@SuppressWarnings("unused")
public abstract class BaseCollection<TDocument extends BaseDocumentModel> {
	private static final Datastore                DATASTORE = new Morphia()
			.mapPackage(Constants.DOCUMENTS_PACKAGE)
			.createDatastore(new MongoClient(new MongoClientURI(Constants.MONGODB_CONNECTION_STRING)),
			                 Constants.DATABASE);
	private final static ScheduledExecutorService EXECUTOR  = Executors.newSingleThreadScheduledExecutor();
	
	static {
		DATASTORE.ensureIndexes();
		DATASTORE.enableDocumentValidation();
	}
	
	private final Class<TDocument> modelClass;
	
	protected BaseCollection(@NotNull final Class<TDocument> modelClass) {
		this.modelClass = modelClass;
	}
	
	protected static ScheduledExecutorService getExecutor() {
		return EXECUTOR;
	}
	
	protected Query<TDocument> query() {
		return DATASTORE.createQuery(modelClass);
	}
	
	protected Future<?> drop(TDocument doc) {
		return EXECUTOR.submit(() -> DATASTORE.delete(doc));
	}
	
	protected Future<Key<TDocument>> publish(TDocument doc) {
		return EXECUTOR.submit(() -> DATASTORE.save(doc));
	}
	
	protected Future<TDocument> queuedGet(@NotNull Query<TDocument> query) {
		return EXECUTOR.submit((Callable<TDocument>) query::get);
	}
	
	protected Future<List<TDocument>> queuedList(@NotNull Query<TDocument> query) {
		return EXECUTOR.submit((Callable<List<TDocument>>) query::asList);
	}
	
}
