package tp2.models.utils;

import javafx.scene.text.Font;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.validator.routines.IntegerValidator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static tp2.models.utils.SGR.*;

@SuppressWarnings("unused")
public final class Utils {
	
	public static void loadFonts() throws IOException, URISyntaxException {
		Utils.readdirDeep(toResourcePath("/"))
		     .filter(path -> path.toString().matches(".*\\.([to]tf|woff2?)"))
		     .map(path -> Utils.class.getResourceAsStream(path.toString()))
		     .forEach(rss -> Font.loadFont(rss, 12));
	}
	
	public static Path toResourcePath(String p) throws URISyntaxException, IOException {
		URI uri = Utils.class.getResource("/").toURI();
		if ("jar".equals(uri.getPath()))
			return FileSystems.newFileSystem(uri, Collections.emptyMap(), Utils.class.getClassLoader()).getPath(p);
		System.out.println(uri.getScheme());
		return new File(uri).toPath();
	}
	
	public static Stream<Path> readdirDeep(Path dir) throws IOException {
		System.out.println(dir);
		return Files.walk(dir)
		            .filter(path -> !path.equals(dir))
		            .flatMap(path -> {
			            Stream<Path> files = null;
			            if (Files.isDirectory(path)) {
				            try {
					            files = readdirDeep(path);
				            } catch (IOException e) {
					            e.printStackTrace();
				            }
			            }
			            return files == null ? Stream.of(path) : files;
		            });
	}
	
	@NotNull
	public static int[] range(final int min, final int max) {
		int[] range = new int[max(min, max) - min(min, max)];
		for (int i = 0; i < range.length; i++)
			range[i] = min < max ? min + i : min - i;
		return range;
	}
	
	@SafeVarargs
	public static <T> boolean isAny(T obj, T... list) {
		return ArrayUtils.contains(list, obj);
	}
	
	public static boolean isValidPort(Integer port) {
		return IntegerValidator.getInstance().isInRange(port, Constants.MIN_PORT_NUMBER, Constants.MAX_PORT_NUMBER);
	}
	
	@Nullable
	public static InetSocketAddress firstAvailableSocketInRange(final @Nullable InetAddress address,
	                                                            final int minPort,
	                                                            final int maxPort) {
		if (address == null) return null;
		for (int port : range(minPort, maxPort + 1)) {
			try {
				(new Socket(address, port)).close();
			} catch (IOException e) {
				return new InetSocketAddress(address, port);
			}
		}
		return null;
	}
	
	public static void describe(String title, Object obj) {
		try {
			System.out.println();
			System.out.println(FG_BRIGHT_MAGENTA.wrap(title)
			                   + FG_BRIGHT_YELLOW.wrap("\n   \u21b3 ")
			                   + BeanUtils.describe(obj)
			                              .entrySet()
			                              .stream()
			                              .map(entry -> FG_BRIGHT_GREEN.wrap(entry.getKey())
			                                            + FG_BRIGHT_YELLOW.wrap(" => ")
			                                            + FG_BRIGHT_CYAN.wrap(entry.getValue()))
			                              .collect(Collectors.joining(FG_BRIGHT_YELLOW.wrap("\n   \u21b3 "))));
			System.out.println();
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			System.err.printf("Couldn't describe %s > %s: %s",
			                  obj, e.getClass().getName(), e.getMessage());
		}
	}
	
	public static <O, R extends O> List<R> cast(Class<R> clazz, Collection<O> objs) {
		return objs.stream().map(clazz::cast).collect(Collectors.toList());
	}
	
	@SafeVarargs
	public static <O, R extends O> List<R> cast(Class<R> clazz, O... objs) {
		return cast(clazz, Arrays.asList(objs));
	}
	
	@SafeVarargs
	public static <O> boolean same(Collection<O> l1, Collection<O> l2, Collection<O>... others) {
		return (l1 == l2 || l1.equals(l2)
		        || (l1.size() == l2.size() && l1.containsAll(l2)))
		       && (others.length == 0 || same(l2, others[0], shift(others)));
	}
	
	public static <O> O[] pop(O[] arr) {
		return ArrayUtils.remove(arr, arr.length - 1);
	}
	
	public static <O> O[] shift(O[] arr) {
		return ArrayUtils.remove(arr, 0);
	}
	
}
