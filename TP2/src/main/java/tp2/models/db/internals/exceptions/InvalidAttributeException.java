package tp2.models.db.internals.exceptions;

import org.jetbrains.annotations.NotNull;
import tp2.models.utils.I18n;

import static java.lang.String.format;
import static tp2.models.utils.I18n.messages;

@SuppressWarnings("unused")
public class InvalidAttributeException extends ValidationException {
	private static final @NotNull I18n CLIENT_ATTRS_I18N = messages().withPrefix("error.invalid.attribute");
	
	public InvalidAttributeException() {
		super(CLIENT_ATTRS_I18N.get("default"));
	}
	
	public InvalidAttributeException(Throwable cause) {
		super(CLIENT_ATTRS_I18N.get("default"), cause);
	}
	
	public InvalidAttributeException(String attrName) {
		super(format(CLIENT_ATTRS_I18N.get("explicit"), attrName));
	}
	
	public InvalidAttributeException(String attrName, Throwable cause) {
		super(format(CLIENT_ATTRS_I18N.get("explicit"), attrName), cause);
	}
	
	public InvalidAttributeException(String objName, String attrName, String... args) {
		super(format(CLIENT_ATTRS_I18N.get(format("%s.%s", objName, attrName)), (Object[]) args));
	}
	
	public InvalidAttributeException(String objName, String attrName, Throwable cause, String... args) {
		super(format(CLIENT_ATTRS_I18N.get(format("%s.%s", objName, attrName)), (Object[]) args), cause);
	}
}
