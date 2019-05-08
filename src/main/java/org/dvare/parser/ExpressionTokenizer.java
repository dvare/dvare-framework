/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Sogiftware.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.dvare.parser;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionTokenizer {
    private static Logger logger = LoggerFactory.getLogger(ExpressionParser.class);
    private static String[] operators = new String[]{"<>", "\\|\\|", "&&", "=>", "!=", "<=", ">=", ":="};
    private static String[] singleOperators = new String[]{"\\(", "\\)", "\\[", "\\]", "=", ">", "<", "!", ";", "\\+", /*"\\-", "\\*", "\\/", "\\^",*/};
    private static Pattern operatorsPattern = Pattern.compile(buildRegex(operators));
    private static Pattern singleOperatorsPattern = Pattern.compile(buildRegex(singleOperators));


    public static String[] toToken(String expr) {
        String[] splits = {"\\s", "\\,", "\\->"};
        return toToken(expr, splits);
    }

    private static String[] toToken(String expr, String[] splits) {
        String regex = buildRegex(splits);
        if (logger.isDebugEnabled()) {
            logger.debug("Token Pattern: {}", regex);
        }
        if (expr != null && !expr.isEmpty()) {

            String expression = expr.replaceAll(" {2}", " ");
            expression = expression.trim();

            if (logger.isDebugEnabled()) {
                logger.debug("Parsing the expression : {}", expression);
            }


            List<Token> tokens = new LinkedList<>();

            Iterator<String> it = Arrays.asList(expression.split(regex)).iterator();
            PeekingIterator<String> iterator = new PeekingIterator<>(it);


            while (iterator.hasNext()) {
                String token = iterator.next();

                if (token.equals("/*") || token.startsWith("/*")) {
                    tokens.addAll(comments(iterator));
                } else if ((countOccurrences(token, '\'') == 1) && (token.startsWith("'") && !token.endsWith("'"))) {
                    tokens.addAll(completeStringLiteral(iterator));
                } else if ((countOccurrences(token, '\'') == 1) && (!token.startsWith("'") && !token.endsWith("'"))) {

                    tokens.addAll(midStartCompeleteStringLiteral(iterator));

                } else if ((countOccurrences(token, '\'') == 2) && !token.startsWith("'") && !token.endsWith("'")) {

                    tokens.addAll(midParseLiteral(iterator));


                } else if ((countOccurrences(token, '\'') == 2) && !token.startsWith("'")) {

                    tokens.addAll(midStartParseStringLiteral(iterator));

                } else if ((countOccurrences(token, '\'') == 2) && !token.endsWith("'")) {
                    tokens.addAll(midEndParseStringLiteral(iterator));
                } else {
                    tokens.addAll(parseToken(token));
                }


            }

            List<String> tokenArray = new ArrayList<>();
            for (Token token : tokens) {
                if (!token.getType().equals(TokenType.COMMENT)) {
                    tokenArray.add(token.getValue());
                }
            }


            if (logger.isDebugEnabled()) {
                logger.debug("tokens: {}", tokenArray);
            }
            return tokenArray.toArray(new String[0]);
        }

        return null;
    }

    private static String buildRegex(String[] splits) {
        StringBuilder regexBuilder = new StringBuilder("");
        Iterator<String> iterator = Arrays.asList(splits).iterator();
        while (iterator.hasNext()) {
            String split = iterator.next();
            regexBuilder.append(split);
            if (iterator.hasNext()) {
                regexBuilder.append("|");
            }
        }
        return regexBuilder.toString();
    }

    private static List<Token> comments(PeekingIterator<String> iterator) {
        List<Token> tokens = new LinkedList<>();

        while (iterator.hasNext()) {
            iterator.next();
            String token = iterator.peek();
            tokens.add(new Token(token, TokenType.COMMENT));
            if (token.equals("*/") || token.endsWith("*/")) {
                break;
            }
        }
        return tokens;
    }


    private static List<Token> completeStringLiteral(PeekingIterator<String> iterator) {
        List<Token> tokens = new LinkedList<>();
        String token = iterator.peek();
        StringBuilder literal = new StringBuilder(token);
        while (iterator.hasNext()) {
            token = iterator.next();
            literal.append(" ").append(token);
            if (!token.startsWith("'") && token.endsWith("'")) {
                tokens.add(new Token(literal.toString(), TokenType.LITERAL));
                break;
            }
        }
        return tokens;
    }


    private static List<Token> midStartCompeleteStringLiteral(PeekingIterator<String> iterator) {
        String token = iterator.peek();
        String left = token.substring(0, token.indexOf("'"));
        left = left.trim();
        List<Token> tokens = new LinkedList<>(parseToken(left));

        token = token.substring(token.indexOf("'"), token.length());
        String tempToken = token.trim();


        while (iterator.hasNext()) {
            token = iterator.next();


            if (!token.startsWith("'") && token.endsWith("'")) {
                tempToken += " " + token;
                tokens.add(new Token(tempToken, TokenType.LITERAL));
            } else if (!token.startsWith("'") && token.contains("'")) {


                left = token.substring(0, token.indexOf("'") + 1);
                left = left.trim();
                tempToken += " " + left;
                tokens.add(new Token(tempToken, TokenType.LITERAL));

                String right = token.substring(token.indexOf("'") + 1, token.length());
                tokens.addAll(parseToken(right));


            } else {
                tempToken += " " + token;
            }

        }

        return tokens;
    }


    private static List<Token> midStartParseStringLiteral(PeekingIterator<String> iterator) {
        String token = iterator.peek();
        String left = token.substring(0, token.indexOf("'"));
        List<Token> tokens = new LinkedList<>(parseToken(left));
        String right = token.substring(token.indexOf("'"), token.length());
        tokens.addAll(parseToken(right));
        return tokens;
    }


    private static List<Token> midEndParseStringLiteral(PeekingIterator<String> iterator) {
        String token = iterator.peek();


        int literalStartPos = token.indexOf("'");
        int literalEndPos = token.indexOf("'", literalStartPos + 1) + 1;


        String left = token.substring(0, literalEndPos);
        List<Token> tokens = new LinkedList<>(parseToken(left));


        String right = token.substring(literalEndPos);
        tokens.addAll(parseToken(right));


        return tokens;
    }


    private static List<Token> midParseLiteral(PeekingIterator<String> iterator) {
        String token = iterator.peek();


        int literalStartPos = token.indexOf("'");
        int literalEndPos = token.indexOf("'", literalStartPos + 1) + 1;


        String left = token.substring(0, literalStartPos);
        List<Token> tokens = new LinkedList<>(parseToken(left));


        String literal = token.substring(literalStartPos, literalEndPos);
        tokens.addAll(parseToken(literal));

        String right = token.substring(literalEndPos, token.length());

        tokens.addAll(parseToken(right));


        return tokens;
    }


    private static int countOccurrences(String haystack, char needle) {
        int count = 0;
        for (int i = 0; i < haystack.length(); i++) {
            if (haystack.charAt(i) == needle) {
                count++;
            }
        }
        return count;
    }


    private static List<Token> parseToken(String token) {
        List<Token> tokens = new ArrayList<>();

        if (token.isEmpty()) {
            return tokens;
        }


        if (!token.startsWith("'") || !token.endsWith("'")) {

            Matcher matcher = operatorsPattern.matcher(token);
            Matcher matcher2 = singleOperatorsPattern.matcher(token);
            if (matcher.find()) {
                tokens.addAll(parse(token, matcher.group()));
            } else if (matcher2.find()) {
                tokens.addAll(parse(token, matcher2.group()));
            } else {
                tokens.add(new Token(token, TokenType.LITERAL));
            }

        } else {
            tokens.add(new Token(token, TokenType.LITERAL));
        }


        return tokens;
    }

    private static List<Token> parse(String token, String operator) {


        int opLength = operator.length();
        int tokenLength = token.length();
        int operatorPos = token.indexOf(operator);

        List<Token> tokens = new ArrayList<>();


        if (token.equals(operator)) {
            tokens.add(new Token(operator, TokenType.OPERATOR));
        } else if (token.indexOf(operator) == 0) {
            tokens.add(new Token(operator, TokenType.OPERATOR));
            String right = token.substring(opLength, tokenLength);
            tokens.addAll(parseToken(right));
        } else {
            String left = token.substring(0, operatorPos);
            tokens.addAll(parseToken(left));
            tokens.add(new Token(operator, TokenType.OPERATOR));
            String right = token.substring(operatorPos + opLength, tokenLength);
            tokens.addAll(parseToken(right));
        }


        return tokens;
    }

    public static String toString(String[] tokens, int pos) {
        return toString(tokens, pos, 0);
    }

    public static String toString(String[] tokens, int pos, int startIndex) {
        StringBuilder stringBuilder = new StringBuilder("");

        if (tokens.length >= pos + 1) {
            pos++;
        }

        for (int i = startIndex; i < pos; i++) {
            stringBuilder.append(tokens[i]);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString().trim();
    }

    public static String toString(String[] tokens) {
        StringBuilder stringBuilder = new StringBuilder("");

        for (String token : tokens) {
            stringBuilder.append(token);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString().trim();
    }


    enum TokenType {
        OPERATOR, COMMENT, NEW_LINE, LITERAL
    }

    static class PeekingIterator<T> implements Iterator<T> {

        private Iterator<T> iterator;
        private T peek;

        public PeekingIterator(Iterator<T> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public T next() {
            peek = iterator.next();
            return peek;
        }

        public T peek() {
            if (peek == null && hasNext()) {
                next();
            }
            return peek;
        }
    }

    static class Token {
        private String value;
        private TokenType type;

        public Token(String value, TokenType type) {
            this.value = value;
            this.type = type;
        }


        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public TokenType getType() {
            return type;
        }

        public void setType(TokenType type) {
            this.type = type;
        }
    }
}

