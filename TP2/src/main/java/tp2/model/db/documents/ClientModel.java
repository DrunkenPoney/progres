package tp2.model.db.documents;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Validation;
import tp2.model.db.internals.BaseDocumentModel;
import tp2.model.db.internals.exceptions.InvalidAttributeException;
import tp2.model.db.internals.exceptions.InvalidDocumentException;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static tp2.model.utils.Constants.MAX_PORT_NUMBER;
import static tp2.model.utils.Constants.MIN_PORT_NUMBER;
import static tp2.model.utils.Utils.isAny;
import static tp2.model.utils.Utils.isValidPort;

@SuppressWarnings("unused")
@Entity("clients")
@Validation("{ port: { $gte: " + MIN_PORT_NUMBER + ", $lte: " + MAX_PORT_NUMBER + "} }")
public class ClientModel extends BaseDocumentModel {
	public static final String OBJ_NAME = "client";
	
	private String name;
	private String address;
	private int    port;
	
	@Embedded
	private Map<String, Object> attributes;
	
	public ClientModel(@NotNull String name,
	                   @NotNull String address,
	                   int port) throws InvalidAttributeException {
		this(ObjectId.get());
		if (!setName(name))
			throw new InvalidAttributeException("name");
		setAddress(address);
	}
	
	private ClientModel(@NotNull ObjectId id) {
		super(id);
		this.address = null;
		this.name = null;
		this.attributes = new Document();
	}
	
	@SuppressWarnings("ConstantConditions")
	protected ClientModel() {
		super(null);
	}
	
	@SuppressWarnings("unchecked")
	public static ClientModel fromDocument(@NotNull Document doc) throws InvalidDocumentException {
		ObjectId id = doc.getObjectId("_id");
		if (id == null) throw new InvalidDocumentException(OBJ_NAME);
		ClientModel client = new ClientModel(id);
		
		if (!client.setName(doc.getString("name")))
			throw new InvalidDocumentException(
					OBJ_NAME, new InvalidAttributeException(OBJ_NAME, "name"));
		
		if (!client.setPort(doc.getInteger("port")))
			throw new InvalidDocumentException(
					OBJ_NAME, new InvalidAttributeException(OBJ_NAME, "port"));
		
		if (!client.setAddress(doc.getString("address")))
			throw new InvalidDocumentException(
					OBJ_NAME, new InvalidAttributeException(OBJ_NAME, "address"));
		
		Map<String, Object> attrs = doc.entrySet()
		                               .stream()
		                               .filter(entry -> !isAny(entry.getKey(), "_id", "name", "address", "port",
		                                                       "attributes"))
		                               .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		
		client.attributes = new Document(attrs);
		if (doc.get("attributes") != null)
			client.attributes.putAll((Document) doc.get("attributes"));
		
		return client;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	protected ClientModel parse(@NotNull Document doc) throws InvalidDocumentException {
		return fromDocument(doc);
	}
	
	public Document toDocument() {
		final Document doc = new Document("_id", getId());
		doc.append("name", getName());
		doc.append("address", getAddress());
		doc.append("port", getPort());
		doc.append("attributes", getAttributes());
		return doc;
	}
	
	public String getName() {
		return name;
	}
	
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	public boolean setName(@NotNull String name) {
		if (isBlank(name)) return false;
		this.name = name;
		return true;
	}
	
	public int getPort() {
		return port;
	}
	
	public boolean setPort(int port) {
		if (!isValidPort(port)) return false;
		this.port = port;
		return true;
	}
	
	public String getAddress() {
		return address;
	}
	
	public boolean setAddress(@NotNull String address) {
		if (!InetAddressValidator.getInstance().isValid(address)) return false;
		this.address = address;
		return true;
	}
	
	public InetSocketAddress getSocket() {
		return new InetSocketAddress(getAddress(), getPort());
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
	
}
