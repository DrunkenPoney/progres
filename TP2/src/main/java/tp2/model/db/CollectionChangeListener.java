package tp2.model.db;

import tp2.model.db.internals.BaseDocumentModel;

public interface CollectionChangeListener<TDocument extends BaseDocumentModel> {
	void listen(TDocument changedDocument);
}
