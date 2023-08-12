package org.dvare.parser;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.exceptions.parser.IllegalLiteralException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExpressionTokenizer {
    private static final Logger logger = LoggerFactory.getLogger(ExpressionParser.class);
    private static final String[] operators = new String[]{"<>", "\\|\\|", "&&", "=>", "!=", "<=", ">=", ":="};
    private static final String[] singleOperators = new String[]{"\\(", "\\)", "\\[", "\\]", "=", ">", "<", "!", ";", "\\+", /*"\\-", "\\*", "\\/", "\\^",*/};
    private static final String[] splits = {"\\s", "\\,", "\\->"};
    private static final Pattern operatorsPattern = Pattern.compile(buildRegex(operators));
    private static final Pattern singleOperatorsPattern = Pattern.compile(buildRegex(singleOperators));
    private static final char stringLiteralNeedle = '\'';
    private static final String stringLiteral = "'";
    private static final String spaceLiteral = " ";

    public static String[] toToken(String expr) throws ExpressionParseException {
        return toToken(expr, splits);
    }

    private static String[] toToken(String expr, String[] splits) throws ExpressionParseException {
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

            List<String> parts = Arrays.asList(expression.split(regex));
            if (logger.isDebugEnabled()) {
                logger.debug("Expression parts : {}", parts);
            }
            List<Token> tokens = toToken(parts);

            List<String> tokenArray = new ArrayList<>();
            for (Token token : tokens) {
                if (!token.getType().equals(Token.TokenType.COMMENT)) {
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

    private static List<Token> toToken(List<String> parts) throws IllegalLiteralException {
        List<Token> tokenList = new LinkedList<>();
        PeekingIterator<String> iterator = new PeekingIterator<>(new ArrayList<>(parts).listIterator());

        while (iterator.hasNext()) {
            String token = iterator.next();
            if (token.isEmpty()) {
                iterator.previous();
                iterator.remove();
                continue;
            }

            int stringLiterals = countOccurrences(token, stringLiteralNeedle);
            if (token.equals("/*") || token.startsWith("/*")) {
                tokenList.addAll(comments(iterator));
            } else if ((stringLiterals == 1) && (
                (token.startsWith(stringLiteral) && !token.endsWith(stringLiteral)) || token.equals(
                    stringLiteral))) {

                if (iterator.hasNext()) {
                    List<Token> allTokens = completeStringLiteral(iterator);
                    if (!allTokens.isEmpty()) {
                        tokenList.addAll(allTokens);
                    } else {
                        String message = "\"" + token + "\" is not an String";
                        throw new IllegalLiteralException(token, message);
                    }
                } else {
                    tokenList.add(new Token(token, Token.TokenType.LITERAL));
                }


            } else if ((stringLiterals == 1) && (!token.startsWith(stringLiteral)
                                                 && !token.endsWith(stringLiteral))) {
                if (iterator.hasNext()) {
                    List<Token> allTokens = midStartCompeleteStringLiteral(iterator);
                    if (!allTokens.isEmpty()) {
                        tokenList.addAll(allTokens);
                    } else {
                        String message = "\"" + token + "\" is not an String";
                        throw new IllegalLiteralException(token, message);
                    }
                } else {
                    tokenList.add(new Token(token, Token.TokenType.LITERAL));
                }

            } else if (
                (stringLiterals != 0 && stringLiterals % 2 == 0) && !token.startsWith(stringLiteral)
                && !token.endsWith(stringLiteral)
                || ((stringLiterals > 2) && token.startsWith(stringLiteral) && token.endsWith(
                    stringLiteral))) {

                addTokensToIteratorOrTokenList(iterator, tokenList, midParseLiteral(iterator));


            } else if ((stringLiterals != 0 && stringLiterals % 2 == 0) && !token.startsWith(
                stringLiteral)) {

                addTokensToIteratorOrTokenList(iterator, tokenList,
                    midStartParseStringLiteral(iterator));

            } else if ((stringLiterals != 0 && stringLiterals % 2 == 0) && !token.endsWith(
                stringLiteral)) {
                addTokensToIteratorOrTokenList(iterator, tokenList,
                    midEndParseStringLiteral(iterator));
            } else {
                addTokensToIteratorOrTokenList(iterator, tokenList, parseToken(token));
            }

        }
        return tokenList;
    }

    private static void addTokensToIteratorOrTokenList(PeekingIterator<String> iterator, List<Token> tokenList, List<Token> parsedTokens) {
        if (parsedTokens.size() == 1 && (
                countOccurrences(parsedTokens.get(0).getValue(), stringLiteralNeedle) == 0 ||
                        countOccurrences(parsedTokens.get(0).getValue(), stringLiteralNeedle) == 2)) {
            tokenList.add(parsedTokens.get(0));
        } else {
            iterator.previous();
            iterator.remove();
            Collections.reverse(parsedTokens);
            for (Token parsedToken : parsedTokens) {
                iterator.add(parsedToken.getValue());
                iterator.previous();
            }
        }

    }

    private static String buildRegex(String[] splits) {
        StringBuilder regexBuilder = new StringBuilder();
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
            tokens.add(new Token(token, Token.TokenType.COMMENT));
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
            if (!token.startsWith(stringLiteral) && (token.endsWith(stringLiteral)) || token.equals(stringLiteral)) {
                tokens.add(new Token(literal.toString(), Token.TokenType.LITERAL));
                break;
            } else if (countOccurrences(literal.toString(), stringLiteralNeedle) == 2) {
                tokens.addAll(midParseLiteral(literal.toString()));
                break;
            }
        }
        return tokens;
    }


    private static List<Token> midStartCompeleteStringLiteral(PeekingIterator<String> iterator) {
        String token = iterator.peek();
        String left = token.substring(0, token.indexOf(stringLiteral));
        left = left.trim();
        List<Token> tokens = new LinkedList<>(parseToken(left));

        token = token.substring(token.indexOf(stringLiteral));
        String tempToken = token.trim();


        while (iterator.hasNext()) {
            token = iterator.next();


            if (!token.startsWith(stringLiteral) && token.endsWith(stringLiteral)) { // case value'
                tempToken += spaceLiteral + token;
                tokens.add(new Token(tempToken, Token.TokenType.LITERAL));
            } else if (!token.startsWith(stringLiteral) && token.contains(stringLiteral)) { //case value'(
                left = token.substring(0, token.indexOf(stringLiteral) + 1);
                left = left.trim();
                tempToken += spaceLiteral + left;
                tokens.add(new Token(tempToken, Token.TokenType.LITERAL));
                String right = token.substring(token.indexOf(stringLiteral) + 1);
                tokens.addAll(parseToken(right));
            } else if (token.startsWith(stringLiteral)) { //case ')
                left = token.substring(0, 1);
                left = left.trim();
                tempToken += spaceLiteral + left;
                tokens.add(new Token(tempToken, Token.TokenType.LITERAL));
                String right = token.substring(1);
                tokens.addAll(parseToken(right));
            } else {
                tempToken += spaceLiteral + token;
            }

        }

        return tokens;
    }


    private static List<Token> midStartParseStringLiteral(PeekingIterator<String> iterator) {
        String token = iterator.peek();
        String left = token.substring(0, token.indexOf(stringLiteral));
        List<Token> tokens = new LinkedList<>(parseToken(left));
        String right = token.substring(token.indexOf(stringLiteral));
        tokens.addAll(parseToken(right));
        return tokens;
    }


    private static List<Token> midEndParseStringLiteral(PeekingIterator<String> iterator) {
        String token = iterator.peek();


        int literalStartPos = token.indexOf(stringLiteral);
        int literalEndPos = token.indexOf(stringLiteral, literalStartPos + 1) + 1;


        String left = token.substring(0, literalEndPos);
        List<Token> tokens = new LinkedList<>(parseToken(left));


        String right = token.substring(literalEndPos);
        tokens.addAll(parseToken(right));


        return tokens;
    }


    private static List<Token> midParseLiteral(PeekingIterator<String> iterator) {
        String token = iterator.peek();
        return midParseLiteral(token);
    }

    private static List<Token> midParseLiteral(String token) {
        int literalStartPos = token.indexOf(stringLiteral);
        int literalEndPos = token.indexOf(stringLiteral, literalStartPos + 1) + 1;


        String left = token.substring(0, literalStartPos);
        List<Token> tokens = new LinkedList<>(parseToken(left));


        String literal = token.substring(literalStartPos, literalEndPos);
        tokens.addAll(parseToken(literal));

        String right = token.substring(literalEndPos);

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


        if (!token.startsWith(stringLiteral) || !token.endsWith(stringLiteral)) {

            Matcher matcher = operatorsPattern.matcher(token);
            Matcher matcher2 = singleOperatorsPattern.matcher(token);
            if (matcher.find()) {
                tokens.addAll(parse(token, matcher.group()));
            } else if (matcher2.find()) {
                tokens.addAll(parse(token, matcher2.group()));
            } else {
                tokens.add(new Token(token, Token.TokenType.LITERAL));
            }

        } else {
            tokens.add(new Token(token, Token.TokenType.LITERAL));
        }


        return tokens;
    }

    private static List<Token> parse(String token, String operator) {


        int opLength = operator.length();
        int tokenLength = token.length();
        int operatorPos = token.indexOf(operator);

        List<Token> tokens = new ArrayList<>();


        if (token.equals(operator)) {
            tokens.add(new Token(operator, Token.TokenType.OPERATOR));
        } else if (token.indexOf(operator) == 0) {
            tokens.add(new Token(operator, Token.TokenType.OPERATOR));
            String right = token.substring(opLength, tokenLength);
            tokens.addAll(parseToken(right));
        } else {
            String left = token.substring(0, operatorPos);
            tokens.addAll(parseToken(left));
            tokens.add(new Token(operator, Token.TokenType.OPERATOR));
            String right = token.substring(operatorPos + opLength, tokenLength);
            tokens.addAll(parseToken(right));
        }


        return tokens;
    }

    public static String toString(String[] tokens, int pos) {
        return toString(tokens, pos, 0);
    }

    public static String toString(String[] tokens, int pos, int startIndex) {
        StringBuilder stringBuilder = new StringBuilder();

        if (startIndex < 0) {
            startIndex = 0;
        }

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
        StringBuilder stringBuilder = new StringBuilder();

        for (String token : tokens) {
            stringBuilder.append(token);
            stringBuilder.append(" ");
        }
        return stringBuilder.toString().trim();
    }


}

