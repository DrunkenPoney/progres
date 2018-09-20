package tp1.models;

import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public final class ANSI {
    public static final char ESC = '\033';
    
    public enum SGRCode {
        NORMAL(0), BOLD(1), INCREASED_INTENSITY(1), FAINT(2), DECREASED_INTENSITY(2), ITALIC(3),
        UNDERLINED(4), SLOW_BLINK(5), RAPID_BLINK(6), INVERSE(7), REVERSE_VIDEO(7), CONCEAL(8), INVISIBLE(8),
        CROSSED_OUT(9), PRIMARY_FONT(10), ALTERNATIVE_FONT_1(11), ALTERNATIVE_FONT_2(12),
        ALTERNATIVE_FONT_3(13), ALTERNATIVE_FONT_4(14), ALTERNATIVE_FONT_5(15), ALTERNATIVE_FONT_6(16),
        ALTERNATIVE_FONT_7(17), ALTERNATIVE_FONT_8(18), ALTERNATIVE_FONT_9(19), FRAKTUR(20), BOLD_OFF(21),
        INTENSITY_AND_COLOR_RESET(22), ITALIC_AND_FRAKTUR_OFF(23), UNDERLINE_OFF(24), BLINK_OFF(25),
        INVERSE_OFF(27), REVERSE_VIDEO_OFF(27), REVEAL(28), INVISIBLE_OFF(28), CROSSED_OUT_OFF(29),
        FOREGROUND_BLACK(30), FOREGROUND_RED(31), FOREGROUND_GREEN(32), FOREGROUND_YELLOW(33),
        FOREGROUND_BLUE(34), FOREGROUND_MAGENTA(35), FOREGROUND_CYAN(36), FOREGROUND_WHITE(37),
        FOREGROUND_CUSTOM(38), FOREGROUND_DEFAULT(39), BACKGROUND_BLACK(40), BACKGROUND_RED(41),
        BACKGROUND_GREEN(42), BACKGROUND_YELLOW(43), BACKGROUND_BLUE(44), BACKGROUND_MAGENTA(45),
        BACKGROUND_CYAN(46), BACKGROUND_WHITE(47), BACKGROUND_CUSTOM(48), BACKGROUND_DEFAULT(49), FRAMED(51),
        ENCIRCLED(52), OVERLINE(53), FRAMED_AND_ENCIRCLED_OFF(54), OVERLINE_OFF(55), IDEO_UNDERLINE(60),
        RIGHT_SIDE_LINE(60), IDEO_DOUBLE_UNDERLINE(61), DOUBLE_RIGHT_SIDE_LINE(61), IDEO_OVERLINE(62),
        LEFT_SIDE_LINE(62), IDEO_DOUBLE_OVERLINE(63), DOUBLE_LEFT_SIDE_LINE(63), IDEO_STRESS_MARK(64),
        IDEO_RESET(65), SIDE_LINE_RESET(65), FOREGROUND_BRIGHT_BLACK(90), FOREGROUND_BRIGHT_RED(91),
        FOREGROUND_BRIGHT_GREEN(92), FOREGROUND_BRIGHT_YELLOW(93), FOREGROUND_BRIGHT_BLUE(94),
        FOREGROUND_BRIGHT_MAGENTA(95), FOREGROUND_BRIGHT_CYAN(96), FOREGROUND_BRIGHT_WHITE(97),
        BACKGROUND_BRIGHT_BLACK(100), BACKGROUND_BRIGHT_RED(101), BACKGROUND_BRIGHT_GREEN(102),
        BACKGROUND_BRIGHT_YELLOW(103), BACKGROUND_BRIGHT_BLUE(104), BACKGROUND_BRIGHT_MAGENTA(105),
        BACKGROUND_BRIGHT_CYAN(106), BACKGROUND_BRIGHT_WHITE(107);
        
        private final int code;
        
        SGRCode(int code) { this.code = code; }
        
        public static SGRCode get(int code) {
            return Arrays.stream(SGRCode.values())
                         .filter(sgr -> sgr.getCode() == code)
                         .findAny()
                         .orElse(null);
        }
        
        public int getCode() { return code; }
        
        @Override
        public String toString() { return g(getCode()); }
    }
    
    public enum Intensity {
        DEFAULT(SGRCode.BOLD_OFF), FAINT(SGRCode.FAINT), BOLD(SGRCode.BOLD);
        private final SGRCode sgr;
        
        Intensity(SGRCode sgr) { this.sgr = sgr; }
        
        public int getCode() { return sgr.getCode(); }
        
        public SGRCode getSGR() { return sgr; }
        
        @Override
        public String toString() { return sgr.toString(); }
    }
    
    public static String reset() {
        return "\033c";
    }
    
    /**
     * Resets all Select/Set Graphic Rendition attributes.
     *
     * @return ANSI escape sequence
     */
    public static String clearSGR() {
        return g(0);
    }
    
    private static String g(int... params) {
        return es('m', params);
    }
    
    private static String es(char endChar, int... params) {
        char[] crissDeJavaStupideAMarde = new char[params.length];
        for (int i = 0; i < params.length; i++) { crissDeJavaStupideAMarde[i] = (char) params[i]; }
        return String.format("%c[%s%c", ESC, String.join(";", new String(crissDeJavaStupideAMarde)), endChar);
    }
    
    
    public static class Color {
        private final int[] params;
        
        private Color(SGRCode identifier, int... params) {
            this.params = ArrayUtils.addAll(new int[]{identifier.getCode()}, params);
        }
        
        private Color(Color color) {
            this.params = color.params;
        }
        
        private static Color color256(SGRCode sgr, short color256) {
            if (color256 < 0 || color256 > 0xFF) { throw new Invalid256ColorException(); }
            return new Color(sgr, 5, color256);
        }
        
        private static Color rgb(SGRCode sgr, short red, short green, short blue) {
            if (Stream.of(red, green, blue).anyMatch(n -> n < 0 || n > 0xFF)) {
                throw new InvalidRGBValueException();
            }
            return new Color(sgr, 2, red, green, blue);
        }
        
        public int[] getSGRParams() { return params; }
        
        @Override
        public boolean equals(Object obj) {
            return obj instanceof Color && Arrays.equals(((Color) obj).params, params);
        }
        
        public static final class ForegroundColor extends Color {
            public static final ForegroundColor DEFAULT        = new ForegroundColor(
                    SGRCode.FOREGROUND_DEFAULT);
            public static final ForegroundColor BLACK          = new ForegroundColor(
                    SGRCode.FOREGROUND_BLACK);
            public static final ForegroundColor RED            = new ForegroundColor(SGRCode.FOREGROUND_RED);
            public static final ForegroundColor GREEN          = new ForegroundColor(
                    SGRCode.FOREGROUND_GREEN);
            public static final ForegroundColor YELLOW         = new ForegroundColor(
                    SGRCode.FOREGROUND_YELLOW);
            public static final ForegroundColor BLUE           = new ForegroundColor(SGRCode.FOREGROUND_BLUE);
            public static final ForegroundColor MAGENTA        = new ForegroundColor(
                    SGRCode.FOREGROUND_MAGENTA);
            public static final ForegroundColor CYAN           = new ForegroundColor(SGRCode.FOREGROUND_CYAN);
            public static final ForegroundColor WHITE          = new ForegroundColor(
                    SGRCode.FOREGROUND_WHITE);
            public static final ForegroundColor BRIGHT_BLACK   = new ForegroundColor(
                    SGRCode.FOREGROUND_BRIGHT_BLACK);
            public static final ForegroundColor BRIGHT_RED     = new ForegroundColor(
                    SGRCode.FOREGROUND_BRIGHT_RED);
            public static final ForegroundColor BRIGHT_GREEN   = new ForegroundColor(
                    SGRCode.FOREGROUND_BRIGHT_GREEN);
            public static final ForegroundColor BRIGHT_YELLOW  = new ForegroundColor(
                    SGRCode.FOREGROUND_BRIGHT_YELLOW);
            public static final ForegroundColor BRIGHT_BLUE    = new ForegroundColor(
                    SGRCode.FOREGROUND_BRIGHT_BLUE);
            public static final ForegroundColor BRIGHT_MAGENTA = new ForegroundColor(
                    SGRCode.FOREGROUND_BRIGHT_MAGENTA);
            public static final ForegroundColor BRIGHT_CYAN    = new ForegroundColor(
                    SGRCode.FOREGROUND_BRIGHT_CYAN);
            public static final ForegroundColor BRIGHT_WHITE   = new ForegroundColor(
                    SGRCode.FOREGROUND_BRIGHT_WHITE);
            
            private ForegroundColor(SGRCode identifier, int... params) {
                super(identifier, params);
            }
            
            private ForegroundColor(Color color) {
                super(color);
            }
            
            public static ForegroundColor color256(short color256) {
                return new ForegroundColor(Color.color256(SGRCode.FOREGROUND_CUSTOM, color256));
            }
            
            public static ForegroundColor rgb(short red, short green, short blue) {
                return new ForegroundColor(Color.rgb(SGRCode.FOREGROUND_CUSTOM, red, green, blue));
            }
        }
        
        public static final class BackgroundColor extends Color {
            public static final BackgroundColor DEFAULT        = new BackgroundColor(
                    SGRCode.FOREGROUND_DEFAULT);
            public static final BackgroundColor BLACK          = new BackgroundColor(
                    SGRCode.BACKGROUND_BLACK);
            public static final BackgroundColor RED            = new BackgroundColor(SGRCode.BACKGROUND_RED);
            public static final BackgroundColor GREEN          = new BackgroundColor(
                    SGRCode.BACKGROUND_GREEN);
            public static final BackgroundColor YELLOW         = new BackgroundColor(
                    SGRCode.BACKGROUND_YELLOW);
            public static final BackgroundColor BLUE           = new BackgroundColor(SGRCode.BACKGROUND_BLUE);
            public static final BackgroundColor MAGENTA        = new BackgroundColor(
                    SGRCode.BACKGROUND_MAGENTA);
            public static final BackgroundColor CYAN           = new BackgroundColor(SGRCode.BACKGROUND_CYAN);
            public static final BackgroundColor WHITE          = new BackgroundColor(
                    SGRCode.BACKGROUND_WHITE);
            public static final BackgroundColor BRIGHT_BLACK   = new BackgroundColor(
                    SGRCode.BACKGROUND_BRIGHT_BLACK);
            public static final BackgroundColor BRIGHT_RED     = new BackgroundColor(
                    SGRCode.BACKGROUND_BRIGHT_RED);
            public static final BackgroundColor BRIGHT_GREEN   = new BackgroundColor(
                    SGRCode.BACKGROUND_BRIGHT_GREEN);
            public static final BackgroundColor BRIGHT_YELLOW  = new BackgroundColor(
                    SGRCode.BACKGROUND_BRIGHT_YELLOW);
            public static final BackgroundColor BRIGHT_BLUE    = new BackgroundColor(
                    SGRCode.BACKGROUND_BRIGHT_BLUE);
            public static final BackgroundColor BRIGHT_MAGENTA = new BackgroundColor(
                    SGRCode.BACKGROUND_BRIGHT_MAGENTA);
            public static final BackgroundColor BRIGHT_CYAN    = new BackgroundColor(
                    SGRCode.BACKGROUND_BRIGHT_CYAN);
            public static final BackgroundColor BRIGHT_WHITE   = new BackgroundColor(
                    SGRCode.BACKGROUND_BRIGHT_WHITE);
            
            private BackgroundColor(SGRCode identifier, int... params) {
                super(identifier, params);
            }
            
            private BackgroundColor(Color color) {
                super(color);
            }
            
            public static BackgroundColor color256(short color256) {
                return new BackgroundColor(Color.color256(SGRCode.BACKGROUND_CUSTOM, color256));
            }
            
            public static BackgroundColor rgb(short red, short green, short blue) {
                return new BackgroundColor(Color.rgb(SGRCode.BACKGROUND_CUSTOM, red, green, blue));
            }
        }
        
        @Override
        public String toString() { return g(params); }
        
    }
    
    public static class InvalidRGBValueException extends RuntimeException {
        public InvalidRGBValueException() { super(); }
        
        public InvalidRGBValueException(short red, short green, short blue) {
            super(String.format("Invalid %d value (0 <= %sn%s <= 255)",
                                red < 0 || red > 0xFF ? "RED"
                                                      : green < 0 || green > 0xFF ? "GREEN"
                                                                                  : "BLUE",
                                SGRCode.ITALIC.toString(),
                                SGRCode.ITALIC_AND_FRAKTUR_OFF.toString()));
        }
    }
    
    public static class Invalid256ColorException extends RuntimeException {
        public Invalid256ColorException() {
            super(String.format("Expected a 256-color code (0 <= %sn%s <= 255)",
                                SGRCode.ITALIC.toString(),
                                SGRCode.ITALIC_AND_FRAKTUR_OFF.toString()));
        }
    }
    
    public static class EscapeSequence {
        private Intensity             intensity;
        private Color.ForegroundColor fgColor;
        private Color.BackgroundColor bgColor;
        private boolean               reverseVideo, underlined, italic, invisible, crossed, overlined, blink;
        
        public EscapeSequence() {
            intensity = Intensity.DEFAULT;
            fgColor = Color.ForegroundColor.DEFAULT;
            bgColor = Color.BackgroundColor.DEFAULT;
            reverseVideo = underlined = italic = invisible = crossed = overlined = blink = false;
        }
        
        public String wrap(String content) {
            return toString() + content + clearSGR();
        }
        
        public Intensity getIntensity() { return intensity; }
        
        public void setIntensity(Intensity intensity) {
            this.intensity = intensity == null ? Intensity.DEFAULT : intensity;
        }
        
        public boolean isReverseVideo() { return reverseVideo; }
        
        public void setReverseVideo(boolean reverseVideo) { this.reverseVideo = reverseVideo; }
        
        public boolean isUnderlined() { return underlined; }
        
        public void setUnderlined(boolean underlined) { this.underlined = underlined; }
        
        public boolean isItalic() { return italic; }
        
        public void setItalic(boolean italic) { this.italic = italic; }
        
        public boolean isInvisible() { return invisible; }
        
        public void setInvisible(boolean invisible) { this.invisible = invisible; }
        
        public boolean isCrossed() { return crossed; }
        
        public void setCrossed(boolean crossed) { this.crossed = crossed; }
        
        public boolean isOverlined() { return overlined; }
        
        public void setOverlined(boolean overlined) { this.overlined = overlined; }
        
        public boolean isBlink() { return blink; }
        
        public void setBlink(boolean blink) { this.blink = blink; }
        
        public Color.ForegroundColor getForegroundColor() { return fgColor; }
        
        public void setForegroundColor(Color.ForegroundColor fgColor) {
            this.fgColor = fgColor == null ? Color.ForegroundColor.DEFAULT : fgColor;
        }
        
        public Color.BackgroundColor getBackgroundColor() { return bgColor; }
        
        public void setBackgroundColor(Color.BackgroundColor bgColor) {
            this.bgColor = bgColor == null ? Color.BackgroundColor.DEFAULT : bgColor;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof EscapeSequence)) { return false; }
            EscapeSequence esc = (EscapeSequence) obj;
            return esc.blink == blink
                   && esc.crossed == crossed
                   && esc.invisible == invisible
                   && esc.italic == italic
                   && esc.overlined == overlined
                   && esc.reverseVideo == reverseVideo
                   && esc.underlined == underlined
                   && esc.intensity.equals(intensity)
                   && esc.fgColor.equals(fgColor)
                   && esc.bgColor.equals(bgColor);
        }
        
        @SuppressWarnings({"MethodDoesntCallSuperMethod", "CloneDoesntDeclareCloneNotSupportedException"})
        @Override
        public Object clone() {
            EscapeSequence esc = new EscapeSequence();
            esc.blink = blink;
            esc.crossed = crossed;
            esc.intensity = intensity;
            esc.invisible = invisible;
            esc.italic = italic;
            esc.overlined = overlined;
            esc.reverseVideo = reverseVideo;
            esc.underlined = underlined;
            esc.fgColor = fgColor;
            esc.bgColor = bgColor;
            return esc;
        }
        
        @Override
        public String toString() {
            return String.format("%s[0;%d;%s;%s;%s;%s;%s;%s;%s;%s;%sm",
                                 ESC,
                                 intensity.getSGR().getCode(),
                                 blink ? SGRCode.SLOW_BLINK : SGRCode.BLINK_OFF,
                                 crossed ? SGRCode.CROSSED_OUT : SGRCode.CROSSED_OUT_OFF,
                                 invisible ? SGRCode.INVISIBLE : SGRCode.INVISIBLE_OFF,
                                 italic ? SGRCode.ITALIC : SGRCode.ITALIC_AND_FRAKTUR_OFF,
                                 overlined ? SGRCode.OVERLINE : SGRCode.OVERLINE_OFF,
                                 reverseVideo ? SGRCode.REVERSE_VIDEO : SGRCode.REVERSE_VIDEO_OFF,
                                 underlined ? SGRCode.UNDERLINED : SGRCode.UNDERLINE_OFF,
                                 Arrays.stream(fgColor.getSGRParams())
                                       .mapToObj(String::valueOf)
                                       .collect(Collectors.joining(";")),
                                 Arrays.stream(bgColor.getSGRParams())
                                       .mapToObj(String::valueOf)
                                       .collect(Collectors.joining(";")));
        }
    }
    
    public static final class Cursor {
        
        /**
         * Moves the cursor {@code n} (default 1) cells up. If the cursor is
         * already at the edge of the screen, this has no effect.
         *
         * @param cells
         *         Movement lenght
         * @return ANSI escape sequence
         */
        public static String up(int cells) {
            return es('A', cells);
        }
        
        /**
         * Moves the cursor {@code n} (default 1) cells down. If the cursor is
         * already at the edge of the screen, this has no effect.
         *
         * @param cells
         *         Movement lenght
         * @return ANSI escape sequence
         */
        public static String down(int cells) {
            return es('B', cells);
        }
        
        /**
         * Moves the cursor {@code n} (default 1) cells right. If the cursor is
         * already at the edge of the screen, this has no effect.
         *
         * @param cells
         *         Movement lenght
         * @return ANSI escape sequence
         */
        public static String right(int cells) {
            return es('C', cells);
        }
        
        /**
         * Moves the cursor {@code n} (default 1) cells left. If the cursor is
         * already at the edge of the screen, this has no effect.
         *
         * @param cells
         *         Movement lenght
         * @return ANSI escape sequence
         */
        public static String left(int cells) {
            return es('D', cells);
        }
        
        /**
         * Moves cursor to beginning of the line {@code n} (default 1) lines
         * down. (not ANSI.SYS)
         *
         * @param lines
         *         Movement lenght
         * @return ANSI escape sequence
         */
        public static String nextLine(int lines) {
            return es('E', lines);
        }
        
        /**
         * Moves cursor to beginning of the line {@code n} (default 1) lines
         * up. (not ANSI.SYS)
         *
         * @param lines
         *         Movement lenght
         * @return ANSI escape sequence
         */
        public static String prevLine(int lines) {
            return es('F', lines);
        }
        
        /**
         * Moves the cursor to column {@code n} (default 1).
         * (not ANSI.SYS)
         *
         * @param col
         *         Column position
         * @return ANSI escape sequence
         */
        public static String toColumn(int col) {
            return es('G', col);
        }
        
        /**
         * Moves the cursor to row {@code n}, column {@code m}. The values are
         * 1-based, and default to 1 (top left corner) if omitted. A sequence
         * such as {@code CSI ;5H} is a synonym for {@code CSI 1;5H} as well
         * as {@code CSI 17;H} is the same as {@code CSI 17H} and {@code CSI 17;1H}
         *
         * @param row
         *         Row position (y)
         * @param col
         *         Column position (x)
         * @return ANSI escape sequence
         */
        public static String pos(int row, int col) {
            return es('H', row, col);
        }
        
        /**
         * (ALternative)
         *
         * @param row
         *         Row position (y)
         * @param col
         *         Column position (x)
         * @return ANSI escape sequence
         *
         * @see #pos(int, int)
         */
        public static String pos2(int row, int col) {
            return es('f', row, col);
        }
        
        /**
         * Clears part of the screen. If {@code n} is 0 (or missing), clear
         * from cursor to end of screen. If {@code n} is 1, clear from cursor
         * to beginning of the screen. If {@code n} is 2, clear entire screen
         * (and moves cursor to upper left on DOS ANSI.SYS). If {@code n} is 3,
         * clear entire screen and delete all lines saved in the scrollback
         * buffer (this feature was added for xterm and is supported by other
         * terminal applications).
         *
         * @param n
         *         Screen part code (0, 1, 2 or 3)
         * @return ANSI escape sequence
         */
        public static String eraseInScreen(int n) {
            return es('J', n);
        }
        
        /**
         * Erases part of the line. If {@code n} is 0 (or missing), clear from
         * cursor to the end of the line. If {@code n} n is 1, clear from
         * cursor to beginning of the line. If {@code n} is 2, clear entire
         * line. Cursor position does not change.
         *
         * @param n
         *         Line part code (0, 1 or 2)
         * @return ANSI escape sequence
         */
        public static String eraseInLine(int n) {
            return es('K', n);
        }
        
        /**
         * Scroll whole page up by {@code n} (default 1) lines. New lines are
         * added at the bottom. (not ANSI.SYS)
         *
         * @param lines
         *         Number of lines to scroll up
         * @return ANSI escape sequence
         */
        public static String scrollUp(int lines) {
            return es('S', lines);
        }
        
        /**
         * Scroll whole page down by {@code n} (default 1) lines. New lines are
         * added at the top. (not ANSI.SYS)
         *
         * @param lines
         *         Number of lines to scroll down
         * @return ANSI escape sequence
         */
        public static String scrollDown(int lines) {
            return es('T', lines);
        }
        
        /**
         * Enable/Disable aux serial port usually for local serial printer
         *
         * @param enable
         *         Enable/disable serial port
         * @return ANSI escape sequence
         */
        public static String toggleSerialPort(boolean enable) {
            return es('i', enable ? 5 : 4);
        }
        
        /**
         * Reports the cursor position (CPR) to the application as (as though
         * typed at the keyboard) {@code ESC[n;mR}, where {@code n} is the row
         * and {@code m} is the column.
         *
         * @return ANSI escape sequence
         */
        public static String cursorPositionReport() {
            return es('n', 6);
        }
        
        /**
         * Saves the cursor position/state.
         *
         * @return ANSI escape sequence
         */
        public static String saveCursorPosition() {
            return es('s');
        }
        
        /**
         * Restores the cursor position/state.
         *
         * @return ANSI escape sequence
         */
        public static String restoreCursorPosition() {
            return es('u');
        }
        
    }
}
