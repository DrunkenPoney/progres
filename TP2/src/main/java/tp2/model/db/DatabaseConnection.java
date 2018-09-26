package tp2.model.db;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tp2.model.Constants;

import java.io.IOException;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.*;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@SuppressWarnings("unchecked")
public class DatabaseConnection {
	private static final Queue<CollectionChangedCallback<ClientModel>> collectionChangedCallbacks = new PriorityQueue<>();
	
	private static final MongoCollection<ClientModel> collection = MongoClients
			.create(Constants.MONGODB_CONNECTION_STRING)
			.getDatabase(Constants.DATABASE)
			.withCodecRegistry(fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
			                                  fromProviders(PojoCodecProvider.builder().automatic(true).build())))
			.getCollection(Constants.CLIENTS_COLLECTION_NAME)
			.withDocumentClass(ClientModel.class);
	
	private static boolean collectionListenerInitialized = false;
	
	private final ClientModel localClient;
	
	public DatabaseConnection(@NotNull String name,
	                          @NotNull AsynchronousServerSocketChannel inputChannel) throws IOException, ClientModel.InvalidClientAttributeException {
		localClient = new ClientModel(name, inputChannel.getLocalAddress());
		Runtime.getRuntime().addShutdownHook(new Thread(this::disconnect));
	}
	
	public static boolean addOnClientStateChanged(CollectionChangedCallback<ClientModel> callback) {
		if (!collectionListenerInitialized) {
			collectionListenerInitialized = true;
			collection.watch()
					.map(ChangeStreamDocument::getFullDocument)
					.forEach((Block<ClientModel>) o -> collectionChangedCallbacks.forEach(c -> c.exec(o)));
//					.map((Function<ChangeStreamDocument<Document>, Document>) ChangeStreamDocument::getFullDocument)
//					.map((Function<Document, ClientModel>) ClientModel::parseDocument)
//					.forEach((Block<ClientModel>) o -> collectionChangedCallbacks.forEach(c -> c.exec(o)));
		}
		return collectionChangedCallbacks.offer(callback);
	}
	
	
	public Set<ClientModel> getOnlineClients() {
		Set<ClientModel> models = new HashSet();
		collection.find().into(models);
//				.map((Function<Document, ClientModel>) ClientModel::parseDocument)
		return models;
	}
	
	public ClientModel getClient() {
		return getClient((ObjectId) null);
	}
	
	@Contract("null -> !null; !null -> _")
	public ClientModel getClient(@Nullable String id) {
		return getClient(id == null ? null : new ObjectId(id));
	}
	
	@Contract("null -> !null; !null -> _")
	public ClientModel getClient(@Nullable ObjectId id) {
		if (id == null) return localClient;
		return collection.find(Aggregates.match(Filters.eq("_id", id))).first();
	}
	
	public boolean isConnected() {
		return getOnlineClients().contains(localClient);
	}
	
	public DatabaseConnection disconnect() {
		if (isConnected())
			collection.findOneAndDelete(getLocalFilter());
		return this;
	}
	
	public DatabaseConnection connect() {
		if (!isConnected())
			collection.insertOne(localClient);
		return this;
	}
	
	public void uploadChanges() {
		collection.findOneAndReplace(getLocalFilter(), localClient);
	}
	
	public interface CollectionChangedCallback<T> {
		void exec(T changed);
	}
	
	private Bson getLocalFilter() {
		return Aggregates.match(Filters.eq("_id", localClient.getId()));
	}
}
