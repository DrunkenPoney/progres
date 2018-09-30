package tp2.model.db.documents;

import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;
import tp2.model.db.internals.BaseDocumentModel;

import java.util.HashSet;
import java.util.Set;

@Entity("groups")
public class GroupModel extends BaseDocumentModel {
	
	private String name;
	
	@Reference
	private Set<ClientModel> members;
	
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
	
	public void setMembers(@NotNull Set<ClientModel> members) {
		this.members = members;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(@NotNull String name) {
		this.name = name;
	}
}
