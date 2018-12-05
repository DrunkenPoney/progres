package progres.tp4.api.dominospizzaapi.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import progres.tp4.api.dominospizzaapi.bo.PizzaSizeBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IPizzaSizeDao;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;
import progres.tp4.api.dominospizzaapi.errors.RequiredParamException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_MISSING_BODY;
import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_REQUIRED_ID;
import static progres.tp4.api.dominospizzaapi.util.Validation.validateId;

@Service
@Path("/pizza/size")
public class PizzaSize {
	
	@Autowired
	private IPizzaSizeDao pizzaSizeDao;
	
	@GET
	@Path("/{id}/get")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response get(@PathParam("id") String id) throws RequiredParamException, RequestValidationException {
		return Response.ok(pizzaSizeDao.get(validateId(id))).build();
	}
	
	@GET
	@Path("/all")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response all() {
		return Response.ok(pizzaSizeDao.all()).build();
	}
	
	@POST
	@Path("/add")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response add(PizzaSizeBo size) throws RequestValidationException {
		if (size == null)
			throw new RequestValidationException(MSG_MISSING_BODY);
		size.validate();
		return Response.ok(pizzaSizeDao.create(size)).build();
	}
	
	@PUT
	@Path("/edit")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response edit(PizzaSizeBo size) throws RequestValidationException {
		if (size == null)
			throw new RequestValidationException(MSG_MISSING_BODY);
		if (size.getId() == null)
			throw new RequestValidationException(MSG_REQUIRED_ID);
		size.validate();
		return Response.ok(pizzaSizeDao.update(size)).build();
	}
	
	@DELETE
	@Path("/{id}/delete")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response delete(@PathParam("id") String id) throws RequiredParamException, RequestValidationException {
		pizzaSizeDao.delete(validateId(id));
		return Response.ok().build();
	}
}
