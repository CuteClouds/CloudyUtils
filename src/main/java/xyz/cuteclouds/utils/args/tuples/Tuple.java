package xyz.cuteclouds.utils.args.tuples;

import java.util.List;

public interface Tuple extends Arg, List<Arg> {
    default boolean containsAny(String value) {
        for (Arg arg : this) {
            if (arg.isPair() && arg.asPair().getKey().equals(value)) {
                return true;
            } else if (arg.isText() && arg.asText().equals(value)) {
                return true;
            }
        }
        return false;
    }

    default boolean containsPair(String key) {
        for (Arg arg : this) {
            if (arg.isPair() && arg.asPair().getKey().equals(key)) return true;
        }
        return false;
    }

    default boolean containsText(String text) {
        for (Arg arg : this) {
            if (arg.isText() && arg.asText().equals(text)) return true;
        }
        return false;
    }

    default Arg get(String key) {
        for (Arg arg : this) {
            if (!arg.isPair()) continue;

            Pair pair = arg.asPair();
            if (pair.getKey().equals(key)) {
                return pair.getValue();
            }
        }
        return null;
    }

    default Arg firstArg() {
        if (isEmpty()) return null;
        return get(0);
    }

    default boolean isSingleton() {
        return size() == 1;
    }
}
