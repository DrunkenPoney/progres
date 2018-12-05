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

import static org.apache.commons.lang3.StringUtils.*;
import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_MISSING_BODY;
import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_REQUIRED_ID;
import static progres.tp4.api.dominospizzaapi.util.Validation.requiredParam;
import static progres.tp4.api.dominospizzaapi.util.Validation.validateId;

@Service
@Path("/ingredient")
public class Ingredient {
	
	@Autowired
	private IIngredientDao ingredientDao;
	
	@GET
	@Path("/{id}/get")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response get(@PathParam("id") String id) throws RequiredParamException, RequestValidationException {
		return Response.ok(ingredientDao.get(validateId(id))).build();
	}
	
	@GET
	@Path("/{key}/getByKey")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getByKey(@PathParam("key") String key) throws RequiredParamException {
		return Response.ok(ingredientDao.getByKey(requiredParam(upperCase(trimToNull(normalizeSpace(key))), "key"))).build();
	}
	
	@GET
	@Path("/all")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response all() {
		return Response.ok(ingredientDao.all()).build();
	}
	
	@POST
	@Path("/add")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response add(IngredientBo ingredient) throws RequestValidationException {
		if (ingredient == null)
			throw new RequestValidationException(MSG_MISSING_BODY);
		ingredient.validate();
		return Response.ok(ingredientDao.create(ingredient)).build();
	}
	
	@PUT
	@Path("/edit")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response edit(IngredientBo ingredient) throws RequestValidationException {
		if (ingredient == null)
			throw new RequestValidationException(MSG_MISSING_BODY);
		if (ingredient.getId() == null)
			throw new RequestValidationException(MSG_REQUIRED_ID);
		ingredient.validate();
		return Response.ok(ingredientDao.update(ingredient)).build();
	}
	
	@DELETE
	@Path("/{id}/delete")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response delete(@PathParam("id") String id) throws RequiredParamException, RequestValidationException {
		ingredientDao.delete(validateId(id));
		return Response.ok().build();
	}
}
