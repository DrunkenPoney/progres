package tp3.bd.collections;

import org.mongodb.morphia.Datastore;

abstract class BaseCollection {
	private final Datastore datastore;
	
	BaseCollection(Datastore datastore) {
		this.datastore = datastore;
	}
	
	public Datastore getDatastore() {
		return datastore;
	}
}
