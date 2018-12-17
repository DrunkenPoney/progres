package progres.tp4.api.dominospizzaapi.bo_old.pizza;

import static java.lang.Math.*;

@SuppressWarnings("unused")
public enum PizzaSize {
	SMALL(10), REGULAR(12), BIG(14), HUGE(16);
	public static final PizzaSize DEFAULT_SIZE = PizzaSize.REGULAR;
	public static final double    USD_TO_CAD   = 1.32961;
	private final       int       inches;
	
	PizzaSize(int inches) { this.inches = inches; }
	
	public int getInches() {
		return inches;
	}
	
	public double getBasePrice() {
		double in = (double) getInches();
		return sqrt(PI * pow(in, 2) * pow(E, (1/2046 * in) - 1.046)) * USD_TO_CAD;
	}
}
