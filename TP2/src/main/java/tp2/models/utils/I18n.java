package tp2.models.utils;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.HashMap.Entry;
import static org.apache.commons.lang3.StringUtils.*;

public class I18n {
	private static final Map<String, I18n> defaults = new HashMap<>();
	
	static {
		if (System.getProperty("user.language").equalsIgnoreCase("fr"))
			Locale.setDefault(Locale.CANADA_FRENCH);
		else Locale.setDefault(Locale.CANADA);
	}
	
	private final ResourceBundle      bundle;
	private final Map<String, String> refs;
	private final String              prefix;
	
	private I18n(@NotNull ResourceBundle bundle, @Nullable String prefix) {
		this.prefix = defaultString(prefix);
		this.bundle = bundle;
		this.refs = init();
	}
	
	@NotNull
	public static I18n bundle(@NotNull String name) {
		if (!defaults.containsKey(name) || !defaults.get(name).bundle.getLocale().equals(Locale.getDefault()))
			defaults.put(name, new I18n(ResourceBundle.getBundle(name, Locale.getDefault()), null));
		return defaults.get(name);
	}
	
	@NotNull
	public static I18n messages() {
		return bundle(Constants.I18N_MESSAGES_BUNDLE_BASE_NAME);
	}
	
	@NotNull
	private Map<String, String> init() {
		return Collections.list(bundle.getKeys())
		                  .stream()
		                  .filter(key -> startsWithIgnoreCase(key, prefix))
		                  .map(key -> Map.entry(stripStart(removeStartIgnoreCase(key, prefix), "."), key))
		                  .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
	}
	
	private I18n updated() {
		return bundle.getLocale() == Locale.getDefault()
		       ? this : bundle(bundle.getBaseBundleName()).withPrefix(prefix);
	}
	
	@NotNull
	public ResourceBundle getBundle() {
		return updated().bundle;
	}
	
	@NotNull
	public I18n withPrefix(String key) {
		return new I18n(getBundle(), prefix + stripStart(key, "."));
	}
	
	@Contract("null -> null; !null -> _")
	public String get(@Nullable String key) {
		return get(key, null);
	}
	
	@Contract("null -> null; !null -> _")
	public <O> O getObject(@Nullable String key) {
		return get(key, null);
	}
	
	@SuppressWarnings("unchecked")
	@Contract("null, _ -> param2; !null, null -> _")
	public <O> O get(@Nullable String key, @Nullable O dfltValue) {
		O ret = dfltValue;
		key = key == null ? null : refs.get(stripStart(key, "."));
		try {
			if (key != null)
				ret = (O) getBundle().getObject(key);
		} catch (MissingResourceException ignored) {
		}
		return ret;
	}
	
	@NotNull
	public String getPrefix() {
		return prefix;
	}
}
