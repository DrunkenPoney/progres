package progres.tp4.api.dominospizzaapi.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import progres.tp4.api.dominospizzaapi.bo.PizzaIngredientBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IPizzaIngredientDao;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;
import progres.tp4.api.dominospizzaapi.errors.RequiredParamException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_MISSING_BODY;
import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_REQUIRED_ID;
import static progres.tp4.api.dominospizzaapi.util.Validation.validateId;

@Service
@Path("/pizza/ingredient")
public class PizzaIngredient {
	
	@Autowired
	private IPizzaIngredientDao pizzaIngredientDao;
	
	@GET
	@Path("/{id}/get")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response get(@PathParam("id") String id) throws RequiredParamException, RequestValidationException {
		return Response.ok(pizzaIngredientDao.get(validateId(id))).build();
	}
	
	@GET
	@Path("/all")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response all() {
		return Response.ok(pizzaIngredientDao.all()).build();
	}
	
	@POST
	@Path("/add")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response add(PizzaIngredientBo pi) throws RequestValidationException {
		if (pi == null)
			throw new RequestValidationException(MSG_MISSING_BODY);
		pi.validate();
		return Response.ok(pizzaIngredientDao.create(pi)).build();
	}
	
	@PUT
	@Path("/edit")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response edit(PizzaIngredientBo pi) throws RequestValidationException {
		if (pi == null)
			throw new RequestValidationException(MSG_MISSING_BODY);
		if (pi.getId() == null)
			throw new RequestValidationException(MSG_REQUIRED_ID);
		pi.validate();
		return Response.ok(pizzaIngredientDao.update(pi)).build();
	}
	
	@DELETE
	@Path("/{id}/delete")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response delete(@PathParam("id") String id) throws RequiredParamException, RequestValidationException {
		pizzaIngredientDao.delete(validateId(id));
		return Response.ok().build();
	}
}
