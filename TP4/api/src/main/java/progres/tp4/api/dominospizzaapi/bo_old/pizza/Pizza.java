package progres.tp4.api.dominospizzaapi.bo_old.pizza;

import org.jetbrains.annotations.NotNull;

public class Pizza {
	private PizzaSize size;
	private PizzaType type;
	private PizzaState state;
	
	public Pizza(@NotNull PizzaSize size, @NotNull PizzaType type) {
		this.state = PizzaState.WAITING;
		this.size = size;
		this.type = type;
	}
	
	public PizzaSize getSize() {
		return size;
	}
	
	public void setSize(PizzaSize size) {
		this.size = size;
	}
	
	public PizzaType getType() {
		return type;
	}
	
	public void setType(PizzaType type) {
		this.type = type;
	}
	
	public PizzaState getState() {
		return state;
	}
	
	public void setState(PizzaState state) {
		this.state = state;
	}
	
	public double getPrice() {
		return getType().getPriceMod() * getSize().getBasePrice();
	}
	
	public double getCost() {
		return getType().calcCost(getSize());
	}
}
