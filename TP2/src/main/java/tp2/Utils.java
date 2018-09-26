package tp2;

import javafx.scene.text.Font;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.stream.Stream;

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
		return Paths.get(uri.getPath());
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
	
	public static <T> boolean isAny(T obj, T... list) {
		return ArrayUtils.contains(list, obj);
	}
}
