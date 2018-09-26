package tp2.model.db;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.SocketAddress;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static tp2.Utils.isAny;

@SuppressWarnings("unused")
@BsonDiscriminator
public class ClientModel {
	@BsonId
	private final ObjectId            id;
	private       StringProperty      nameProperty;
	private       SocketAddress       address;
	private       Map<String, Object> attributes;
	
	public ClientModel(@NotNull String name, @NotNull SocketAddress address) throws InvalidClientAttributeException {
		this(ObjectId.get());
		if (!setName(name))
			throw new InvalidClientAttributeException("name");
		setAddress(address);
	}
	
	private ClientModel(@NotNull ObjectId id) {
		this.id = id;
	}
	
	@BsonCreator
	private ClientModel(@BsonProperty("_id") @NotNull ObjectId id,
	                    @BsonProperty("name") @NotNull String name,
	                    @BsonProperty("address") @NotNull SocketAddress address,
	                    @BsonProperty("attributes") @NotNull Map<String, Object> attributes) {
		this.id = id;
		this.attributes = attributes;
		this.address = address;
		this.nameProperty = new SimpleStringProperty(name);
	}
	
	@SuppressWarnings("unchecked")
	public static ClientModel fromDocument(@NotNull Document doc) throws InvalidClientDocumentException {
		ObjectId id = doc.getObjectId("_id");
		if (id == null) throw new InvalidClientDocumentException();
		ClientModel client = new ClientModel(id);
		
		if (!client.setName(doc.getString("name")))
			throw new InvalidClientDocumentException(new Exception("L'attribute «name» est obligatoire"));
		
		client.attributes = doc.entrySet()
				.stream()
				.filter(entry -> !isAny(entry.getKey(), "_id", "name", "address", "attributes"))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		client.attributes.putAll((Map<String, Object>) doc.get("attributes"));
		
		return client;
	}
	
	
	static ClientModel parseDocument(Document doc) {
		if (doc == null) return null;
		try {
			return fromDocument(doc);
		} catch (InvalidClientDocumentException e) {
			throw new RuntimeException(e);
		}
	}
	
	public ObjectId getObjectId() {
		return id;
	}
	
	public String getId() {
		return id.toHexString();
	}
	
	@NotNull
	public Document toDocument() {
		final Document doc = new Document("_id", getId());
		doc.append("name", getName());
		doc.append("address", getAddress());
		doc.append("attributes", getAttributes());
//		attributes.entrySet()
//				.stream()
//				.filter(entry -> !isAny(entry.getKey(), "_id", "name", "address"))
//				.forEach(entry -> doc.append(entry.getKey(), entry.getValue()));
		return doc;
	}
	
	public StringProperty nameProperty() {
		return nameProperty;
	}
	
	public String getName() {
		return nameProperty.get();
	}
	
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public boolean setName(@NotNull String name) {
		if (isNotBlank(name)) return false;
		this.nameProperty.set(name);
		return true;
	}
	
	public SocketAddress getAddress() {
		return address;
	}
	
	public void setAddress(@NotNull SocketAddress address) {
		this.address = address;
	}
	
	public Map<String, Object> getAttributes() {
		return Collections.unmodifiableMap(attributes);
	}
	
	@Nullable
	public Object get(@NotNull String key) {
		return attributes.get(key);
	}
	
	@Nullable
	public Object set(@NotNull String key, @Nullable Object value) {
		if (value == null)
			return attributes.remove(key);
		return attributes.put(key, value);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		
		if (obj instanceof Document) {
			try {
				obj = fromDocument((Document) obj);
			} catch (InvalidClientDocumentException ignored) {
			}
		}
		
		return obj instanceof ClientModel && ((ClientModel) obj).getId().equals(getId());
	}
	
	public static class InvalidClientAttributeException extends Exception {
		private InvalidClientAttributeException(String attrName) {
			super(String.format("L'attribut «%s» du client est invalide.", attrName));
		}
	}
	
	public static class InvalidClientDocumentException extends Exception {
		private static final String ERROR_MESSAGE = "Le document spécifié ne représente pas un client valide";
		
		protected InvalidClientDocumentException() {
			super(ERROR_MESSAGE);
		}
		
		protected InvalidClientDocumentException(Throwable cause) {
			super(ERROR_MESSAGE, cause);
		}
	}
	
}
