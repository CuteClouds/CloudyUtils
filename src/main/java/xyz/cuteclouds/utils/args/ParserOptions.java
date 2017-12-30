package xyz.cuteclouds.utils.args;

/**
 * Lists of flags available to use on {@link ArgParser}.
 */
public class ParserOptions {
    /**
     * Default parsing options.
     */
    public static final int DEFAULT = 0b000;
    /**
     * <p>Disables Implicit Tuples.</p>
     * By default, when semicolons are present,
     * <pre>"key1: value1, value2; key2: false"</pre>
     * will turn into
     * <pre>"key1: (value1, value2), key2: false"</pre>
     * <p>Passing this flag disables this behaviour, and commas will behave as semicolons.</p>
     */
    public static final int NO_IMPLICIT_TUPLES = 0x010;
    /**
     * <p>Disables Smart Tuples.</p>
     * By default,
     * <pre>"key: (value)"</pre>
     * will be unboxed from the tuple and turn
     * <pre>"key: value"</pre>
     * <p>Passing this flag disables this behaviour.</p>
     */
    public static final int NO_SMART_TUPLES = 0b001;
    /**
     * <p>Disables Raw Pair.</p>
     * By default,
     * <pre>"key1: key2: value1"</pre>
     * will turn into
     * <pre>"key1: (key2: value1)"</pre>
     * <p>Passing this flag disables this behaviour.</p>
     */
    public static final int RAW_PAIRS = 0b100;
}
