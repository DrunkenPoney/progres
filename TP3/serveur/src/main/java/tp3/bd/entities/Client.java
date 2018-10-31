package tp3.bd.entities;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import org.apache.commons.validator.routines.EmailValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.annotations.*;
import tp3.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import static java.lang.String.join;
import static org.apache.commons.lang3.StringUtils.isBlank;

@Entity("clients")
@SuppressWarnings("unused")
public class Client extends BaseEntity {
	
	private String firstName, lastName;
	
	@Indexed(options = @IndexOptions(unique = true))
	private String      email;
	
	@Serialized
	private PhoneNumber tel;
	
	@Reference
	private List<Reservation> reservations;
	
	public Client(String firstName, String lastName, String email) {
		this(firstName, lastName, email, null);
	}
	
	public Client(String firstName, String lastName, String email, @Nullable PhoneNumber tel) {
		this();
		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
		setTel(tel);
	}
	
	protected Client() {
		super();
		this.reservations = new ArrayList<>();
		this.firstName = null;
		this.lastName = null;
		this.email = null;
		this.tel = null;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public boolean setFirstName(@NotNull String firstName) {
		if (isBlank(firstName)) return false;
		this.firstName = firstName;
		return true;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public boolean setLastName(@NotNull String lastName) {
		if (isBlank(lastName)) return false;
		this.lastName = lastName;
		return true;
	}
	
	public String getEmail() {
		return email;
	}
	
	public boolean setEmail(@NotNull String email) {
		if (!EmailValidator.getInstance().isValid(email)) return false;
		this.email = email;
		return true;
	}
	
	public PhoneNumber getTel() {
		return tel;
	}
	
	public void setTel(@NotNull String tel) throws NumberParseException {
		PhoneNumberUtil.getInstance().parse(tel, Constants.DEFAULT_REGION_CODE, this.tel);
	}
	
	public void setTel(PhoneNumber tel) {
		this.tel = tel;
	}
	
	public String getFullName() {
		return join(" ", getFirstName(), getLastName()); 
	}
	
	public List<Reservation> getReservations() {
		return reservations;
	}
}
