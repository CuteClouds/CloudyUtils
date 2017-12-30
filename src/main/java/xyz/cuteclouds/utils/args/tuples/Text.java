package xyz.cuteclouds.utils.args.tuples;

/**
 * Represents a {@link String}.
 *
 * @see xyz.cuteclouds.utils.args.ArgParser
 * @see Arg
 * @see String
 */
public class Text implements Arg {
    private final String value;

    /**
     * Creates a new object
     *
     * @param value The value of this object
     */
    public Text(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * Returns the value of this object.
     * @return The value of this object
     */
    public String value() {
        return value;
    }
}
