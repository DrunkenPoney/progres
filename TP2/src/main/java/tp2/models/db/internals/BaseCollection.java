package tp2.models.db.internals;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import tp2.models.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public abstract class BaseCollection<TDocument extends BaseDocumentModel<TDocument>> {
	private static final Datastore DATASTORE = new Morphia()
			.mapPackage(Constants.DOCUMENTS_PACKAGE)
			.createDatastore(new MongoClient(new MongoClientURI(Constants.MONGODB_CONNECTION_STRING)),
			                 Constants.DATABASE);
	
	static {
		DATASTORE.ensureIndexes();
		DATASTORE.enableDocumentValidation();
	}
	
	private final ConcurrentMap<TDocument, Consumer<TDocument>> modelListeners;
	private final List<CollectionListener<TDocument>>           listeners;
	private final Class<TDocument>                              modelClass;
	
	
	protected BaseCollection(final @NotNull Class<TDocument> modelClass) {
		this.modelClass = modelClass;
		this.listeners = new ArrayList<>();
		this.modelListeners = new ConcurrentHashMap<>();
		
		final List<TDocument> prevDocs = query().asList();
		Observable.<List<TDocument>>create(subscriber -> {
			List<TDocument> docs = query().asList();
			if (docs.size() != prevDocs.size() || !docs.containsAll(prevDocs))
				subscriber.onNext(docs);
			subscriber.onComplete();
		}).subscribeOn(Schedulers.io())
		  .doOnNext(docs -> Flowable
				  .fromIterable(listeners)
				  .parallel()
				  .runOn(Schedulers.computation())
				  .doOnNext(listener -> listener.listen(new ArrayList<>(prevDocs), new ArrayList<>(docs)))
				  .doOnError(throwable -> System.err.println(throwable.getMessage()))
				  .sequential()
				  .subscribe())
		  .doOnNext(docs -> prevDocs.clear())
		  .doOnNext(prevDocs::addAll)
		  .repeat()
		  .subscribe();
		
	}
	
	protected static Datastore getDatastore() {
		return DATASTORE;
	}
	
	public Query<TDocument> query() {
		return getDatastore().createQuery(modelClass);
	}
	
	protected TDocument save(@NotNull TDocument doc) {
		doc.setId((ObjectId) getDatastore().save(doc).getId());
		return doc;
	}
	
	public boolean addListener(@NotNull CollectionListener<TDocument> listener) {
		return listeners.add(listener);
	}
	
	public boolean removeListener(@NotNull CollectionListener<TDocument> listener) {
		return listeners.remove(listener);
	}
	
	public TDocument get(@NotNull TDocument doc) {
		return getDatastore().get(doc);
	}
	
	public Consumer<TDocument> getModelListener(@NotNull TDocument target) {
		return modelListeners.get(target);
	}
	
	public void setModelListener(@NotNull TDocument target, @Nullable Consumer<TDocument> callback) {
		if (callback == null) modelListeners.remove(target);
		else modelListeners.put(target, callback);
	}
	
	public void removeModelListener(@NotNull TDocument target) {
		setModelListener(target, null);
	}
}
