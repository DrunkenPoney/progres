package progres.tp4.api.dominospizzaapi.bo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import javax.persistence.Id;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

@JsonAutoDetect(
	fieldVisibility = ANY,
	isGetterVisibility = NONE,
	getterVisibility = NONE,
	setterVisibility = NONE,
	creatorVisibility = DEFAULT)
public abstract class BaseBo implements IValidated {
	
	@Id
	public abstract Long getId();
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof BaseBo && ( (BaseBo) obj ).getId().equals(getId());
	}
	
}
