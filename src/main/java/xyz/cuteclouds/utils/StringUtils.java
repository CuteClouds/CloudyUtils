package xyz.cuteclouds.utils;

import gnu.trove.list.linked.TCharLinkedQueueList;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;

/**
 * Utility class for dealing with Strings and parsing.
 */
public class StringUtils {
    private static final String[] STRING_ARRAY = new String[0];

    public static String limit(String value, int length) {
        if (value.length() <= length) return value;
        return value.substring(0, length - 3) + "...";
    }

    public static Map<String, String> parse(String[] args) {
        LinkedList<String> list = new LinkedList<>();
        Collections.addAll(list, args);
        return StringUtilsImpl.parse0(list);
    }

    public static Map<String, String> parse(String args) {
        TCharLinkedQueueList list = new TCharLinkedQueueList((char) -1);
        list.add(args.toCharArray());
        return StringUtilsImpl.parse0(StringUtilsImpl.split0(list, 0, false));
    }

    public static Map<String, String> parse(Collection<String> args) {
        return StringUtilsImpl.parse0(new LinkedList<>(args));
    }

    public static String[] splitArgs(String args, int expectedArgs) {
        return splitArgs(args, expectedArgs < 2 ? expectedArgs : expectedArgs - 1, true);
    }

    public static String[] splitArgs(String args, int expectedArgs, boolean rest) {
        TCharLinkedQueueList list = new TCharLinkedQueueList((char) -1);
        list.add(args.toCharArray());
        return StringUtilsImpl.split0(list, expectedArgs, rest).toArray(STRING_ARRAY);
    }

    public static String unbox(String s) {
        while (!s.isEmpty() && (s.startsWith("\"") && s.endsWith("\"") || s.startsWith("'") && s.endsWith("'"))) {
            s = s.substring(1, s.length() - 1);
        }

        return s;
    }
}
