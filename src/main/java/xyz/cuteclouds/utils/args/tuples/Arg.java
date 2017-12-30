package xyz.cuteclouds.utils.args.tuples;

import java.util.Optional;

/**
 * Base object of {@link xyz.cuteclouds.utils.args.ArgParser}, being also used in {@link Tuple}s and {@link Pair}s.
 *
 * @see xyz.cuteclouds.utils.args.ArgParser
 * @see Text
 * @see Tuple
 * @see Pair
 */
public interface Arg {
    /**
     * Casts this object to a {@link Pair}.
     *
     * @return itself.
     * @throws ClassCastException if this object is not an instance of {@link Pair}.
     */
    default Pair asPair() {
        return (Pair) this;
    }

    /**
     * Casts this object to a {@link Text} and returns it value.
     *
     * @return the {@link String} value of this object.
     * @throws ClassCastException if this object is not an instance of {@link Text}.
     */
    default String asText() {
        return ((Text) this).value();
    }

    /**
     * Casts this object to a {@link Tuple}.
     *
     * @return itself.
     * @throws ClassCastException if this object is not an instance of {@link Tuple}.
     */
    default Tuple asTuple() {
        return (Tuple) this;
    }

    /**
     * Checks if this object is a {@link Pair}.
     *
     * @return true if this object is an instance of {@link Pair}.
     */
    default boolean isPair() {
        return this instanceof Pair;
    }

    /**
     * Checks if this object is a {@link Text}.
     *
     * @return true if this object is an instance of {@link Text}.
     */
    default boolean isText() {
        return this instanceof Text;
    }

    /**
     * Checks if this object is a {@link Tuple}.
     *
     * @return true if this object is an instance of {@link Tuple}.
     */
    default boolean isTuple() {
        return this instanceof Tuple;
    }

    /**
     * Checks if this object is a {@link Pair} and maybe casts it.
     *
     * @return empty object, if not an instance of {@link Pair}, otherwise, the object.
     */
    default Optional<Pair> optionalPair() {
        if (!isPair()) return Optional.empty();
        return Optional.of(asPair());
    }

    /**
     * Checks if this object is a {@link Text} and maybe casts it.
     *
     * @return empty object, if not an instance of {@link Tuple}, otherwise, the string value.
     */
    default Optional<String> optionalText() {
        if (!isText()) return Optional.empty();
        return Optional.of(asText());
    }

    /**
     * Checks if this object is a {@link Tuple} and maybe casts it.
     *
     * @return empty object, if not an instance of {@link Tuple}, otherwise, the object.
     */
    default Optional<Tuple> optionalTuple() {
        if (!isTuple()) return Optional.empty();
        return Optional.of(asTuple());
    }
}
