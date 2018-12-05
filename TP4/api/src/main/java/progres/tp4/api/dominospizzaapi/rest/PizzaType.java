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
import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_REQUIRED_ID;
import static progres.tp4.api.dominospizzaapi.util.Validation.validateId;

@Service
@Path("/pizza/type")
public class PizzaType {
	
	@Autowired
	private IPizzaTypeDao clientDao;
	
	@GET
	@Path("/{id}/get")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response get(@PathParam("id") String id) throws RequiredParamException, RequestValidationException {
		return Response.ok(clientDao.get(validateId(id))).build();
	}
	
	@GET
	@Path("/all")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response all() {
		return Response.ok(clientDao.all()).build();
	}
	
	@POST
	@Path("/add")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response add(PizzaTypeBo type) throws RequestValidationException {
		if (type == null)
			throw new RequestValidationException(MSG_MISSING_BODY);
		type.validate();
		return Response.ok(clientDao.create(type)).build();
	}
	
	@PUT
	@Path("/edit")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response edit(PizzaTypeBo type) throws RequestValidationException {
		if (type == null)
			throw new RequestValidationException(MSG_MISSING_BODY);
		if (type.getId() == null)
			throw new RequestValidationException(MSG_REQUIRED_ID);
		type.validate();
		return Response.ok(clientDao.update(type)).build();
	}
	
	@DELETE
	@Path("/{id}/delete")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response delete(@PathParam("id") String id) throws RequiredParamException, RequestValidationException {
		clientDao.delete(validateId(id));
		return Response.ok().build();
	}
}
