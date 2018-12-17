package progres.tp4.api.dominospizzaapi.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import progres.tp4.api.dominospizzaapi.bo.IngredientBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IIngredientDao;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;
import progres.tp4.api.dominospizzaapi.errors.RequiredParamException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_MISSING_BODY;
import static progres.tp4.api.dominospizzaapi.util.Validation.validateKey;

@Service
@Path("/ingredient")
public class Ingredient {
	
	@Autowired
	private IIngredientDao ingredientDao;
	
	@GET
	@Path("/{key}/get")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response get(@PathParam("key") String key) throws RequiredParamException {
		return Response.ok(ingredientDao.getByKey(validateKey(key))).build();
	}
	
	@GET
	@Path("/all")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response all() {
		return Response.ok(ingredientDao.all()).build();
	}
	
	@POST
	@Path("/add")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response add(IngredientBo ingredient) throws RequestValidationException {
		if (ingredient == null)
			throw new RequestValidationException(MSG_MISSING_BODY);
		ingredient.validate();
		ingredientDao.create(ingredient);
		return Response.ok(ingredient.getKey()).build();
	}
	
	@PUT
	@Path("/edit")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response edit(IngredientBo ingredient) throws RequestValidationException {
		if (ingredient == null)
			throw new RequestValidationException(MSG_MISSING_BODY);
		ingredient.validate();
		return Response.ok(ingredientDao.update(ingredient)).build();
	}
	
	@DELETE
	@Path("/{key}/delete")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response delete(@PathParam("key") String key) throws RequiredParamException {
		ingredientDao.delete(ingredientDao.getByKey(validateKey(key)));
		return Response.ok().build();
	}
}
