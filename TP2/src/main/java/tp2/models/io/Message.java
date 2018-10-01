package tp2.models.io;

import org.jetbrains.annotations.NotNull;
import tp2.models.db.documents.ClientModel;
import tp2.models.db.documents.GroupModel;

import java.io.Serializable;

@SuppressWarnings("unused")
public class Message implements Serializable {
	private static final long serialVersionUID = -6416529426873270241L;
	
	private final String      message;
	private final ClientModel sender;
	private final GroupModel  target;
	
	public Message(@NotNull ClientModel sender, @NotNull String message, @NotNull GroupModel target) {
		this.sender = sender;
		this.message = message;
		this.target = target;
	}
	
	public ClientModel getSender() {
		return sender;
	}
	
	public String getMessage() {
		return message;
	}
	
	public GroupModel getTarget() {
		return target;
	}
	
}
