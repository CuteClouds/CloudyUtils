package xyz.cuteclouds.utils;

import gnu.trove.queue.TCharQueue;

import java.util.LinkedHashMap;
import java.util.LinkedList;

import static xyz.cuteclouds.utils.StringUtils.unbox;

class StringUtilsImpl {
    private static String unprefix(String s) {
        char c = s.charAt(0);
        while (!s.isEmpty() && s.charAt(0) == c) s = s.substring(1);
        return s;
    }

    static LinkedHashMap<String, String> parse0(LinkedList<String> args) {
        LinkedHashMap<String, String> result = new LinkedHashMap<>();

        while (!args.isEmpty()) {
            String arg = args.poll();
            if (arg.charAt(0) == '-' || arg.charAt(0) == '/') {
                String k = unprefix(arg);

                int indexOf = k.indexOf('=');
                if (indexOf != -1) {
                    result.put(unbox(k.substring(0, indexOf)), unbox(k.substring(indexOf + 1)));
                } else {
                    String v = args.peek();

                    if (v == null || v.charAt(0) == '-' || v.charAt(0) == '/') {
                        result.put(unbox(k), null);
                    } else {
                        args.remove();
                        result.put(unbox(k), v);
                    }
                }

            } else {
                result.compute(null, (k, v) -> v == null ? arg : v + " " + arg);
            }
        }

        return result;
    }

    static LinkedList<String> split0(TCharQueue chars, int expectedArgs, boolean rest) {
        LinkedList<String> result = new LinkedList<>();

        if (expectedArgs < 1) rest = false;

        StringBuilder b = new StringBuilder();

        while (!chars.isEmpty()) {
            char c = chars.poll();

            if (c == '\\' && !chars.isEmpty()) {
                b.append(escape(chars.poll()));
                continue;
            }

            if (c == '"' || c == '\'' || c == '`') {
                char quoteChar = c;
                //Grab all starting quotes
                b.append(c);
                while (!chars.isEmpty()) {
                    c = chars.poll();
                    if (c != quoteChar) break;
                    b.append(c);
                }
                //Then content
                b.append(c);
                while (!chars.isEmpty()) {
                    c = chars.poll();

                    if (c == '\\' && !chars.isEmpty()) {
                        b.append(escape(chars.poll()));
                        continue;
                    }

                    b.append(c);
                    if (c == quoteChar) break;
                }
                //Grab all ending quotes
                while (!chars.isEmpty()) {
                    c = chars.peek();
                    if (c != quoteChar) break;
                    b.append(c);
                    chars.poll();
                }
                continue;
            }

            if (!Character.isWhitespace(c)) {
                b.append(c);
                continue;
            }

            if (b.length() != 0) {
                result.add(unbox(b.toString()));

                if (expectedArgs > 0 && result.size() == expectedArgs) {
                    break;
                }

                b = new StringBuilder();
            }

            while (!chars.isEmpty()) {
                if (Character.isWhitespace(chars.peek())) {
                    chars.poll();
                    continue;
                }
                break;
            }
        }

        if (b.length() != 0 && (expectedArgs <= 0 || result.size() != expectedArgs)) {
            result.add(unbox(b.toString()));
        }

        if (rest) {
            b = new StringBuilder();

            while (!chars.isEmpty()) {
                if (Character.isWhitespace(chars.peek())) {
                    chars.poll();
                    continue;
                }
                break;
            }

            while (!chars.isEmpty()) {
                char c = chars.poll();

                if (c == '\\' && !chars.isEmpty()) {
                    b.append(escape(chars.poll()));
                    continue;
                }

                b.append(c);
            }

            result.add(unbox(b.toString()));
        }

        return result;
    }

    private static char escape(char c) {
        switch (c) {
            case 'n':
                return '\n';
            case 'r':
                return '\r';
            case 't':
                return '\t';
            default:
                return c;
        }
    }
}
