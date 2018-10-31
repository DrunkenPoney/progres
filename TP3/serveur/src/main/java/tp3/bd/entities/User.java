package tp3.bd.entities;

import org.jetbrains.annotations.NotNull;
import org.mongodb.morphia.annotations.*;
import tp3.utils.EncryptionUtils;

import java.util.ArrayList;
import java.util.List;

@Entity // TODO @Validation
@SuppressWarnings("unused")
public class User extends BaseEntity {
	@Indexed(options = @IndexOptions(unique = true))
	private String username;
	private EncryptedPassword password;
	@Reference
	private List<Right> rights;
	@Reference
	private List<UserGroup> groups;
	
	public User(String username, String password) {
		this();
		this.username = username;
		this.password = new EncryptedPassword(password);
	}
	
	protected User() {
		super();
		this.username = null;
		this.password = null;
		this.rights = new ArrayList<>();
		this.groups = new ArrayList<>();
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
	
	public List<Right> getRights() {
		return rights;
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
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof String)
				return EncryptionUtils.encrypt((String) obj, this.salt).equals(this.password);
			return obj instanceof EncryptedPassword
					&& ((EncryptedPassword) obj).password.equals(this.password);
		}
	}
}
