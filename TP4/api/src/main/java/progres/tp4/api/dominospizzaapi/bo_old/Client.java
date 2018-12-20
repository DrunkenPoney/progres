package progres.tp4.api.dominospizzaapi.bo_old;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class Client {
	private static final PhoneNumberUtil PHONE_NUMBER_UTIL = PhoneNumberUtil.getInstance();
	
	private String firstName, lastName, postalCode, city;
	private PhoneNumber tel;
	
	public Client(@NotNull String firstName,
	              @NotNull String lastName,
	              @NotNull String postalCode,
	              @NotNull String city,
	              @NotNull String tel) throws NumberParseException {
		this.firstName = firstName;
		this.lastName = lastName;
		this.postalCode = postalCode;
		this.city = city;
		this.tel = PHONE_NUMBER_UTIL.parse(tel, "CA");
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
	
	public PhoneNumber getTel() {
		return tel;
	}
	
	public void setTel(@NotNull String tel) throws NumberParseException {
		this.setTel(PHONE_NUMBER_UTIL.parse(tel, "CA"));
	}
	
	public void setTel(@NotNull PhoneNumber tel) {
		this.tel = tel;
	}
}
