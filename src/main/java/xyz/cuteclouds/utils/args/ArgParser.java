package xyz.cuteclouds.utils.args;

import org.intellij.lang.annotations.MagicConstant;
import xyz.cuteclouds.utils.args.external.Position;
import xyz.cuteclouds.utils.args.external.SyntaxException;
import xyz.cuteclouds.utils.args.impl.ArgLexer;
import xyz.cuteclouds.utils.args.impl.Token;
import xyz.cuteclouds.utils.args.tuples.*;

import java.util.LinkedList;
import java.util.List;

import static xyz.cuteclouds.utils.args.impl.TokenType.*;

public class ArgParser {
    private final boolean semicolons, smartTuples, implicitTuples, rawPairs;
    //yes, a LinkedList. why? because I need Queue capabilities.
    private final LinkedList<Token> tokens;

    public ArgParser(List<Token> tokens, @MagicConstant(flagsFromClass = ParserOptions.class) int flags) {
        if (tokens instanceof LinkedList) {
            this.tokens = (LinkedList<Token>) tokens;
        } else {
            this.tokens = new LinkedList<>(tokens);
        }

        this.semicolons = tokens.stream().anyMatch(token -> token.getType() == SEMICOLON);
        if (tokens.isEmpty()) tokens.add(new Token(new Position(0, 0, 0), EOF));

        this.smartTuples = (flags & ParserOptions.NO_SMART_TUPLES) == 0;
        this.implicitTuples = (flags & ParserOptions.NO_IMPLICIT_TUPLES) == 0;
        this.rawPairs = (flags & ParserOptions.RAW_PAIRS) == ParserOptions.RAW_PAIRS;

        //System.out.println(tokens.stream().map(Object::toString).collect(Collectors.joining(" ")));
    }

    public ArgParser(ArgLexer lexer, @MagicConstant(flagsFromClass = ParserOptions.class) int flags) {
        this(lexer.getTokens(), flags);
    }

    public ArgParser(String s, @MagicConstant(flagsFromClass = ParserOptions.class) int flags) {
        this(new ArgLexer(s), flags);
    }

    public ArgParser(List<Token> tokens) {
        this(tokens, 0);
    }

    public ArgParser(ArgLexer lexer) {
        this(lexer, 0);
    }

    public ArgParser(String s) {
        this(s, 0);
    }

    public Tuple parse() {
        if (tokens.peek().is(EOF)) return new LinkedTuple();

        Tuple tuple = parseTuple(null, true, false);
        if (tuple.isSingleton()) {
            Arg arg = tuple.firstArg();
            if (arg.isTuple()) return arg.asTuple();
        }

        return tuple;
    }

    private Arg parseOnce(boolean root) {
        Token token = tokens.poll();

        switch (token.getType()) {
            case LINE: {
                return parseOnce(root);
            }
            case LEFT_PAREN: {
                return parseSmartTuple();
            }
            case LEFT_BRACKET: {
                return parseTuple(null, false, false);
            }
            case TEXT: {
                String text = token.getString();

                if (tokens.peek().is(ASSIGN)) {
                    tokens.poll();

                    Arg value = parseOnce(false);

                    if (root && semicolons && implicitTuples && tokens.peek().is(COMMA)) {
                        tokens.poll();
                        value = parseTuple(value, false, false);
                    }

                    if (value.isPair() && !rawPairs) {
                        value = new LinkedTuple(value);
                    }

                    return new Pair(text, value);
                } else {
                    Arg arg = new Text(text);

                    if (root && semicolons && tokens.peek().is(COMMA)) {
                        tokens.poll();
                        return parseTuple(arg, false, false);
                    }

                    return arg;
                }
            }
        }

        throw new SyntaxException("Unexpected " + token, token.getPosition());
    }

    private Arg parseSmartTuple() {
        Tuple tuple = parseTuple(null, false, true);
        if (smartTuples && tuple.isSingleton()) {
            return tuple.firstArg();
        }
        return tuple;
    }

    private Tuple parseTuple(Arg first, boolean root, boolean smart) {
        Tuple tuple = new LinkedTuple();

        boolean implicit = first != null;

        assert !(implicit && root && smart);

        if (implicit) {
            tuple.add(first);
        }

        if (!implicit && !root && tokens.peek().is(smart ? RIGHT_PAREN : RIGHT_BRACKET)) {
            tokens.poll();
            return tuple;
        }

        while (true) {
            tuple.add(parseOnce(!implicit));

            Token token = tokens.poll();
            switch (token.getType()) {
                case RIGHT_PAREN: {
                    if (implicit) {
                        tokens.push(token);
                        return tuple;
                    }
                    if (!root && smart) return tuple;
                    break;
                }

                case RIGHT_BRACKET: {
                    if (implicit) {
                        tokens.push(token);
                        return tuple;
                    }
                    if (!root && !smart) return tuple;
                    break;
                }

                case COMMA: {
                    if (root && semicolons && implicitTuples) break;
                    continue;
                }

                case LINE:
                case SEMICOLON: {
                    if (implicit) {
                        tokens.push(token);
                        return tuple;
                    }

                    if (tokens.peek().is(EOF)) {
                        return tuple;
                    }
                    continue;
                }
                case EOF: {
                    tokens.push(token);
                    if (!implicit && !root) break;
                    return tuple;
                }
            }

            throw new SyntaxException("Unexpected " + token, token.getPosition());
        }
    }
}
