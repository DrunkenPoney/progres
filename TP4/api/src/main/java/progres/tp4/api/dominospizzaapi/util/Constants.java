package progres.tp4.api.dominospizzaapi.util;

public final class Constants {
	public static final String ENTITY_KEY_PATTERN  = "^[A-Z0-9_]{2,}$";
	public static final String POSTAL_CODE_PATTERN = "^([A-Z]\\d){3}$";
	public static final String PHONE_PATTERN       = "^(?=(?:\\D*.\\D*){10,15}$)\\+?\\d{1,4}?[-.\\x20]?\\(?\\d{1,3}?\\)?[-.\\x20]?\\d{1,4}[-.\\x20]?\\d{1,4}[-.\\x20]?\\d{1,9}$";
	public static final double USD_TO_CAD          = 1.32961;
}
