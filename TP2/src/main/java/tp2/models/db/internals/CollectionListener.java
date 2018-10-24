package tp2.models.db.internals;

import java.util.List;

public interface CollectionListener<TDocument extends BaseDocumentModel> {
	void listen(final List<TDocument> old, final List<TDocument> current);
}
