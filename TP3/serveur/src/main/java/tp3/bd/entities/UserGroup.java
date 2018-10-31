package tp3.bd.entities;

import org.jetbrains.annotations.NotNull;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

import java.util.ArrayList;
import java.util.List;

@Entity("groups")
@SuppressWarnings("unused")
public class UserGroup extends BaseEntity {
	private String groupName;
	
	@Reference
	private List<Right> rights;
	
	protected UserGroup() {
		super();
		this.groupName = null;
		rights = new ArrayList<>();
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public void setGroupName(@NotNull String groupName) {
		this.groupName = groupName;
	}
	
	public List<Right> getRights() {
		return rights;
	}
}
