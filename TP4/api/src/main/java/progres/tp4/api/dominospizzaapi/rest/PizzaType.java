package progres.tp4.api.dominospizzaapi.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import progres.tp4.api.dominospizzaapi.bo.PizzaTypeBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IPizzaTypeDao;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;
import progres.tp4.api.dominospizzaapi.errors.RequiredParamException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_MISSING_BODY;
import static progres.tp4.api.dominospizzaapi.util.Validation.validateKey;

@Service
@Path("/pizza/type")
public class PizzaType {
	
	@Autowired
	private IPizzaTypeDao pizzaTypeDao;
	
	@GET
	@Path("/{key}/get")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response get(@PathParam("key") String key) throws RequiredParamException {
		return Response.ok(pizzaTypeDao.getByKey(validateKey(key))).build();
	}
	
	@GET
	@Path("/all")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response all() {
		return Response.ok(pizzaTypeDao.all()).build();
	}
	
	@POST
	@Path("/add")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response add(PizzaTypeBo type) throws RequestValidationException {
		if (type == null)
			throw new RequestValidationException(MSG_MISSING_BODY);
		type.validate();
		pizzaTypeDao.create(type);
		return Response.ok(type.getKey()).build();
	}
	
	@PUT
	@Path("/edit")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response edit(PizzaTypeBo type) throws RequestValidationException {
		if (type == null)
			throw new RequestValidationException(MSG_MISSING_BODY);
		type.validate();
		return Response.ok(pizzaTypeDao.update(type)).build();
	}
	
	@DELETE
	@Path("/{key}/delete")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response delete(@PathParam("key") String key) throws RequiredParamException {
		pizzaTypeDao.delete(pizzaTypeDao.getByKey(validateKey(key)));
		return Response.ok().build();
	}
}
