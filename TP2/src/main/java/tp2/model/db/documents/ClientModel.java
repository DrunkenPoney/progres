package tp2.model.db.documents;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Validation;
import tp2.model.db.internals.BaseDocumentModel;
import tp2.model.db.internals.exceptions.InvalidAttributeException;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static tp2.model.utils.Constants.MAX_PORT_NUMBER;
import static tp2.model.utils.Constants.MIN_PORT_NUMBER;
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
		super(ObjectId.get());
		if (!setName(name))
			throw new InvalidAttributeException("name");
		setAddress(address);
	}
	
	private ClientModel() {
		super();
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
