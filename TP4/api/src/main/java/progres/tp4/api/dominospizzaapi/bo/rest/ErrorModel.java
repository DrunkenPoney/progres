package progres.tp4.api.dominospizzaapi.bo.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.jetbrains.annotations.NotNull;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.*;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.*;

@JsonAutoDetect(
	fieldVisibility = NONE,
	getterVisibility = NONE,
	setterVisibility = NONE,
	isGetterVisibility = NONE,
	creatorVisibility = ANY)
@JsonInclude(NON_NULL)
@SuppressWarnings("unused")
public class ErrorModel {
	
	@JsonProperty
	private String type;
	
	@JsonProperty
	private String message;
	
	@JsonProperty
	private String trace;
	
	@JsonProperty
	private ErrorModel cause;
	
	private ErrorModel() {}
	
	public ErrorModel(@NotNull Throwable err) {
		this.type = err.getClass().getTypeName();
		this.message = ExceptionUtils.getMessage(err);
		this.trace = ExceptionUtils.getStackTrace(err);
		this.cause = err.getCause() == null ? null : new ErrorModel(err);
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getTrace() {
		return trace;
	}
	
	public ErrorModel getCause() {
		return cause;
	}
	
	public String getType() {
		return type;
	}
}
