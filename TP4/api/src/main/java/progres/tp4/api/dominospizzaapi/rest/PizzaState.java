package progres.tp4.api.dominospizzaapi.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import progres.tp4.api.dominospizzaapi.bo.PizzaStateBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IPizzaStateDao;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;
import progres.tp4.api.dominospizzaapi.errors.RequiredParamException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_MISSING_BODY;
import static progres.tp4.api.dominospizzaapi.util.Validation.validateKey;

@Component
@Path("/pizza/state")
public class PizzaState {
	
	@Autowired
	private IPizzaStateDao pizzaStateDao;
	
	@GET
	@Path("/{key}/get")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response get(@PathParam("key") String key) throws RequiredParamException {
		return Response.ok(pizzaStateDao.getByKey(validateKey(key))).build();
	}
	
	@GET
	@Path("/all")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response all() {
		return Response.ok(pizzaStateDao.all()).build();
	}
	
	@POST
	@Path("/add")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response add(PizzaStateBo state) throws RequestValidationException {
		if (state == null)
			throw new RequestValidationException(MSG_MISSING_BODY);
		state.validate();
		pizzaStateDao.create(state);
		return Response.ok(state.getKey()).build();
	}
	
	@PUT
	@Path("/edit")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response edit(PizzaStateBo state) throws RequestValidationException {
		if (state == null)
			throw new RequestValidationException(MSG_MISSING_BODY);
		state.validate();
		return Response.ok(pizzaStateDao.update(state)).build();
	}
	
	@DELETE
	@Path("/{key}/delete")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response delete(@PathParam("key") String key) throws RequiredParamException {
		pizzaStateDao.delete(pizzaStateDao.getByKey(validateKey(key)));
		return Response.ok().build();
	}
}
