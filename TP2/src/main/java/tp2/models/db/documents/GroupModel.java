package tp2.models.db.documents;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;
import tp2.models.db.internals.BaseDocumentModel;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static tp2.models.utils.Utils.same;

@Entity("groups")
@SuppressWarnings("unused")
public class GroupModel extends BaseDocumentModel<GroupModel> implements Serializable {
	private static final long serialVersionUID = -6960004063225580557L;
	
	private String name;
	
	public GroupModel(@NotNull String name) {
		super(ObjectId.get());
		this.name = name;
	}
	
	private GroupModel() {
		super();
	}
	
	@Override
	public boolean isExactSame(GroupModel doc1) {
		return equals(doc1) && getName().equals(doc1.getName());
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(@NotNull String name) {
		this.name = name;
	}
	
}
