package progres.tp4.api.dominospizzaapi.bo_old.pizza.ingredients;

import org.jetbrains.annotations.NotNull;

import static progres.tp4.api.dominospizzaapi.bo_old.pizza.ingredients.Ingredients.BASE_VOLUME_UNIT;

public interface IReadOnlyIngredient {
	String getName();
	
	double getBaseCost();
	
	default double calcCost(@NotNull Volume volume) {
		return volume.getVolume(BASE_VOLUME_UNIT) / getVolume(BASE_VOLUME_UNIT) * getBaseCost();
	}
	
	double getVolume(VolumeUnit unit);
	
	boolean equals(Object obj);
}
