package xyz.cuteclouds.utils.args.tuples;

import java.util.function.Supplier;

public interface Arg {
    default boolean isPair() {
        return this instanceof Pair;
    }

    default boolean isTuple() {
        return this instanceof Tuple;
    }

    default boolean isText() {
        return this instanceof Text;
    }

    default Pair asPair() {
        return (Pair) this;
    }

    default Tuple asTuple() {
        return (Tuple) this;
    }

    default String asText() {
        return ((Text) this).value();
    }

    default Pair requirePair(Supplier<RuntimeException> e) {
        if (!isPair()) throw e.get();
        return asPair();
    }

    default Tuple requireTuple(Supplier<RuntimeException> e) {
        if (!isTuple()) throw e.get();
        return asTuple();
    }

    default String requireText(Supplier<RuntimeException> e) {
        if (!isText()) throw e.get();
        return asText();
    }
}
