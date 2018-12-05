package progres.tp4.api.dominospizzaapi.bo;

import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;

public interface IValidated {
	void validate() throws RequestValidationException;
}
