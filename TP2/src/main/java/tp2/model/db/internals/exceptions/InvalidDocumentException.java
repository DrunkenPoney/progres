package tp2.model.db.internals.exceptions;

import static tp2.model.utils.I18n.messages;

public class InvalidDocumentException extends ValidationException {
	private static final String I18N_ERROR_MESSAGE_KEY = "error.invalid.document";
	
	public InvalidDocumentException(String objName) {
		super(String.format(messages().get(I18N_ERROR_MESSAGE_KEY), objName));
	}
	
	public InvalidDocumentException(String objName, Throwable cause) {
		super(String.format(messages().get(I18N_ERROR_MESSAGE_KEY), objName), cause);
	}
}
