package progres.tp4.api.dominospizzaapi.bo;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.Fetch;
import org.jetbrains.annotations.NotNull;
import progres.tp4.api.dominospizzaapi.bo.embeddable.Volume;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static progres.tp4.api.dominospizzaapi.util.Utils.msgRequiredAttr;

@Entity
@Table(name = "pty_ing_pti")
@SuppressWarnings("unused")
@IdClass(PizzaIngredientBo.PKPizzaIngredient.class)
public class PizzaIngredientBo implements IBaseBo {
	@Id
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "key_ing", updatable = false, nullable = false)
	private IngredientBo ingredient;
	
	@Id
	@OneToOne
	@JoinColumn(name = "key_pty", updatable = false, nullable = false)
	private PizzaTypeBo type;
	
	@Embedded
	private Volume volume;
	
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
	
	@JsonProperty(value = "cost", access = READ_ONLY)
	public double getCost() {
		return getIngredient().getCost() / getIngredient().getVolume().convertTo(getVolume().getUnit()) * getVolume().getVolume();
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
		
		if (isBlank(getIngredient().getKey()))
			throw new RequestValidationException(msgRequiredAttr("key", "ingredient"));
		if (isBlank(getType().getKey()))
			throw new RequestValidationException(msgRequiredAttr("key", "type"));
	}
	
	public static class PKPizzaIngredient implements Serializable {
		private static final long serialVersionUID = -2657027976555388891L;
		private IngredientBo ingredient;
		private PizzaTypeBo type;
		
		private PKPizzaIngredient() {}
		
		public PKPizzaIngredient(@NotNull IngredientBo ingredient, @NotNull PizzaTypeBo type) {
			this.ingredient = ingredient;
			this.type = type;
		}
		
		public IngredientBo getIngredient() {
			return ingredient;
		}
		
		public PizzaTypeBo getType() {
			return type;
		}
		
		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			
			PKPizzaIngredient that = (PKPizzaIngredient) o;
			
			if (ingredient != null ? !Objects.equals(ingredient.getKey(), that.ingredient.getKey()) : that.ingredient != null) return false;
			return type != null ? Objects.equals(type.getKey(), that.type.getKey()) : that.type == null;
		}
		
		@Override
		public int hashCode() {
			int result = ingredient != null ? ingredient.hashCode() : 0;
			result = 31 * result + ( type != null ? type.hashCode() : 0 );
			return result;
		}
	}
}