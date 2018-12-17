package progres.tp4.api.dominospizzaapi.bo.embeddable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.hibernate.annotations.ColumnDefault;
import org.jetbrains.annotations.NotNull;
import progres.tp4.api.dominospizzaapi.bo.*;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;

import javax.persistence.*;
import javax.validation.constraints.Min;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.*;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static progres.tp4.api.dominospizzaapi.util.Utils.msgRequiredAttr;

@Embeddable
@JsonRootName("pizza")
@SuppressWarnings("unused")
public class Pizza implements IBaseBo {
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "key_pty", nullable = false)
	private PizzaTypeBo type;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "key_psi", nullable = false)
	private PizzaSizeBo size;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "key_pst", nullable = false)
	private PizzaStateBo state;
	
	@Min(1)
	@Column(name = "quantity", nullable = false)
	private int quantity;
	
	public PizzaTypeBo getType() {
		return type;
	}
	
	public void setType(@NotNull PizzaTypeBo type) {
		this.type = type;
	}
	
	public PizzaSizeBo getSize() {
		return size;
	}
	
	public void setSize(@NotNull PizzaSizeBo size) {
		this.size = size;
	}
	
	public PizzaStateBo getState() {
		return state;
	}
	
	public void setState(@NotNull PizzaStateBo state) {
		this.state = state;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public void validate() throws RequestValidationException {
		if (getType() == null)
			throw new RequestValidationException(msgRequiredAttr("type", "pizza"));
		if (getSize() == null)
			throw new RequestValidationException(msgRequiredAttr("size", "pizza"));
		if (getState() == null)
			throw new RequestValidationException(msgRequiredAttr("state", "pizza"));
		
		if (isBlank(getType().getKey()))
			throw new RequestValidationException(msgRequiredAttr("key", "type"));
		if (isBlank(getSize().getKey()))
			throw new RequestValidationException(msgRequiredAttr("key", "size"));
		if (isBlank(getState().getKey()))
			throw new RequestValidationException(msgRequiredAttr("key", "state"));
	}
	
	@JsonProperty(value = "cost", access = READ_ONLY)
	public double getCost() {
		return getType().getIngredients()
		                .stream()
			.mapToDouble(PizzaIngredientBo::getCost)
			.sum();
	}
	// TODO getCost
}
