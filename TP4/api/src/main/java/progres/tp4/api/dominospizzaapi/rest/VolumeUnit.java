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

import static org.apache.commons.lang3.StringUtils.trimToNull;
import static org.apache.commons.lang3.StringUtils.upperCase;
import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_MISSING_BODY;
import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_REQUIRED_ID;
import static progres.tp4.api.dominospizzaapi.util.Utils.normalizeSpaces;
import static progres.tp4.api.dominospizzaapi.util.Validation.requiredParam;
import static progres.tp4.api.dominospizzaapi.util.Validation.validateId;

@Service
@Path("/volume-unit")
public class VolumeUnit {
	
	@Autowired
	private IVolumeUnitDao volumeUnitDao;
	
	@GET
	@Path("/{id}/get")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response get(@PathParam("id") String id) throws RequiredParamException, RequestValidationException {
		return Response.ok(volumeUnitDao.get(validateId(id))).build();
	}
	
	@GET
	@Path("/{key}/getByKey")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getByKey(@PathParam("key") String key) throws RequiredParamException {
		return Response.ok(volumeUnitDao.getByKey(requiredParam(upperCase(trimToNull(normalizeSpaces(key))), "key")))
		               .build();
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
		return Response.ok(volumeUnitDao.create(volume)).build();
	}
	
	@PUT
	@Path("/edit")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response edit(VolumeUnitBo volume) throws RequestValidationException {
		if (volume == null)
			throw new RequestValidationException(MSG_MISSING_BODY);
		if (volume.getId() == null)
			throw new RequestValidationException(MSG_REQUIRED_ID);
		volume.validate();
		return Response.ok(volumeUnitDao.update(volume)).build();
	}
	
	@DELETE
	@Path("/{id}/delete")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response delete(@PathParam("id") String id) throws RequiredParamException, RequestValidationException {
		volumeUnitDao.delete(validateId(id));
		return Response.ok().build();
	}
}
