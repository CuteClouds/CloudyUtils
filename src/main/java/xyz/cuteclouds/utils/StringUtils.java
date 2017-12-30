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

    /**
     * Limits a {@link String} to the specified length.
     *
     * @param value  the string to be limited
     * @param length the maximum length
     * @return the limited string. might be itself.
     */
    public static String limit(String value, int length) {
        if (value.length() <= length) return value;
        return value.substring(0, length - 3) + "...";
    }

    /**
     * Parse a {@link String}[] to a {@link Map}.
     *
     * @param args the array
     * @return the parsed map
     */
    public static Map<String, String> parse(String[] args) {
        LinkedList<String> list = new LinkedList<>();
        Collections.addAll(list, args);
        return StringUtilsImpl.parse0(list);
    }

    /**
     * Parses a {@link String} to a map
     *
     * @param args the string
     * @return the parsed map
     */
    public static Map<String, String> parse(String args) {
        TCharLinkedQueueList list = new TCharLinkedQueueList((char) -1);
        list.add(args.toCharArray());
        return StringUtilsImpl.parse0(StringUtilsImpl.split0(list, 0, false));
    }

    /**
     * Parses a {@link Collection} of {@link String}s to a {@link Map}.
     *
     * @param args the collection
     * @return the parsed map
     */
    public static Map<String, String> parse(Collection<String> args) {
        return StringUtilsImpl.parse0(new LinkedList<>(args));
    }

    /**
     * Parses a {@link String} to a {@link String}[]
     *
     * @param args         the string
     * @param expectedArgs the expected amount of arguments (last argument will be rest)
     * @return the parsed array
     */
    public static String[] splitArgs(String args, int expectedArgs) {
        return splitArgs(args, expectedArgs < 2 ? expectedArgs : expectedArgs - 1, true);
    }

    /**
     * Parses a {@link String} to a {@link String}[]. Splits to exhaustion.
     *
     * @param args the string
     * @return the parsed array
     */
    public static String[] splitArgs(String args) {
        return splitArgs(args, 0, false);
    }

    /**
     * Parses a {@link String} to a {@link String}[]
     *
     * @param args         the string
     * @param expectedArgs the expected amount of arguments
     * @param rest         if the rest should be included as the last array value or discarded
     * @return the parsed array
     */
    public static String[] splitArgs(String args, int expectedArgs, boolean rest) {
        TCharLinkedQueueList list = new TCharLinkedQueueList((char) -1);
        list.add(args.toCharArray());
        return StringUtilsImpl.split0(list, expectedArgs, rest).toArray(STRING_ARRAY);
    }

    /**
     * Recursively loops the String and unbox any pairs of {@code "} or  {@code '}
     *
     * @param s the string
     * @return unboxed string
     */
    public static String unbox(String s) {
        while (!s.isEmpty() && (s.startsWith("\"") && s.endsWith("\"") || s.startsWith("'") && s.endsWith("'"))) {
            s = s.substring(1, s.length() - 1);
        }

        return s;
    }

    /**
     * Recursively loops the String and sequentially unboxes it as much as possible
     *
     * @param s           the string
     * @param sorrounding the chars to be removed
     * @return unboxed string
     */
    public static String unbox(String s, String sorrounding) {
        return unbox(s, sorrounding.toCharArray());
    }

    /**
     * Recursively loops the String and sequentially unboxes it as much as possible
     *
     * @param s     the string
     * @param chars the chars to be removed
     * @return unboxed string
     */
    public static String unbox(String s, char... chars) {
        for (char ch : chars) {
            String c = String.valueOf(ch);
            if (s.isEmpty() || !s.startsWith(c) || !s.endsWith(c)) {
                break;
            }

            s = s.substring(1, s.length() - 1);
        }

        return s;
    }
}
