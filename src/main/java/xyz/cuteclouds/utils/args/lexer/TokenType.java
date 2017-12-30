package xyz.cuteclouds.utils.args.lexer;

public enum TokenType {
    /** Character <b>(</b> */
    LEFT_PAREN('('),
    /** Character <b>)/b> */
    RIGHT_PAREN(')'),

    /** Character <b>[</b> */
    LEFT_BRACKET('['),
    /** Character <b>]</b> */
    RIGHT_BRACKET(']'),

    /** Character <b>,</b> */
    COMMA(','),
    /** Character <b>;</b> */
    SEMICOLON(';'),

    /** Character <b>:</b> or <b>=</b> */
    ASSIGN(':'),

    /** Piece of text */
    TEXT,

    /** A line */
    LINE,
    /** End of file */
    EOF;

    public final Character defaultChar;

    TokenType(char defaultChar) {
        this.defaultChar = defaultChar;
    }

    TokenType() {
        this.defaultChar = null;
    }
}
