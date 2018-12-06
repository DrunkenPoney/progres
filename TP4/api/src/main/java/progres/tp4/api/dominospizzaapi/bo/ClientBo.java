package progres.tp4.api.dominospizzaapi.bo;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import org.hibernate.validator.constraints.Length;
import org.jetbrains.annotations.NotNull;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static org.apache.commons.lang3.StringUtils.trim;
import static progres.tp4.api.dominospizzaapi.util.Constants.PHONE_PATTERN;
import static progres.tp4.api.dominospizzaapi.util.Constants.POSTAL_CODE_PATTERN;
import static progres.tp4.api.dominospizzaapi.util.Messages.*;
import static progres.tp4.api.dominospizzaapi.util.Utils.normalizeSpaces;

@Entity
@Table(name = "client_cli")
@SuppressWarnings("unused")
public class ClientBo implements IBaseBo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id_cli", nullable = false)
	private Long id;
	
	@NotBlank(message = MSG_BLANK_FIRST_NAME)
	@Column(name = "first_name", nullable = false)
	private String firstName;
	
	@NotBlank(message = MSG_BLANK_LAST_NAME)
	@Column(name = "last_name", nullable = false)
	private String lastName;
	
	@NotBlank(message = MSG_BLANK_ADDRESS)
	@Column(name = "address", nullable = false)
	private String address;
	
	@Length(min = 6, max = 6, message = MSG_INVALID_POSTAL_CODE)
	@Pattern(regexp = POSTAL_CODE_PATTERN)
	@Column(name = "postal_code", nullable = false, length = 6)
	private String postalCode;
	
	@NotBlank(message = MSG_BLANK_CITY)
	@Column(name = "city", nullable = false)
	private String city;
	
	@Pattern(regexp = PHONE_PATTERN, message = MSG_INVALID_PHONE)
	@Column(name = "phone", nullable = false, length = 15, unique = true)
	private String phone;
	
	public Long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(@NotNull String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(@NotNull String lastName) {
		this.lastName = lastName;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(@NotNull String address) {
		this.address = address;
	}
	
	public String getPostalCode() {
		return postalCode;
	}
	
	public void setPostalCode(@NotNull String postalCode) {
		this.postalCode = postalCode;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(@NotNull String city) {
		this.city = city;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(@NotNull String phone) {
		this.phone = phone;
	}
	
	@Override
	public void validate() throws RequestValidationException {
		setAddress(trim(normalizeSpaces(getAddress())));
		setFirstName(trim(normalizeSpaces(getFirstName())));
		setLastName(trim(normalizeSpaces(getLastName())));
		setCity(trim(normalizeSpaces(getCity())));
		setPostalCode(trim(normalizeSpaces(getPostalCode())));
		
		final PhoneNumberUtil phu = PhoneNumberUtil.getInstance();
		try {
			setPhone(phu.format(phu.parse(getPhone(), "CA"), PhoneNumberFormat.INTERNATIONAL));
		} catch (NumberParseException err) {
			throw new RequestValidationException(MSG_INVALID_PHONE, err);
		}
		
		// TODO isBlank validations
	}
}
