package tp2.models.db.documents;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.annotations.*;
import tp2.models.db.internals.BaseDocumentModel;
import tp2.models.db.internals.exceptions.InvalidAttributeException;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static tp2.models.utils.Utils.isValidPort;

@SuppressWarnings("unused")
@Entity("clients")
@Validation("{" +
            "  port: {" +
            "    $lte: 65535," +
            "    $gte: 0" +
            "  }," +
            "  name: {" +
            "    $type: 2" +
            "  }," +
            "  address: {" + // Source: http://jsfiddle.net/8S4nq/563/
            "    $regex: '((^\\\\s*((([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))\\\\s*$)|(^\\\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|((25[0-5]|2[0-4]\\\\d|1\\\\d\\\\d|[1-9]?\\\\d)(\\\\.(25[0-5]|2[0-4]\\\\d|1\\\\d\\\\d|[1-9]?\\\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|:((25[0-5]|2[0-4]\\\\d|1\\\\d\\\\d|[1-9]?\\\\d)(\\\\.(25[0-5]|2[0-4]\\\\d|1\\\\d\\\\d|[1-9]?\\\\d)){3})|:))|(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\\\d|1\\\\d\\\\d|[1-9]?\\\\d)(\\\\.(25[0-5]|2[0-4]\\\\d|1\\\\d\\\\d|[1-9]?\\\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\\\d|1\\\\d\\\\d|[1-9]?\\\\d)(\\\\.(25[0-5]|2[0-4]\\\\d|1\\\\d\\\\d|[1-9]?\\\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\\\d|1\\\\d\\\\d|[1-9]?\\\\d)(\\\\.(25[0-5]|2[0-4]\\\\d|1\\\\d\\\\d|[1-9]?\\\\d)){3}))|:))|(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\\\d|1\\\\d\\\\d|[1-9]?\\\\d)(\\\\.(25[0-5]|2[0-4]\\\\d|1\\\\d\\\\d|[1-9]?\\\\d)){3}))|:))|(:(((:[0-9A-Fa-f]{1,4}){1,7})|((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\\\d|1\\\\d\\\\d|[1-9]?\\\\d)(\\\\.(25[0-5]|2[0-4]\\\\d|1\\\\d\\\\d|[1-9]?\\\\d)){3}))|:)))(%.+)?\\\\s*$))|(^\\\\s*((?=.{1,255}$)(?=.*[A-Za-z].*)[0-9A-Za-z](?:(?:[0-9A-Za-z]|\\\\b-){0,61}[0-9A-Za-z])?(?:\\\\.[0-9A-Za-z](?:(?:[0-9A-Za-z]|\\\\b-){0,61}[0-9A-Za-z])?)*)\\\\s*$)'," +
            "    $options: 's'" +
            "  }" +
            "}")
@Indexes({@Index(fields = @Field("name"), options = @IndexOptions(unique = true)),
          @Index(fields = {@Field("address"), @Field("port")}, options = @IndexOptions(unique = true))})
public class ClientModel extends BaseDocumentModel<ClientModel> implements Serializable {
	public static final  String OBJ_NAME         = "client";
	private static final long   serialVersionUID = -4989469602238675790L;
	
	private String name;
	private String address;
	private int    port;
	
	@Reference
	private GroupModel group;
	
	@Embedded
	private Map<String, Object> attributes;
	
	public ClientModel(@NotNull String name,
	                   @NotNull String address,
	                   int port) throws InvalidAttributeException {
		super(ObjectId.get());
		if (!setName(name))
			throw new InvalidAttributeException(OBJ_NAME, "name");
		if (!setAddress(address))
			throw new InvalidAttributeException(OBJ_NAME, "address");
		if (!setPort(port))
			throw new InvalidAttributeException(OBJ_NAME, "port");
	}
	
	private ClientModel() {
		super();
	}
	
	public GroupModel getGroup() {
		return group;
	}
	
	public void setGroup(GroupModel group) {
		this.group = group;
	}
	
	@Override
	public boolean isExactSame(ClientModel doc1) {
		return equals(doc1);
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
	
	@NotNull
	public Map<String, Object> getAttributes() {
		if (attributes == null) attributes = new HashMap<>();
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
		return super.equals(obj)
		       || (obj instanceof ClientModel
		           && (((ClientModel) obj).getName().equals(getName())
		               || (((ClientModel) obj).getAddress().equals(getAddress())
		                   && ((ClientModel) obj).getPort() == getPort())));
	}
}
