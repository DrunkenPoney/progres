package progres.tp4.api.dominospizzaapi.bo_old.pizza;

@SuppressWarnings("unused")
public enum PizzaState {
	WAITING("En attente"),
	COOKING("En cuisson"),
	READY("Prête"),
	DELIVERING("En livraison"),
	DELIVERED("Livrée"),
	CANCELED("Annulée");
	
	private final String label;
	
	PizzaState(String label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		return this.label;
	}
}
