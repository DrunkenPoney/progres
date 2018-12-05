package progres.tp4.api.dominospizzaapi.util;

import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;
import progres.tp4.api.dominospizzaapi.errors.RequiredParamException;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Long.parseLong;
import static java.lang.String.format;
import static java.util.Objects.isNull;
import static org.apache.commons.lang3.RegExUtils.replaceAll;
import static org.apache.commons.lang3.StringUtils.*;
import static progres.tp4.api.dominospizzaapi.util.Messages.*;

@SuppressWarnings("unused")
public final class Validation {
	
	public static <O> O requiredParam(O param, String name) throws RequiredParamException {
		if (param == null)
			throw new RequiredParamException(
				format(MSG_REQUIRED_PARAM, name));
		return param;
	}
	
	public static <M extends EntryVector<String, Object>> M requiredParams(M params) throws RequiredParamException {
		List<String> missing =
			params.keys()
			      .stream()
			      .filter(key -> isNull(params.get(key)))
			      .map(key -> format("%d:%s", params.indexOf(key), key))
			      .collect(Collectors.toList());
		
		if (!missing.isEmpty())
			throw new RequiredParamException(format(
				missing.size() > 1 ? MSG_REQUIRED_PARAMS : MSG_REQUIRED_PARAM,
				replaceAll(join(missing, ", "),
				           ",\\s*(\\w+)$",
				           " et $1")));
		
		return params;
	}
	
	public static long validateId(String id) throws RequiredParamException, RequestValidationException {
		requiredParam(id = trimToNull(normalizeSpace(id)), "«ID»");
		try {
			return parseLong(trim(id));
		} catch (NumberFormatException err) {
			throw new RequestValidationException(MSG_INVALID_PARAM_ID, err);
		}
	}
}
