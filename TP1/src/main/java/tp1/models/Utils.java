package tp1.models;

import javafx.scene.Node;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.time.temporal.TemporalUnit;

@SuppressWarnings("unused")
public final class Utils {
    public static final String ERROR_STYLE_CLASS = "error";
    
    
    /**
     * Made by me ;)
     * $1 => Protocol
     * $2 => Domain name
     * $3 => Port
     * $4 => Path
     * $5 => File
     * $6 => Query
     * $7 => Hash
     *
     * @link "https://regex101.com/r/TtV3Sj/1"
     */
    public static final String REGEX_URL_PARTS = "^((?:https?|ftp):\\/\\/?)?([^:/\\s.]+\\.[^:/\\s]+|localhost)(:\\d+)?((?:\\/\\w+)*\\/)?([\\w\\-.]+[^#?\\s]+)?([^#]*)?(#[\\w-]*)?$";
    
    public static String byteToBinary(byte b) {
        return String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
    }
    
    public static byte[] int2byte(int[] src) {
        int    srcLength = src.length;
        byte[] dst       = new byte[srcLength << 2];
        
        for (int i = 0; i < srcLength; i++) {
            int x = src[i];
            int j = i << 2;
            dst[j++] = (byte) (x & 0xff);
            dst[j++] = (byte) ((x >>> 8) & 0xff);
            dst[j++] = (byte) ((x >>> 16) & 0xff);
            dst[j] = (byte) ((x >>> 24) & 0xff);
        }
        return dst;
    }
    
    public static Thread after(long time, TemporalUnit unit, Runnable callback) {
        return after(Duration.of(time, unit).toMillis(), callback);
    }
    
    public static Thread after(long ms, Runnable callback) {
        Thread th = new Thread(() -> {
            try {
                Thread.sleep(ms);
                callback.run();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        th.start();
        return th;
    }
    
    
    public static boolean toggleStyleClass(Node element, String styleClass) {
        return toggleStyleClass(element, styleClass, null);
    }
    
    public static boolean toggleStyleClass(Node element, String styleClass, @Nullable Boolean force) {
        if (force != null) {
            if (!force) {
                element.getStyleClass().remove(styleClass);
            } else if (!element.getStyleClass().contains(styleClass)) {
                element.getStyleClass().add(styleClass);
            }
        } else {
            if (element.getStyleClass().contains(styleClass)) {
                element.getStyleClass().remove(styleClass);
            } else { element.getStyleClass().add(styleClass); }
        }
        return element.getStyleClass().contains(styleClass);
    }
    
    
    public static String[] split(String src, int subLength) {
        String[] out = new String[(int) Math.ceil(src.length() / subLength)];
        
        for (int j = 0, i = 0; i < src.length(); i += subLength, j++) {
            int x = i + subLength;
            out[j] = src.substring(i, x >= src.length() ? src.length() - 1 : x);
        }
        
        return out;
    }
}
