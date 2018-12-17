package progres.tp4.api.dominospizzaapi.bo;

import com.fasterxml.jackson.annotation.JsonRootName;
import org.jetbrains.annotations.NotNull;
import progres.tp4.api.dominospizzaapi.bo.embeddable.Volume;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trim;
import static progres.tp4.api.dominospizzaapi.util.Constants.ENTITY_KEY_PATTERN;
import static progres.tp4.api.dominospizzaapi.util.Messages.*;
import static progres.tp4.api.dominospizzaapi.util.Utils.msgRequiredAttr;
import static progres.tp4.api.dominospizzaapi.util.Utils.normalizeSpaces;

@Entity
@Table(name = "ingredient_ing")
@JsonRootName("ingredient")
@SuppressWarnings("unused")
public class IngredientBo implements IBaseBo {
	
	@Id
	@NotBlank(message = MSG_BLANK_KEY)
	@Pattern(regexp = ENTITY_KEY_PATTERN, message = MSG_INVALID_KEY)
	@Column(name = "key_ing", nullable = false, unique = true, updatable = false)
	private String key;
	
	@NotBlank(message = MSG_BLANK_TITLE)
	@Column(name = "title", nullable = false)
	private String title;
	
	@Min(value = 0, message = MSG_COST_MIN_ZERO)
	@Column(name = "cost", nullable = false, precision = 2)
	private double cost;
	
	@Embedded
	private Volume volume;
	
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
	
	public double getCost() {
		return cost;
	}
	
	public void setCost(double cost) {
		this.cost = cost;
	}
	
	public Volume getVolume() {
		return volume;
	}
	
	public void setVolume(@NotNull Volume volume) {
		this.volume = volume;
	}
	
	@Override
	public void validate() throws RequestValidationException {
		setKey(trim(normalizeSpaces(getKey())));
		setTitle(trim(normalizeSpaces(getTitle())));
		
		if (isBlank(getKey()))
			throw new RequestValidationException(msgRequiredAttr("key"));
		if (isBlank(getTitle()))
			throw new RequestValidationException(msgRequiredAttr("title"));
		if (getVolume() == null)
			throw new RequestValidationException(msgRequiredAttr("volume"));
		getVolume().validate();
	}
}
