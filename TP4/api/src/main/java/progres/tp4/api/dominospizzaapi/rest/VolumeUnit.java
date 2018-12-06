package progres.tp4.api.dominospizzaapi.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import progres.tp4.api.dominospizzaapi.bo.VolumeUnitBo;
import progres.tp4.api.dominospizzaapi.dao.interfaces.IVolumeUnitDao;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;
import progres.tp4.api.dominospizzaapi.errors.RequiredParamException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_MISSING_BODY;
import static progres.tp4.api.dominospizzaapi.util.Validation.validateKey;

@Service
@Path("/volume-unit")
public class VolumeUnit {
	
	@Autowired
	private IVolumeUnitDao volumeUnitDao;
	
	@GET
	@Path("/{key}/get")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response get(@PathParam("key") String key) throws RequiredParamException {
		return Response.ok(volumeUnitDao.getByKey(validateKey(key))).build();
	}
	
	@GET
	@Path("/all")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response all() {
		return Response.ok(volumeUnitDao.all()).build();
	}
	
	@POST
	@Path("/add")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response add(VolumeUnitBo volume) throws RequestValidationException {
		if (volume == null)
			throw new RequestValidationException(MSG_MISSING_BODY);
		volume.validate();
		volumeUnitDao.create(volume);
		return Response.ok(volume.getKey()).build();
	}
	
	@PUT
	@Path("/edit")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response edit(VolumeUnitBo volume) throws RequestValidationException {
		if (volume == null)
			throw new RequestValidationException(MSG_MISSING_BODY);
		volume.validate();
		return Response.ok(volumeUnitDao.update(volume)).build();
	}
	
	@DELETE
	@Path("/{key}/delete")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response delete(@PathParam("key") String key) throws RequiredParamException {
		volumeUnitDao.delete(volumeUnitDao.getByKey(validateKey(key)));
		return Response.ok().build();
	}
}
