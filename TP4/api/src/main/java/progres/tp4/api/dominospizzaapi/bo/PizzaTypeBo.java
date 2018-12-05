package progres.tp4.api.dominospizzaapi.bo;

import org.jetbrains.annotations.NotNull;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.trim;
import static progres.tp4.api.dominospizzaapi.util.Utils.msgRequiredAttr;
import static progres.tp4.api.dominospizzaapi.util.Utils.normalizeSpaces;

@Entity
@Table(name = "pizza_type_pty")
public class PizzaTypeBo extends BaseBo {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id_pty", nullable = false)
	private Long id;
	
	@NotBlank
	@Column(name = "title", nullable = false)
	private String title;
	
	@OneToMany
	@JoinTable(
		name = "pty_ing_pti",
		joinColumns = {
			@JoinColumn(name = "id_pti")
		},
		inverseJoinColumns = {
			@JoinColumn(name = "id_pty")
		}
	)
	private Set<PizzaIngredientBo> ingredients;
	
	@Column(name = "base_price", nullable = false, precision = 2)
	private double basePrice;
	
	@Override
	public Long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
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
	
	public double getBasePrice() {
		return basePrice;
	}
	
	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}
	
	@Override
	public void validate() throws RequestValidationException {
		setTitle(trim(normalizeSpaces(getTitle())));
		
		if (getIngredients() == null) setIngredients(new HashSet<>());
		// TODO validate ingredient => id not null
		
		if (isBlank(getTitle()))
			throw new RequestValidationException(msgRequiredAttr("title"));
	}
}
