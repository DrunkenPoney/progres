package progres.tp4.api.dominospizzaapi.bo.embeddable;

import org.jetbrains.annotations.NotNull;
import progres.tp4.api.dominospizzaapi.bo.IValidated;
import progres.tp4.api.dominospizzaapi.bo.PizzaSizeBo;
import progres.tp4.api.dominospizzaapi.bo.PizzaStateBo;
import progres.tp4.api.dominospizzaapi.bo.PizzaTypeBo;
import progres.tp4.api.dominospizzaapi.errors.RequestValidationException;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
@SuppressWarnings("unused")
public class Pizza implements IValidated {
	
	@ManyToOne
	@JoinColumn(name = "id_pty", nullable = false)
	private PizzaTypeBo type;
	
	@ManyToOne
	@JoinColumn(name = "id_psi", nullable = false)
	private PizzaSizeBo size;
	
	@ManyToOne
	@JoinColumn(name = "id_pst", nullable = false)
//	@ColumnDefault("") // TODO << WAITING
	private PizzaStateBo state;
	
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
	
	public double getPrice() {
		return getType().getBasePrice() * getSize().getPriceMod();
	}
	
	public PizzaStateBo getState() {
		return state;
	}
	
	public void setState(@NotNull PizzaStateBo state) {
		this.state = state;
	}
	
	@Override
	public void validate() throws RequestValidationException {
		// TODO validate => id not null => type, size, state
	}
	
	// TODO getCost
}
