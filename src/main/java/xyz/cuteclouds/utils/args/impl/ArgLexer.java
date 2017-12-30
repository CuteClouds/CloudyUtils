/*
 *  Copyright 2017 An Tran and Adrian Todt
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package xyz.cuteclouds.utils.args.impl;

import xyz.cuteclouds.utils.args.external.Lexer;
import xyz.cuteclouds.utils.args.external.Position;
import xyz.cuteclouds.utils.args.external.SyntaxException;

import java.io.InputStream;
import java.io.Reader;

public class ArgLexer extends Lexer<Token> {

    public ArgLexer(InputStream inputStream) {
        super(inputStream);
    }

    public ArgLexer(String s) {
        super(s);
    }

    public ArgLexer(Reader reader) {
        super(reader);
    }

    public ArgLexer(Reader reader, int historyBuffer) {
        super(reader, historyBuffer);
    }

    @Override
    protected void readTokens() {
        if (!hasNext()) {
            tokens.add(make(TokenType.EOF));
            return;
        }

        char c = advance();

        while (Character.isSpaceChar(c) || c == '\t') c = advance();

        switch (c) {

            case '(': {
                push(make(TokenType.LEFT_PAREN));
                return;
            }
            case ')': {
                push(make(TokenType.RIGHT_PAREN));
                return;
            }

            case '[': {
                push(make(TokenType.LEFT_BRACKET));
                return;
            }
            case ']': {
                push(make(TokenType.RIGHT_BRACKET));
                return;
            }

            case ',': {
                push(make(TokenType.COMMA));
                return;
            }
            case ';': {
                push(make(TokenType.SEMICOLON));
                return;
            }

            case ':':
            case '=': {
                push(make(TokenType.ASSIGN));
                return;
            }

            case '"':
            case '\'': {
                readString(c, false);
                return;
            }

            case '`': {
                readString(c, true);
                return;
            }

            case '\r':
            case '\n': {
                if (!tokens.isEmpty()) {
                    switch (lastToken().getType()) {
                        case LINE:
                        case SEMICOLON:
                        case COMMA:
                        case ASSIGN:
                            return;
                        default:
                            push(make(TokenType.LINE));
                    }
                }
                return;
            }

            case '\0':
            case (char) -1: {
                push(make(TokenType.EOF));
                return;
            }

            default: {
                back();
                readName();
            }
        }
    }

    @Override
    protected void afterReading() {
        if (lastToken().getType() != TokenType.EOF) {
            tokens.add(new Token(getPosition(), TokenType.EOF));
        }
    }

    private void escapeNext(StringBuilder sb) {
        char c = this.advance();
        switch (c) {
            case 'b':
                sb.append('\b');
                break;
            case 't':
                sb.append('\t');
                break;
            case 'n':
                sb.append('\n');
                break;
            case 'f':
                sb.append('\f');
                break;
            case 'r':
                sb.append('\r');
                break;
            case 'u':
                try {
                    sb.append((char) Integer.parseInt(this.advance(4), 16));
                } catch (NumberFormatException e) {
                    throw new SyntaxException("Illegal escape.", e);
                }
                break;
            default:
                sb.append(c);
                break;
        }
    }

    private Token make(TokenType type, String value) {
        return make(new Position(index - value.length(), line, lineIndex - value.length()), type, value);
    }

    private Token make(Position position, TokenType type) {
        return new Token(position, type);
    }

    private Token make(Position position, TokenType type, String value) {
        return new Token(position, type, value);
    }

    private Token make(TokenType type) {
        return make(new Position(index, line, lineIndex), type);
    }

    private void readName() {
        StringBuilder sb = new StringBuilder();

        char c;
        loop:
        while (true) {
            c = advance();

            switch (c) {
                case 0:
                case '\r':
                case '\n':
                case ':':
                case '=':
                case ',':
                case ';':
                case '(':
                case ')':
                case '[':
                case ']':
                    break loop;

                case '\\': {
                    escapeNext(sb);
                    break;
                }
            }

            sb.append(c);
        }

        back();
        push(make(TokenType.TEXT, sb.toString().trim()));
    }

    private void readString(char initialQuote, boolean keepQuotes) {
        char c;

        StringBuilder qb = new StringBuilder();
        while (match(initialQuote)) qb.append(initialQuote);
        String remainingQuote = qb.toString();

        StringBuilder sb = new StringBuilder();

        if (keepQuotes) {
            sb.append(initialQuote).append(remainingQuote);
        }

        while (true) {
            c = this.advance();
            switch (c) {
                case '\\': {
                    escapeNext(sb);
                    break;
                }

                case 0: {
                    if (!keepQuotes) {
                        throw new SyntaxException("Unterminated string.");
                    }

                    push(make(TokenType.TEXT, sb.toString()));
                }
                case '\r':
                case '\n': {
                    if (remainingQuote.isEmpty() || !keepQuotes) {
                        throw new SyntaxException("Unterminated string.");
                    }
                }
                default:
                    if (c == initialQuote && (remainingQuote.isEmpty() || match(remainingQuote))) {
                        if (keepQuotes) {
                            sb.append(initialQuote).append(remainingQuote);
                        }
                        push(make(TokenType.TEXT, sb.toString()));
                        return;
                    }
                    sb.append(c);
            }
        }
    }
}