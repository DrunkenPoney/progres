package progres.tp4.api.dominospizzaapi.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import progres.tp4.api.dominospizzaapi.bo.CommandeBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.ICommandeDao;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;
import progres.tp4.api.dominospizzaapi.errors.RequiredParamException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_MISSING_BODY;
import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_REQUIRED_ID;
import static progres.tp4.api.dominospizzaapi.util.Validation.validateId;

@Service
@Path("/commande")
public class Commande {
	
	@Autowired
	private ICommandeDao commandeDao;
	
	@GET
	@Path("/{id}/get")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response get(@PathParam("id") String id) throws RequiredParamException, RequestValidationException {
		return Response.ok(commandeDao.get(validateId(id))).build();
	}
	
	@GET
	@Path("/all")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response all() {
		return Response.ok(commandeDao.all()).build();
	}
	
	@POST
	@Path("/add")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response add(CommandeBo commande) throws RequestValidationException {
		if (commande == null)
			throw new RequestValidationException(MSG_MISSING_BODY);
		commande.validate();
		return Response.ok(commandeDao.create(commande)).build();
	}
	
	@PUT
	@Path("/edit")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response edit(CommandeBo commande) throws RequestValidationException {
		if (commande == null)
			throw new RequestValidationException(MSG_MISSING_BODY);
		commande.validate();
		if (commande.getId() == null)
			throw new RequestValidationException(MSG_REQUIRED_ID);
		return Response.ok(commandeDao.update(commande)).build();
	}
	
	@DELETE
	@Path("/{id}/delete")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response delete(@PathParam("id") String id) throws RequiredParamException, RequestValidationException {
		commandeDao.delete(validateId(id));
		return Response.ok().build();
	}
}
