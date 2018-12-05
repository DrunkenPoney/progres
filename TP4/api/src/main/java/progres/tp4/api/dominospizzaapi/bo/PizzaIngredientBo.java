package progres.tp4.api.dominospizzaapi.bo;

import org.jetbrains.annotations.NotNull;
import progres.tp4.api.dominospizzaapi.bo.embeddable.Volume;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;

import javax.persistence.*;

import static progres.tp4.api.dominospizzaapi.util.Utils.msgRequiredAttr;

@Entity
@Table(name = "pty_ing_pti",
       uniqueConstraints = @UniqueConstraint(
	       name = "uk_ing_pty",
	       columnNames = {
		       "id_ing",
		       "id_pty"
	       }
       )
)
@SuppressWarnings("unused")
public class PizzaIngredientBo extends BaseBo {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "id_pti")
	private Long id;
	
	@OneToOne
	@JoinColumn(name = "id_ing", updatable = false, nullable = false)
	private IngredientBo ingredient;
	
	@OneToOne
	@JoinColumn(name = "id_pty", updatable = false, nullable = false)
	private PizzaTypeBo type;
	
	@Embedded
	private Volume volume;
	
	@Override
	public Long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public IngredientBo getIngredient() {
		return ingredient;
	}
	
	public void setIngredient(@NotNull IngredientBo ingredient) {
		this.ingredient = ingredient;
	}
	
	public PizzaTypeBo getType() {
		return type;
	}
	
	public void setType(@NotNull PizzaTypeBo type) {
		this.type = type;
	}
	
	public Volume getVolume() {
		return volume;
	}
	
	public void setVolume(@NotNull Volume volume) {
		this.volume = volume;
	}
	
	@Override
	public void validate() throws RequestValidationException {
		
		if (getIngredient() == null)
			throw new RequestValidationException(msgRequiredAttr("ingredient"));
		if (getType() == null)
			throw new RequestValidationException(msgRequiredAttr("type"));
		if (getVolume() == null)
			throw new RequestValidationException(msgRequiredAttr("volume"));
		getVolume().validate();
		
		if (getIngredient().getId() == null)
			throw new RequestValidationException(msgRequiredAttr("id", "ingredient"));
		if (getType().getId() == null)
			throw new RequestValidationException(msgRequiredAttr("id", "type"));
	}
}