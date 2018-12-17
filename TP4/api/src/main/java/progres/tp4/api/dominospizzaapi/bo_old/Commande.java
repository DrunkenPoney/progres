package progres.tp4.api.dominospizzaapi.bo_old;


import org.jetbrains.annotations.NotNull;
import progres.tp4.api.dominospizzaapi.bo_old.pizza.Pizza;

import static org.apache.commons.lang3.RandomUtils.nextLong;

@SuppressWarnings("unused")
public class Commande {
	private long id;
	private Pizza pizza;
	private Client client;
	
	public Commande(@NotNull Pizza pizza, @NotNull Client client) {
		this.id = nextLong();
		this.pizza = pizza;
		this.client = client;
	}
	
	public long getId() {
		return id;
	}
	
	public Pizza getPizza() {
		return pizza;
	}
	
	public void setPizza(@NotNull Pizza pizza) {
		this.pizza = pizza;
	}
	
	public Client getClient() {
		return client;
	}
	
	public void setClient(@NotNull Client client) {
		this.client = client;
	}
}
