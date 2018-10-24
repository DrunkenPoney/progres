package tp2.models.db.collections;

import org.jetbrains.annotations.NotNull;
import tp2.models.db.documents.ClientModel;
import tp2.models.db.documents.GroupModel;
import tp2.models.db.internals.BaseCollection;

import java.util.List;

import static tp2.models.db.collections.Accessors.getClientsCollection;

@SuppressWarnings("unused")
public class GroupsCollection extends BaseCollection<GroupModel> {
	GroupsCollection() {
		super(GroupModel.class);
	}
	
	public GroupModel save(@NotNull GroupModel group) {
		return super.save(group);
	}
	
	public List<GroupModel> list() {
		return query().asList();
	}
	
	public boolean delete(GroupModel group) {
		return getDatastore().delete(group).wasAcknowledged();
	}
	
	public List<ClientModel> membersOf(GroupModel group) {
		return getClientsCollection().query().field("group").equal(group).asList();
	}
}