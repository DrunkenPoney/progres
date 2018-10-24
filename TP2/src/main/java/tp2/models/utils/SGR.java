package tp2.models.utils;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

import static java.lang.Math.round;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.ArrayUtils.*;
import static org.apache.commons.lang3.StringUtils.join;

/**
 * >>>>> SGR (Select/Set Graphic Rendition) <<<<<
 * SGR sets display attributes. Several attributes can be set in the same sequence, separated by semicolons.
 * Each display attribute remains in effect until a following occurrence of SGR resets it.
 * If no codes are given, ESC[m is treated as ESC[0m (reset/normal)
 *
 * @author Elie Grenon (DrunkenPoney)
 * @version 30/09/2018
 */
@SuppressWarnings("unused")
public enum SGR {
	RESET(),
	BOLD(1),
	FAINT(2),
	ITALIC(3),
	UDERLINE(4),
	BLINK1(5),
	BLINK2(6),
	REVERSE_VIDEO(7),
	CONCEAL(8),
	CROSSED_OUT(9),
	DBL_UNDERLINE(21),
	RESET_BOLD(22),
	RESET_ITALIC(23),
	RESET_UNDERLINE(24),
	RESET_BLINK(25),
	RESET_REVERSE_VIDEO(27),
	RESET_CONCEAL(28), REVEAL(28),
	RESET_CROSSED_OUT(29),
	ENCIRCLE(52),
	OVERLINE(53),
	RESET_ENCIRCLE(54),
	RESET_OVERLINE(55),
	// Foreground
	FG_BLACK(30),
	FG_RED(31),
	FG_GREEN(32),
	FG_YELLOW(33),
	FG_BLUE(34),
	FG_MAGENTA(35),
	FG_CYAN(36),
	FG_WHITE(37),
	RESET_FOREGROUND(39),
	// Background
	BG_BLACK(40),
	BG_RED(41),
	BG_GREEN(42),
	BG_YELLOW(43),
	BG_BLUE(44),
	BG_MAGENTA(45),
	BG_CYAN(46),
	BG_WHITE(47),
	RESET_BACKGROUND(49),
	// Bright foreground
	FG_BRIGHT_BLACK(90),
	FG_BRIGHT_RED(91),
	FG_BRIGHT_GREEN(92),
	FG_BRIGHT_YELLOW(93),
	FG_BRIGHT_BLUE(94),
	FG_BRIGHT_MAGENTA(95),
	FG_BRIGHT_CYAN(96),
	FG_BRIGHT_WHITE(97),
	// Bright background
	BG_BRIGHT_BLACK(100),
	BG_BRIGHT_RED(101),
	BG_BRIGHT_GREEN(102),
	BG_BRIGHT_YELLOW(103),
	BG_BRIGHT_BLUE(104),
	BG_BRIGHT_MAGENTA(105),
	BG_BRIGHT_CYAN(106),
	BG_BRIGHT_WHITE(107);
	
	public static final  int[]  C_8BIT_BASE_FG_PARAMS  = new int[]{38, 5};
	public static final  int[]  C_8BIT_BASE_BG_PARAMS  = new int[]{48, 5};
	public static final  int[]  C_24BIT_BASE_FG_PARAMS = new int[]{38, 2};
	public static final  int[]  C_24BIT_BASE_BG_PARAMS = new int[]{48, 2};
	private static final String ESC                    = "\u001b[";
	private static final String DELIM                  = ";";
	private static final char   END_CHAR               = 'm';
	private final        int[]  params;
	
	SGR(int... params) {
		this.params = params;
	}
	
	public static String concat(@NotNull SGR... styles) {
		return ESC +
		       Stream.of(styles)
		             .map(SGR::params)
		             .map(params -> join(params, DELIM))
		             .collect(joining(DELIM))
		       + END_CHAR;
	}
	
	/**
	 * It's preferable to use {@link SGR#toAnsi256(int, int, int, boolean)} for rgb colors since
	 * 24-bit RGB is not as widely supported as 8-bit color is.
	 *
	 * @param red        Red tint (0 to 255)
	 * @param green      Green tint (0 to 255)
	 * @param blue       Blue tint (0 to 255)
	 * @param background Is background color or not
	 * @return ANSI 8-bit color escape sequence
	 */
	public static String RGB(int red, int green, int blue, boolean background) {
		return ESC + join(toObject(addAll(background ? C_24BIT_BASE_BG_PARAMS
		                                             : C_24BIT_BASE_FG_PARAMS, red, green, blue)), DELIM)
		       + END_CHAR;
	}
	
	/**
	 * Applies RGB color to foreground.
	 *
	 * @param red   Red tint (0 to 255)
	 * @param green Green tint (0 to 255)
	 * @param blue  Blue tint (0 to 255)
	 * @return ANSI 8-bit color escape sequence
	 * @see #RGB(int, int, int, boolean)
	 */
	public static String RGB(int red, int green, int blue) {
		return RGB(red, green, blue, false);
	}
	
	/**
	 * Has more chance to be supported than real RGB but
	 * has only 216 colors + 16 base colors + 24 grey tints.
	 * <p>
	 * Source: {@link "https://stackoverflow.com/a/26665998/5647659"|Stackoverflow}
	 *
	 * @param red        Red tint (0 to 255)
	 * @param green      Green tint (0 to 255)
	 * @param blue       Blue tint (0 to 255)
	 * @param background Is background color or not
	 * @return ANSI 8-bit color escape sequence
	 */
	public static String toAnsi256(int red, int green, int blue, boolean background) {
		int ansi256;
		// we use the extended greyscale palette here, with the exception of
		// black and white. normal palette only has 4 greyscale shades.
		if (red == green && green == blue) {
			if (red < 8) ansi256 = 16;
			else if (red > 248) ansi256 = 231;
			else ansi256 = round(((((float) red) - 8) / 247) * 24) + 232;
		} else
			ansi256 = 16
			          + (36 * round((float) red / 255 * 5))
			          + (6 * round((float) green / 255 * 5))
			          + round((float) blue / 255 * 5);
		
		return ESC + join(toObject(add(background ? C_8BIT_BASE_BG_PARAMS
		                                          : C_8BIT_BASE_FG_PARAMS, ansi256)), DELIM)
		       + END_CHAR;
	}
	
	/**
	 * Applies RGB color to foreground.
	 *
	 * @param red   Red tint (0 to 255)
	 * @param green Green tint (0 to 255)
	 * @param blue  Blue tint (0 to 255)
	 * @return ANSI 8-bit color escape sequence
	 * @see #toAnsi256(int, int, int, boolean)
	 */
	public static String toAnsi256(int red, int green, int blue) {
		return toAnsi256(red, green, blue, false);
	}
	
	/**
	 * Wraps the specified text with the current SGR escape sequence and the reset escape sequence.
	 *
	 * @param txt Text to wrap
	 * @return The wrapped text
	 */
	public String wrap(Object txt) {
		return wrap(txt, RESET);
	}
	
	/**
	 * Wraps the specified text with the current SGR escape sequence and the specified one ({@code endSGR}).
	 *
	 * @param txt Text to wrap
	 * @return The wrapped text
	 */
	public String wrap(Object txt, SGR endSGR) {
		return escape() + txt + endSGR.escape();
	}
	
	/**
	 * Returns the ANSI escape sequence.
	 *
	 * @return ANSI escape sequence
	 */
	public String escape() {
		return ESC + join(toObject(params), DELIM) + END_CHAR;
	}
	
	/**
	 * Returns the SGR escape sequence parameters.
	 *
	 * @return An array of params (empty if RESET)
	 */
	public final int[] params() {
		return params;
	}
	
}
