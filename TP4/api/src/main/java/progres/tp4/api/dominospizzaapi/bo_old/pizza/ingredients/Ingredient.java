package progres.tp4.api.dominospizzaapi.bo_old.pizza.ingredients;

import static progres.tp4.api.dominospizzaapi.bo_old.pizza.ingredients.VolumeUnit.convert;

class Ingredient implements IReadOnlyIngredient {
	private final String     name;
	private final double     baseCost;
	private final VolumeUnit unit;
	private final double     baseVolume;
	
	Ingredient(String name, double baseCost, double baseVolume, VolumeUnit unit) {
		this.name = name;
		this.baseCost = baseCost;
		this.unit = unit;
		this.baseVolume = baseVolume;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public double getVolume(VolumeUnit unit) {
		return convert(baseVolume, this.unit, unit);
	}
	
	@Override
	public double getBaseCost() {
		return this.baseCost;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Ingredient && ((Ingredient) obj).name.equalsIgnoreCase(this.name);
	}
}
