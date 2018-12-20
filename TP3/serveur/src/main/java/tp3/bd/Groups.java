package tp3.bd;

import org.jetbrains.annotations.NotNull;
import tp3.bd.entities.UserGroup;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.collections.ListUtils.unmodifiableList;

public enum Groups {
	ADMINISTRATOR("administrator", Permissions.ADMIN);
	private final UserGroup group;
	
	Groups(@NotNull String groupName, Permissions... permissions) {
		UserGroup group = Collections.query(UserGroup.class).field("name").equalIgnoreCase(groupName).get();
		if (group == null) group = Collections.save(new UserGroup(groupName));
		this.group = group;
		for(Permissions perm : permissions)
			if(perm != null) addPermission(perm);
	}
	
	public UserGroup get() {
		return group;
	}
	
	public String getName() {
		return group.getGroupName();
	}
	
	public void addPermission(@NotNull Permissions permission) {
		get().getPermissions().add(permission.get());
		Collections.save(get());
	}
	
	@SuppressWarnings("unchecked")
	public List<Permissions> getPermissions() {
		return unmodifiableList(get().getPermissions()
				.stream()
				.map(Permissions::valueOf)
				.collect(Collectors.toList()));
	}
	
	public boolean removePermission(@NotNull Permissions permission) {
		boolean removed = get().getPermissions().remove(permission.get());
		if (removed) Collections.save(get());
		return removed;
	}
}
