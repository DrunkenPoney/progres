package progres.tp4.api.dominospizzaapi.bo;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;

@JsonAutoDetect(
	fieldVisibility = ANY,
	isGetterVisibility = NONE,
	getterVisibility = NONE,
	setterVisibility = NONE,
	creatorVisibility = DEFAULT)
public interface IBaseBo {
	void validate() throws RequestValidationException;
}
