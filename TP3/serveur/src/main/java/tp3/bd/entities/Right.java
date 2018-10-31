package tp3.bd.entities;

import org.jetbrains.annotations.NotNull;
import org.mongodb.morphia.annotations.*;

@Entity("rights")
@SuppressWarnings("unused")
public class Right extends BaseEntity {
	
	@Indexed(options = @IndexOptions(unique = true))
	private String key;
	
	public Right(@NotNull String key) {
		super();
		this.key = key;
	}
	
	protected Right() {
		super();
		this.key = null;
	}
	
	public String getKey() {
		return key;
	}
}
