package tp2.models.io.data;

import org.jetbrains.annotations.NotNull;
import tp2.models.db.documents.ClientModel;
import tp2.models.db.documents.GroupModel;

import java.io.Serializable;

@SuppressWarnings("unused")
public class MessageData extends TransmissionData implements Serializable {
	private static final long serialVersionUID = -6416529426873270241L;
	
	private final String message;
	
	public MessageData(@NotNull ClientModel sender, @NotNull String message, @NotNull GroupModel target) {
		super(sender, target);
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
