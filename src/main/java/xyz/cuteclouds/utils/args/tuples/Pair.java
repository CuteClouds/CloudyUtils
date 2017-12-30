package xyz.cuteclouds.utils.args.tuples;

import java.util.Map;

/**
 * Represents a name-value pair.
 *
 * @see xyz.cuteclouds.utils.args.ArgParser
 * @see Arg
 * @see javafx.util.Pair
 */
public class Pair extends javafx.util.Pair<String, Arg> implements Arg, Map.Entry<String, Arg> {
    /**
     * Creates a new pair
     *
     * @param key   The key for this pair
     * @param value The value to use for this pair
     */
    public Pair(String key, Arg value) {
        super(key, value);
    }

    @Override
    public Arg setValue(Arg ignored) {
        throw new UnsupportedOperationException("setValue");
    }
}
