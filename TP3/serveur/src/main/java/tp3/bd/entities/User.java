package tp3.bd.entities;

import org.jetbrains.annotations.NotNull;
import org.mongodb.morphia.annotations.*;
import tp3.utils.EncryptionUtils;

import java.util.ArrayList;
import java.util.List;

@Entity("users") // TODO @Validation
@SuppressWarnings("unused")
public class User extends BaseEntity {
	@Indexed(options = @IndexOptions(unique = true))
	private String            username;
	private EncryptedPassword password;
	
	@Reference
	private List<Permission> permissions;
	
	@Reference
	private List<UserGroup> groups;
	
	public User(String username, String password) {
		this();
		this.username = username.toLowerCase();
		this.password = new EncryptedPassword(password);
	}
	
	protected User() {
		super();
		this.username = null;
		this.password = null;
		this.permissions = new ArrayList<>();
		this.groups = new ArrayList<>();
	}
	
	@PostLoad
	private void onLoad() {
		this.username = this.username.toLowerCase();
	}
	
	public String getUsername() {
		return username;
	}
	
	public EncryptedPassword getPassword() {
		return password;
	}
	
	public void setPassword(@NotNull String password) {
		this.password = new EncryptedPassword(password);
	}
	
	public List<Permission> getPermissions() {
		return permissions;
	}
	
	public List<UserGroup> getGroups() {
		return groups;
	}
	
	@Embedded
	public static class EncryptedPassword {
		private final String password, salt;
		
		public EncryptedPassword(@NotNull String password) {
			this.salt = EncryptionUtils.salt();
			this.password = EncryptionUtils.encrypt(password, salt);
		}
		
		public boolean is(String pwd) {
			return pwd != null && EncryptionUtils.encrypt(pwd, this.salt).equals(this.password);
		}
		
		public boolean is(EncryptedPassword pwd) {
			return pwd != null && pwd.password.equals(this.password);
		}
	}
}
