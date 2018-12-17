package progres.tp4.api.dominospizzaapi.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.jetbrains.annotations.NotNull;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static java.lang.Math.*;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trim;
import static progres.tp4.api.dominospizzaapi.util.Constants.ENTITY_KEY_PATTERN;
import static progres.tp4.api.dominospizzaapi.util.Constants.USD_TO_CAD;
import static progres.tp4.api.dominospizzaapi.util.Messages.*;
import static progres.tp4.api.dominospizzaapi.util.Utils.msgRequiredAttr;
import static progres.tp4.api.dominospizzaapi.util.Utils.normalizeSpaces;

@Entity
@Table(name = "pizza_size_psi")
@JsonRootName("pizzaSize")
@SuppressWarnings("unused")
public class PizzaSizeBo implements IBaseBo {
	
	@Id
	@NotBlank(message = MSG_BLANK_KEY)
	@Pattern(regexp = ENTITY_KEY_PATTERN, message = MSG_INVALID_KEY)
	@Column(name = "key_psi", nullable = false, updatable = false)
	private String key;
	
	@NotBlank(message = MSG_BLANK_TITLE)
	@Column(name = "title", nullable = false)
	private String title;
	
	@Min(0)
	@Column(name = "inches", nullable = false, unique = true)
	private int inches;
	
	public String getKey() {
		return key;
	}
	
	public void setKey(@NotNull String key) {
		this.key = key;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(@NotNull String title) {
		this.title = title;
	}
	
	public int getInches() {
		return inches;
	}
	
	public void setInches(int inches) {
		this.inches = inches;
	}
	
	@JsonProperty(value = "basePrice", access = READ_ONLY)
	public double getBasePrice() {
		double in = (double) getInches();
		return sqrt(PI * pow(in, 2) * pow(E, ( 1 / 2046 * in ) - 1.046)) * USD_TO_CAD;
	}
	
	@Override
	public void validate() throws RequestValidationException {
		setKey(trim(normalizeSpaces(getKey())));
		
		if (isBlank(getKey()))
			throw new RequestValidationException(msgRequiredAttr("key"));
	}
}
