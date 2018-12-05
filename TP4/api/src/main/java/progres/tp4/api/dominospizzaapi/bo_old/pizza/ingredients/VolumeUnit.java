package progres.tp4.api.dominospizzaapi.bo_old.pizza.ingredients;

@SuppressWarnings("unused")
public enum VolumeUnit {
	CUBIC_METER("M\u00e8tre(s) cube(s)", "m\u00b3", 1),
	CUBIC_KILOMETER("Kilom\u00e8tre(s) cube(s)", "km\u00b3", 1e9),
	CUBIC_DECIMETER("D\u00e9cim\u00e8tre(s) cube(s)", "dm\u00b3", 1e-3),
	CUBIC_CENTIMETER("Centim\u00e8tre(s) cube(s)", "cm\u00b3", 1e-6),
	CUBIC_MILLIMETER("Millim\u00e8tre(s) cube(s)", "mm\u00b3", 1e-9),
	LITER("Litre(s)", "L", 1e-3),
	MILLILITER("Millilitre(s)", "mL", 1e-6),
	BARREL_US("Baril(s) US", "bbl (US)", 0.1192404712),
	GALLON_US("Gallon(s) US", "gal (US)", 3.7854118e-4),
	QUART_US("Quart(s) US", "qt (US)", 9.463529e-5),
	PINT_US("Pinte(s) US", "pt (US)", 4.731765e-5),
	CUP_US("Tasse(s) US", "t (US)", 2.365882e-5),
	FLUID_OUNCE_US("Once(s) liquide(s) US", "fl oz (US)", 2.95735e-5),
	TABLESPOON_US("Cuilli\u00e8re(s) \u00e0 table US", "c. \u00e0 table (US)", 1.47868e-5),
	DESSERTSPOON_US("Cuill\u00e8re(s) \u00e0 d\u00e9ssert US", "c. \u00e0 d\u00e9ssert (US)", 9.8578431875e-6),
	TEASPOON_US("Cuill\u00e8re(s) \u00e0 th\u00e9 US", "c. \u00e0 th\u00e9 (US)", 4.92892159375e-6),
	CUBIC_MILE("Mille(s) cube(s)", "mi\u00b3", 4168181825.4406),
	CUBIC_YARD("Verge(s) cube(s)", "vg\u00b3", 0.764554858),
	CUBIC_FOOT("Pied(s) cube(s)", "pi\u00b3", 2.83168466e-3),
	CUBIC_INCH("Pousse(s) cube(s)", "po\u00b3", 1.63871e-5),
	EXALITER("Exalitre(s)", "EL", 1e15),
	PETALITER("Petalitre(s)", "PT", 1e12),
	TERALITER("Teralitre(s)", "TL", 1e9),
	GIGALITER("Gigalitre(s)", "GL", 1e6),
	MEGALITER("Megalitre(s)", "ML", 1e3),
	KILOLITER("Kilolitre(s)", "kL", 1),
	HECTOLITER("Hectolitre(s)", "hL", 0.1),
	DEKALITER("Decalitre(s)", "daL", 0.01),
	DECILITER("Decilitre(s)", "dL", 1e-4),
	CENTILITER("Centilitre(s)", "cL", 1e-5),
	MICROLITER("Microlitre(s)", "\u00b5L", 1e-9),
	NANOLITER("Nanolitre(s)", "nL", 1e-12),
	PICOLITER("Picolitre(s)", "pL", 1e-15),
	FEMTOLITER("Femtolitre(s)", "fL", 1e-18),
	ATTOLITER("Attolitre(s)", "aL", 1e-21),
	CC("CC", "cc", 1e-6),
	DROP("Goutte(s)", "goutte(s)", 5e-8),
	BARREL_UK("Baril(s) UK", "bbl (UK)", 0.16365924),
	GALLON_UK("Gallon(s) UK", "gal (UK)", 4.54609e-3),
	QUART_UK("Quart(s) UK", "qt (UK)", 1.1365225e-3),
	PINT_UK("Pinte(s) UK", "pt (UK)", 5.682613e-4),
	CUP_UK("Tasse(s) UK", "t (UK)", 2.841306e-4),
	FLUID_OUNCE_UK("Once(s) liquide(s) UK", "fl oz (UK)", 2.84131e-5),
	TABLESPOON("Cuilli\u00e8re(s) \u00e0 table", "c. \u00e0 table", 1.5e-5),
	TABLESPOON_UK("Cuilli\u00e8re(s) \u00e0 table UK", "c. \u00e0 table (UK)", 1.77582e-5),
	DESSERTSPOON_UK("Cuill\u00e8re(s) \u00e0 d\u00e9ssert UK", "c. \u00e0 d\u00e9ssert (UK)", 1.18388e-5),
	TEASPOON_UK("Cuill\u00e8re(s) \u00e0 th\u00e9 UK", "c. \u00e0 th\u00e9 (UK)", 5.9193880208333e-6),
	CUP("Tasse(s)", "t", 2.5e-4),
	TEASPOON("Cuill\u00e8re(s) \u00e0 th\u00e9", "c. \u00e0 th\u00e9", 5e-6),
	STERE("St\u00e8re(s)", "st", 1),
	CORD("Corde(s)", "cd", 3.6245563638),
	DRAM("Drachme(s)", "dr", 3.6966911953125e-6),
	EARTH("Plan\u00e8te(s) Terre", "terre(s)", 1.083e+21);
	private final String name, unit;
	private final double mod;
	
	VolumeUnit(String name, String unit, double mod) {
		this.name = name;
		this.unit = unit;
		this.mod = mod;
	}
	
	public static double convert(double val, VolumeUnit from, VolumeUnit to) {
		return (val / from.mod) * to.mod;
	}
	
	public double getModifier() {
		return mod;
	}
	
	public String getUnit() {
		return unit;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
