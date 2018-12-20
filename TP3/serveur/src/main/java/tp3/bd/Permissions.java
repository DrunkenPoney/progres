package tp3.bd;

import tp3.bd.entities.Permission;

public enum Permissions {
	ADMIN("admin");
	private final Permission permission;
	
	Permissions(String key) {
		Permission permission = Collections.query(Permission.class).field("key").equalIgnoreCase(key).get();
		if (permission == null) permission = Collections.save(new Permission(key));
		this.permission = permission;
	}
	
	public static Permissions byKey(String key) {
		for (Permissions perm : Permissions.values())
			if (perm.getKey().equalsIgnoreCase(key))
				return perm;
		return null;
	}
	
	public static Permissions valueOf(Permission permission) {
		for (Permissions perm : Permissions.values())
			if (perm.get().equals(permission))
				return perm;
		return null;
	}
	
	public Permission get() {
		return permission;
	}
	
	public Long getId() {
		return permission.getId();
	}
	
	public String getKey() {
		return permission.getKey();
	}
}
