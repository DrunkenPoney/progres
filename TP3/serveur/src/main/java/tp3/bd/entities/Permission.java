package tp3.bd.entities;

import org.jetbrains.annotations.NotNull;
import org.mongodb.morphia.annotations.*;

@Entity("permissions")
@SuppressWarnings("unused")
public class Permission extends BaseEntity {
	
	@Indexed(options = @IndexOptions(unique = true))
	private String key;
	
	public Permission(@NotNull String key) {
		super();
		this.key = key;
	}
	
	protected Permission() {
		super();
		this.key = null;
	}
	
	public String getKey() {
		return key;
	}
}
