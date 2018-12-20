package tp3.bd.entities;

import org.jetbrains.annotations.NotNull;
import org.mongodb.morphia.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Entity("groups")
@SuppressWarnings("unused")
public class UserGroup extends BaseEntity {
	@Property("name")
	@Indexed(options = @IndexOptions(unique = true))
	private String groupName;
	
	@Reference
	private List<Permission> permissions;
	
	public UserGroup(@NotNull String groupName) {
		super();
		this.groupName = groupName;
		this.permissions = new ArrayList<>();
	}
	
	protected UserGroup() {
		super();
		this.groupName = null;
		this.permissions = new ArrayList<>();
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public void setGroupName(@NotNull String groupName) {
		this.groupName = groupName;
	}
	
	public List<Permission> getPermissions() {
		return permissions;
	}
}
