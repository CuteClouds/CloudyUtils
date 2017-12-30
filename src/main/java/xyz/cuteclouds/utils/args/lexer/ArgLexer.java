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

package xyz.cuteclouds.utils.args.lexer;

import xyz.cuteclouds.utils.args.SyntaxException;

import java.io.InputStream;
import java.io.Reader;

import static xyz.cuteclouds.utils.args.lexer.TokenType.*;

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
            tokens.add(make(EOF));
            return;
        }

        char c = advance();

        while (Character.isSpaceChar(c) || c == '\t') c = advance();

        switch (c) {

            case '(': {
                push(make(LEFT_PAREN));
                return;
            }
            case ')': {
                push(make(RIGHT_PAREN));
                return;
            }

            case '{': {
                push(make(LEFT_BRACE));
                return;
            }
            case '}': {
                push(make(RIGHT_BRACE));
                return;
            }

            case ',': {
                push(make(COMMA));
                return;
            }
            case ';': {
                push(make(SEMICOLON));
                return;
            }

            case ':':
            case '=': {
                push(make(ASSIGN));
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
                            push(make(LINE));
                    }
                }
                return;
            }

            case '\0':
            case (char) -1: {
                push(make(EOF));
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
        if (lastToken().getType() != EOF) {
            tokens.add(new Token(getPosition(), EOF));
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
                case '{':
                case '}':
                    break loop;

                case '\\': {
                    escapeNext(sb);
                    break;
                }
            }

            sb.append(c);
        }

        back();
        push(make(TEXT, sb.toString().trim()));
    }

    private void readString(char initialQuote, boolean keepQuotes) {
        char c;

        StringBuilder qb = new StringBuilder();
        while (match(initialQuote)) qb.append(initialQuote);
        String remainingQuote = qb.toString();
        boolean rawQuotes = remainingQuote.isEmpty();

        StringBuilder sb = new StringBuilder();

        if (keepQuotes) {
            sb.append(initialQuote).append(remainingQuote);
        }

        loop:
        while (true) {
            c = this.advance();

            switch (c) {
                case '\\': {
                    if (!rawQuotes) {
                        escapeNext(sb);
                    } else {
                        sb.append(c);
                    }
                    continue;
                }

                case 0: {
                    if (!keepQuotes) {
                        throw new SyntaxException("Unterminated string.");
                    }

                    break loop;
                }

                case '\r':
                case '\n': {
                    if (rawQuotes) {
                        if (sb.length() == 0) continue;
                    } else if (!keepQuotes) {
                        throw new SyntaxException("Unterminated string.");
                    }
                }

                default: {
                    if (c == initialQuote && (rawQuotes || match(remainingQuote))) {
                        if (keepQuotes) {
                            sb.append(initialQuote).append(remainingQuote);
                        }
                        break loop;
                    }
                    sb.append(c);
                }
            }
        }

        push(make(TEXT, sb.toString()));
    }
}