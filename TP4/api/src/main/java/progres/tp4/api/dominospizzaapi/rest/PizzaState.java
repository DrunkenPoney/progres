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

import static org.apache.commons.lang3.StringUtils.*;
import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_MISSING_BODY;
import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_REQUIRED_ID;
import static progres.tp4.api.dominospizzaapi.util.Validation.requiredParam;
import static progres.tp4.api.dominospizzaapi.util.Validation.validateId;

@Component
@Path("/pizza/state")
public class PizzaState {
	
	@Autowired
	private IPizzaStateDao pizzaStateDao;
	
	@GET
	@Path("/{id}/get")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response get(@PathParam("id") String id) throws RequiredParamException, RequestValidationException {
		return Response.ok(pizzaStateDao.get(validateId(id))).build();
	}
	
	@GET
	@Path("/{key}/getByKey")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getByKey(@PathParam("key") String key) throws RequiredParamException {
		return Response.ok(pizzaStateDao.getByKey(requiredParam(upperCase(trimToNull(normalizeSpace(key))), "key"))).build();
	}
	
	@GET
	@Path("/all")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response all() {
		return Response.ok(pizzaStateDao.all()).build();
	}
	
	@POST
	@Path("/add")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response add(PizzaStateBo state) throws RequestValidationException {
		if (state == null)
			throw new RequestValidationException(MSG_MISSING_BODY);
		state.validate();
		return Response.ok(pizzaStateDao.create(state)).build();
	}
	
	@PUT
	@Path("/edit")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response edit(PizzaStateBo state) throws RequestValidationException {
		if (state == null)
			throw new RequestValidationException(MSG_MISSING_BODY);
		if (state.getId() == null)
			throw new RequestValidationException(MSG_REQUIRED_ID);
		state.validate();
		return Response.ok(pizzaStateDao.update(state)).build();
	}
	
	@DELETE
	@Path("/{id}/delete")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response delete(@PathParam("id") String id) throws RequiredParamException, RequestValidationException {
		pizzaStateDao.delete(validateId(id));
		return Response.ok().build();
	}
}
