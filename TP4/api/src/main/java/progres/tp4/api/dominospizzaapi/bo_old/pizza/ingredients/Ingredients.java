package progres.tp4.api.dominospizzaapi.bo_old.pizza.ingredients;

import org.jetbrains.annotations.NotNull;
import progres.tp4.api.dominospizzaapi.bo_old.pizza.PizzaSize;

import java.time.Instant;

import static java.util.Objects.requireNonNull;
import static progres.tp4.api.dominospizzaapi.bo_old.pizza.PizzaSize.DEFAULT_SIZE;

/**
 * INGREDIENTS
 * Sources:
 *
 * @see "https://www.kingarthurflour.com/learn/ingredient-weight-chart.html"
 * @see "https://www.aqua-calc.com/calculate/food-volume-to-weight"
 * @see "https://www.google.ca/search?tbm=shop"
 * @see "https://www.loblaws.ca/Food/Fruits-%26-Vegetables/Vegetable/"
 */
@SuppressWarnings("unused")
public enum Ingredients {
	TOMATO_SAUCE("Sauce tomate", 1.39, 213, VolumeUnit.MILLILITER),
	// 1 Flour Cup = 120g // 2500g / 120g = ?? flour cups
	FLOUR("Farine", 4.99, 2500 / 120, VolumeUnit.CUP),
	// 2.25 yeast tsp = 7g // 7.95 $/lb // 1 lb = 453.592g // 453.592 / (7 / 2.25) = ?? tsp
	YEAST("Levure", 7.95, 453.592 / (7 / 2.25), VolumeUnit.TEASPOON),
	OLIVE_OIL("Huile d'olive", 20.97, 3, VolumeUnit.LITER),
	// 1 sugar cup = 198g // 2000g / 198g = ?? cups
	SUGAR("Sucre", 3.42, 2000 / 198, VolumeUnit.CUP),
	// 1 cup = 245g // 1.76 $/kg // 1000g / 245g = ?? cup
	TOMATOES("Tomates", 1.76, 1000 / 245, VolumeUnit.CUP),
	// 0.25 salt cup = 65g // 453.592g / (65g / 0.25 cup) = ?? cup
	SALT("Sel", 3.96, 453.592 / (65 / 0.25), VolumeUnit.CUP),
	WATER("Eau", 0, Double.MAX_VALUE, VolumeUnit.DROP),
	// 4oz = 1 cup
	CHEESE("Fromage", 9.25, 1, VolumeUnit.CUP),
	// 1 cup = 258g // 800g / 258g = ?? cups
	PEPPERONI("Pepperoni", 10.15, 800 / 258, VolumeUnit.CUP),
	// 1 cup = 142g // 6.56$/kg // 1000 / 142 = ?? cups 
	GREEN_PEPPER("Poivron vert", 6.56, 1000 / 142, VolumeUnit.CUP),
	// 1 cup = 206.4g // 375g / 206.4g = ?? cups
	BACON("Bacon", 6.99, 375 / 206.4, VolumeUnit.CUP),
	// 1 cup = 142 grams // 10 lb * 453.592g / 142g = ?? cups 
	ONIONS("Oignons", 6.99, 10 * 453.592 / 142, VolumeUnit.CUP),
	OLIVES("Olives", 4.95, 250, VolumeUnit.MILLILITER),
	// 1 cup = 70g // 320g / 70g = ?? cup
	MUSHROOMS("Champignons", 4.99, 320 / 70, VolumeUnit.CUP),
	// 1 cup = 110.4g // 500g / 110.4 = ?? cups
	BLACK_PEPPER("Poivre", 29.99, 500 / 110.4, VolumeUnit.CUP);
	
	public static final VolumeUnit BASE_VOLUME_UNIT = VolumeUnit.CUP;
	
	private final IReadOnlyIngredient        ingredient;
	
	Ingredients(String name, double baseCost, double baseVolume, VolumeUnit unit) {
		this.ingredient = new Ingredient(name, baseCost, baseVolume, unit);
	}
	
	public IReadOnlyIngredient getIngredient() {
		return this.ingredient;
	}
	
	public static Volume calcVolume(@NotNull PizzaSize size, @NotNull Volume volume) {
		return new Volume(BASE_VOLUME_UNIT,
			size.getInches() / DEFAULT_SIZE.getInches() * volume.getVolume(BASE_VOLUME_UNIT));
	}
	
	private static class IngredientUsage implements Comparable<IngredientUsage> {
		private final Instant date;
		private final double  vol;
		
		private IngredientUsage(double vol) {
			this.date = Instant.now();
			this.vol = vol;
		}
		
		public double getVolume() {
			return vol;
		}
		
		public Instant getDate() {
			return date;
		}
		
		@Override
		public int compareTo(@NotNull IngredientUsage o) {
			return this.date.compareTo(o.date);
		}
	}
}
