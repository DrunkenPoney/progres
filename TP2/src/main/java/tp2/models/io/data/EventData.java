package tp2.models.io.data;

import tp2.models.db.documents.ClientModel;
import tp2.models.db.documents.GroupModel;

import java.io.Serializable;

@SuppressWarnings("unused")
public class EventData extends TransmissionData implements Serializable {
	public enum EventType {
		GROUP_LEFT, GROUP_JOIN
	}
	
	private final EventType event;
	
	public EventData(ClientModel sender, EventType event, GroupModel target) {
		super(sender, target);
		this.event = event;
	}
	
	public EventType getEvent() {
		return event;
	}
}
