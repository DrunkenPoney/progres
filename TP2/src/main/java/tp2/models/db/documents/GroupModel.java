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

@Entity("groups")
@SuppressWarnings("unused")
public class GroupModel extends BaseDocumentModel implements Serializable {
	private static final long serialVersionUID = -6960004063225580557L;
	
	@Reference
	private final Set<ClientModel> members;
	
	private String name;
	
	public GroupModel(@NotNull String name) {
		super(ObjectId.get());
		members = new HashSet<>();
		this.name = name;
	}
	
	private GroupModel() {
		super();
		members = new HashSet<>();
	}
	
	public Set<ClientModel> getMembers() {
		return members;
	}
	
	public boolean addMembers(@NotNull ClientModel... members) {
		return addMembers(Arrays.asList(members));
	}
	
	public boolean addMembers(@NotNull Collection<ClientModel> members) {
		return getMembers().addAll(members);
	}
	
	public boolean removeMembers(@NotNull ClientModel... members) {
		return removeMembers(Arrays.asList(members));
	}
	
	public boolean removeMembers(@NotNull Collection<ClientModel> members) {
		return getMembers().removeAll(members);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(@NotNull String name) {
		this.name = name;
	}
}
