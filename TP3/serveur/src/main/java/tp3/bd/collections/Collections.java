package tp3.bd.collections;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import tp3.Constants;

public final class Collections {
	private static final Datastore DATASTORE = new Morphia()
			.mapPackage(Constants.DOCUMENTS_PACKAGE)
			.createDatastore(new MongoClient(new MongoClientURI(Constants.MONGODB_CONNECTION_STRING)),
			                 Constants.DATABASE);
	
	static {
		DATASTORE.ensureIndexes();
		DATASTORE.enableDocumentValidation();
	}
	
	
}
