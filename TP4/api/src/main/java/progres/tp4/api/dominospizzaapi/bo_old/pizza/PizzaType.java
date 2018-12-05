package progres.tp4.api.dominospizzaapi.bo_old.pizza;

import org.jetbrains.annotations.NotNull;
import progres.tp4.api.dominospizzaapi.bo_old.pizza.ingredients.Ingredients;
import progres.tp4.api.dominospizzaapi.bo_old.pizza.ingredients.Volume;
import progres.tp4.api.dominospizzaapi.bo_old.pizza.ingredients.VolumeUnit;

import java.util.HashMap;
import java.util.Map;

import static progres.tp4.api.dominospizzaapi.bo_old.pizza.ingredients.Ingredients.calcVolume;
import static progres.tp4.api.dominospizzaapi.bo_old.pizza.ingredients.VolumeUnit.*;


@SuppressWarnings("unused")
public enum PizzaType {
	CHEESE("Fromage", 0.7, new PizzaIngredients()
		                       .add(Ingredients.TOMATO_SAUCE, FLUID_OUNCE_UK, 8)
		                       .add(Ingredients.CHEESE, CUP, 1 / 3)),
	PEPPERONI("Peppéroni", 0.85, new PizzaIngredients()
		                             .add(Ingredients.PEPPERONI, CUP, 1 / 3)
		                             .add(Ingredients.TOMATO_SAUCE, FLUID_OUNCE_UK, 8)
		                             .add(Ingredients.CHEESE, CUP, 0.25)),
	ALL_DRESSED("Garnie", 1, new PizzaIngredients()
		                         .add(Ingredients.CHEESE, CUP, 0.25)
		                         .add(Ingredients.PEPPERONI, CUP, 1 / 3)
		                         .add(Ingredients.TOMATO_SAUCE, FLUID_OUNCE_UK, 8)
		                         .add(Ingredients.GREEN_PEPPER, CUP, 2)
		                         .add(Ingredients.MUSHROOMS, CUP, 1)),
	BACON("Bacon", 0.9, new PizzaIngredients()
		                    .add(Ingredients.PEPPERONI, CUP, 1 / 3)
		                    .add(Ingredients.CHEESE, CUP, 0.25)
		                    .add(Ingredients.TOMATO_SAUCE, FLUID_OUNCE_UK, 8)
		                    .add(Ingredients.BACON, CUP, 1));
	
	private final PizzaIngredients ingredients;
	private final String           title;
	private final double           priceMod;
	
	
	PizzaType(@NotNull String title, double priceMod, PizzaIngredients ingredients) {
		this.title = title;
		this.priceMod = priceMod;
		// See https://www.thespruceeats.com/12-inch-pizza-crust-427787#section--ingredients_1-0
		this.ingredients = ingredients.add(Ingredients.YEAST, TEASPOON, 2.5)
		                              .add(Ingredients.WATER, CUP, 0.5)
		                              .add(Ingredients.SUGAR, TEASPOON, 0.5)
		                              .add(Ingredients.SALT, TEASPOON, 0.5)
		                              .add(Ingredients.OLIVE_OIL, TABLESPOON, 1)
		                              .add(Ingredients.FLOUR, CUP, 1.5);
	}
	
	public Map<Ingredients, Volume> getIngredients(@NotNull PizzaSize size) {
		return this.ingredients.getIngredients(size);
	}
	
	public double calcCost(@NotNull PizzaSize size) {
		return getIngredients(size)
			       .entrySet()
			       .stream()
			       .mapToDouble(entry -> entry.getKey()
			                                  .getIngredient()
			                                  .calcCost(entry.getValue()))
			       .sum();
	}
	
	public double getPriceMod() {
		return priceMod;
	}
	
	public String getTitle() {
		return title;
	}
	
	private static class PizzaIngredients {
		private final Map<Ingredients, Volume> ingredients;
		
		PizzaIngredients() {
			this.ingredients = new HashMap<>();
		}
		
		public PizzaIngredients add(@NotNull Ingredients ingredient, @NotNull Volume volume) {
			if (this.ingredients.containsKey(ingredient))
				this.ingredients.get(ingredient).add(volume);
			else this.ingredients.put(ingredient, volume);
			return this;
		}
		
		public PizzaIngredients add(@NotNull Ingredients ingredient, @NotNull VolumeUnit unit, double volume) {
			return this.add(ingredient, new Volume(unit, volume));
		}
		
		public Map<Ingredients, Volume> getIngredients(@NotNull PizzaSize size) {
			Map<Ingredients, Volume> ing = new HashMap<>();
			for (Map.Entry<Ingredients, Volume> entry : this.ingredients.entrySet())
				ing.put(entry.getKey(), calcVolume(size, entry.getValue()));
			return ing;
		}
	}
}
