package progres.tp4.api.dominospizzaapi.bo;

import com.fasterxml.jackson.annotation.JsonRootName;
import org.jetbrains.annotations.NotNull;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static org.apache.commons.lang3.StringUtils.*;
import static progres.tp4.api.dominospizzaapi.util.Constants.ENTITY_KEY_PATTERN;
import static progres.tp4.api.dominospizzaapi.util.Messages.*;
import static progres.tp4.api.dominospizzaapi.util.Utils.msgRequiredAttr;
import static progres.tp4.api.dominospizzaapi.util.Utils.normalizeSpaces;

@Entity
@Table(name = "volume_unit_vun")
@JsonRootName("volumeUnit")
@SuppressWarnings("unused")
public class VolumeUnitBo implements IBaseBo {
	
	@Id
	@NotBlank(message = MSG_BLANK_KEY)
	@Pattern(regexp = ENTITY_KEY_PATTERN, message = MSG_INVALID_KEY)
	@Column(name = "key_vun", nullable = false, updatable = false)
	private String key;
	
	@NotBlank(message = MSG_BLANK_TITLE)
	@Column(name = "title", nullable = false)
	private String title;
	
	@NotBlank(message = MSG_BLANK_UNIT)
	@Column(name = "unit", nullable = false)
	private String unit;
	
	@Min(value = 0, message = MSG_MODIFIER_MIN_ZERO)
	@Column(name = "modifier", nullable = false, precision = 65)
	private double modifier;
	
	public String getKey() {
		return key;
	}
	
	public void setKey(@NotNull String key) {
		this.key = upperCase(key);
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(@NotNull String title) {
		this.title = title;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public void setUnit(@NotNull String unit) {
		this.unit = unit;
	}
	
	public double getModifier() {
		return modifier;
	}
	
	public void setModifier(double mod) {
		this.modifier = mod;
	}
	
	@Override
	public void validate() throws RequestValidationException {
		setKey(trim(normalizeSpaces(getKey())));
		setTitle(trim(normalizeSpaces(getTitle())));
		setUnit(trim(normalizeSpaces(getUnit())));
		
		if (isBlank(getKey()))
			throw new RequestValidationException(msgRequiredAttr("key"));
		if (isBlank(getTitle()))
			throw new RequestValidationException(msgRequiredAttr("title"));
		if (isBlank(getUnit()))
			throw new RequestValidationException(msgRequiredAttr("unit"));
	}
}
