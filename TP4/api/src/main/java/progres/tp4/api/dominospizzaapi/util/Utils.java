package progres.tp4.api.dominospizzaapi.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static java.lang.String.format;
import static org.apache.commons.lang3.RegExUtils.replaceAll;
import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_MISSING_ATTR;
import static progres.tp4.api.dominospizzaapi.util.Messages.MSG_MISSING_ENTITY_ATTR;

public final class Utils {
	
	public static String normalizeSpaces(String str) {
		return normalizeSpaces(str, false);
	}
	
	public static String normalizeSpaces(String str, boolean allowInvisibleSeperator) {
		return replaceAll(str, format("[\\x{2002}-\\x{200f}\\x{205f}-\\x{2062}%s]", allowInvisibleSeperator ? "\\x{2063}" : ""), String.valueOf('\u0020'));
	}
	
	public static String msgRequiredAttr(@NotNull String attr) {
		return msgRequiredAttr(attr, null);
	}
	
	public static String msgRequiredAttr(@NotNull String attr, @Nullable String entity) {
		return format(entity == null ? MSG_MISSING_ATTR : MSG_MISSING_ENTITY_ATTR,
		              attr, entity);
	}
}
