package progres.tp4.api.dominospizzaapi.bo;

import org.jetbrains.annotations.NotNull;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trim;
import static progres.tp4.api.dominospizzaapi.util.Constants.ENTITY_KEY_PATTERN;
import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_BLANK_KEY;
import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_INVALID_KEY;
import static progres.tp4.api.dominospizzaapi.util.Utils.msgRequiredAttr;
import static progres.tp4.api.dominospizzaapi.util.Utils.normalizeSpaces;

@Entity
@Table(name = "pizza_type_pty")
public class PizzaTypeBo implements IBaseBo {
	
	@Id
	@NotBlank(message = MSG_BLANK_KEY)
	@Pattern(regexp = ENTITY_KEY_PATTERN, message = MSG_INVALID_KEY)
	@Column(name = "key_pty", nullable = false, updatable = false)
	private String key;
	
	@NotBlank
	@Column(name = "title", nullable = false)
	private String title;
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinColumn(name = "pty_pti", referencedColumnName = "key_pty")
	private Set<PizzaIngredientBo> ingredients;
	
	@Min(0)
	@Column(name = "price_mod", nullable = false, precision = 2)
	private double priceMod;
	
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
	
	public Set<PizzaIngredientBo> getIngredients() {
		return ingredients;
	}
	
	public void setIngredients(@NotNull Set<PizzaIngredientBo> ingredients) {
		this.ingredients = ingredients;
	}
	
	public double getPriceMod() {
		return priceMod;
	}
	
	public void setPriceMod(double priceMod) {
		this.priceMod = priceMod;
	}
	
	@Override
	public void validate() throws RequestValidationException {
		setTitle(trim(normalizeSpaces(getTitle())));
		setKey(trim(normalizeSpaces(getKey())));
		
		if (getIngredients() == null) setIngredients(new HashSet<>());
		// TODO validate ingredient => key not blank
		
		if (isBlank(getTitle()))
			throw new RequestValidationException(msgRequiredAttr("title"));
		if (isBlank(getKey()))
			throw new RequestValidationException(msgRequiredAttr("key"));
	}
}
