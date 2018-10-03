package tp2.models.io;

import tp2.models.db.documents.ClientModel;
import tp2.models.db.documents.GroupModel;

import java.io.Serializable;

public abstract class TransmissionData implements Serializable {
	private static final long serialVersionUID = -5422701434882388657L;
	
	private final ClientModel sender;
	private final GroupModel  target;
	
	protected TransmissionData(ClientModel sender, GroupModel target) {
		this.sender = sender;
		this.target = target;
	}
	
	public ClientModel getSender() {
		return sender;
	}
	
	public GroupModel getTarget() {
		return target;
	}
}
