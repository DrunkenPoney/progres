package tp3.bd.entities;

import org.mongodb.morphia.annotations.Id;

@SuppressWarnings("unused")
public abstract class BaseEntity {
	@Id
	private final Long id;
	
	protected BaseEntity() {
		id = null;
	}
	
	public Long getId() {
		return id;
	}
}
