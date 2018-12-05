package progres.tp4.api.dominospizzaapi.internals.providers;

import progres.tp4.api.dominospizzaapi.bo.rest.ErrorModel;
import progres.tp4.api.dominospizzaapi.errors.IRequestException;

import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ServiceExceptionMapper implements ExceptionMapper {
	
	@Override
	public Response toResponse(Throwable exception) {
		return Response.status(exception instanceof IRequestException
			                       || exception instanceof ValidationException
		                       ? Status.BAD_REQUEST :  Status.INTERNAL_SERVER_ERROR)
			.entity(new ErrorModel(exception))
			.build();
	}
}
