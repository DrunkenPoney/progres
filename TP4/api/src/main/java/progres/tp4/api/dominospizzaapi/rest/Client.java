package progres.tp4.api.dominospizzaapi.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import progres.tp4.api.dominospizzaapi.bo.ClientBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IClientDao;
import progres.tp4.api.dominospizzaapi.dao.interfaces.ICommandeDao;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;
import progres.tp4.api.dominospizzaapi.errors.RequiredParamException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.apache.commons.lang3.StringUtils.lowerCase;
import static org.apache.commons.lang3.StringUtils.trim;
import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_MISSING_BODY;
import static progres.tp4.api.dominospizzaapi.util.Utils.normalizeSpaces;
import static progres.tp4.api.dominospizzaapi.util.Validation.requiredParam;
import static progres.tp4.api.dominospizzaapi.util.Validation.validateId;

@Service
@Path("client")
public class Client {
	@Autowired
	private IClientDao clientDao;
	
	@Autowired
	private ICommandeDao commandeDao;
	
	@GET
	@Path("/{id}/get")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response get(@PathParam("id") String id) throws RequiredParamException, RequestValidationException {
		return Response.ok(clientDao.get(validateId(id))).build();
	}
	
	@GET
	@Path("/{id}/get/commandes")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response listCommandes(@PathParam("id") String clientId) throws RequiredParamException, RequestValidationException {
		return Response.ok(commandeDao.byClient(clientDao.get(validateId(clientId)))).build();
	}
	
	@GET
	@Path("/{username}/getByUsername")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response byUsername(@PathParam("username") String username) throws RequiredParamException {
		return Response.ok(clientDao.getByUsername(
			requiredParam(trim(lowerCase(normalizeSpaces(username))), "username"))).build();
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
	public Response add(ClientBo client) throws RequestValidationException {
		if (client == null)
			throw new RequestValidationException(MSG_MISSING_BODY);
		client.validate();
		clientDao.create(client);
		return Response.ok(client.getId()).build();
	}
	
	@PUT
	@Path("/edit")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response edit(ClientBo client) throws RequestValidationException {
		if (client == null) 
			throw new RequestValidationException(MSG_MISSING_BODY);
		client.validate();
		return Response.ok(clientDao.update(client)).build();
	}
	
	@DELETE
	@Path("/{id}/delete")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response delete(@PathParam("id") String id) throws RequiredParamException, RequestValidationException {
		clientDao.delete(clientDao.get(validateId(id)));
		return Response.ok().build();
	}
}
