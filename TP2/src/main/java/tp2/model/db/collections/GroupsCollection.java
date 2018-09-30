package tp2.model.db.collections;

import tp2.model.db.documents.GroupModel;
import tp2.model.db.internals.BaseCollection;

import java.util.List;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("unused")
public class GroupsCollection extends BaseCollection<GroupModel> {
	
	GroupsCollection() {
		super(GroupModel.class);
	}
	
	public void save(GroupModel group) {
		publish(group);
	}
	
	public List<GroupModel> list() {
		return query().asList();
	}
	
	public GroupModel refresh(GroupModel group) {
		try {
			GroupModel model = queuedGet(query().field("_id").equal(group.getObjectId())).get();
			group.setMembers(model.getMembers());
			group.setName(model.getName());
			return group;
		} catch (InterruptedException | ExecutionException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void delete(GroupModel group) {
		drop(group);
	}
}