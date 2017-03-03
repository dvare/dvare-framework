/*The MIT License (MIT)

Copyright (c) 2016-2017 Muhammad Hammad

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Sogiftware.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.*/


package org.dvare.parser;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ExpressionTokenizer {
    private static Logger logger = LoggerFactory.getLogger(ExpressionParser.class);


    public static void main(String args[]) throws IOException {
        //String exp = "V1 in ['A','B'] ; V2 in [2,3] ; V3 in [3.1,3.2] ; V4 in [true,false] ; V5 in [12-05-2016,13-05-2016] ; V6 in [12-05-2016-15:30:00,13-05-2016-15:30:00] ; V7 in [R'B1.*',R'A1.*']";
        String exp = "Variable1 = (7 + 3)" +
                " And Variable1 <> ( 30 - 10)" +
                " And Variable2 = (4 * 5)" +
                " And Variable8 = 'A' " +
                " And Variable1 = ( Variable2 / 2 )" +
                " And Variable1 = ( Variable1 min Variable2 )" +
                " And Variable2 = ( Variable1 max Variable2 )";
        for (String token : toToken(exp)) {

            System.out.println(token);
        }

    }


    // private String operators[] = new String[]{"(", ")", "[", "]", "<>", "||", "&&", "=>", "!=", "<=", ">=", "=", ">", "<", "!", "+"};


    public static String[] toToken(String expr) {
        String splits[] = {"\\s", "\\,", "\\->"};
        return toToken(expr, splits);
    }

    public static String[] toToken(String expr, String splits[]) {


        StringBuilder builder = new StringBuilder("");
        for (String split : splits) {
            builder.append(split);
            builder.append("|");
        }

        String regex = builder.toString().trim();
        if (regex.endsWith("|")) {
            regex = regex.substring(0, regex.length() - 1);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Token Pattern: {}", regex);
        }
        if (expr != null && !expr.isEmpty()) {

            String expression = expr.replaceAll("  ", " ");
            expression = expression.trim();

            if (logger.isDebugEnabled()) {
                logger.debug("Parsing the expression : {}", expression);
            }

            List<String> tokenArray = new ArrayList<>();
            Iterator<String> tokenizer = Arrays.asList(expression.split(regex)).iterator();


            while (tokenizer.hasNext()) {
                String token = tokenizer.next();
                if (validateToken(token)) {

                    tokenArray.addAll(parseToken(token));

                } else {
                    if (!token.isEmpty()) {
                        tokenArray.add(token);
                    }
                }


            }

            if (logger.isDebugEnabled()) {
                logger.debug("tokens: {}", tokenArray);
            }
            return tokenArray.toArray(new String[tokenArray.size()]);
        }

        return null;
    }

    private static boolean validateToken(String token) {

        if (token.length() < 1) {
            return false;
        }

        if (token.contains("[")) {
            return true;
        }
        if (token.contains("]")) {
            return true;
        }

        if (token.contains("(")) {
            return true;
        }
        if (token.contains(")")) {
            return true;
        }
        if (token.contains("<>")) {
            return true;
        }
        if (token.contains("||")) {
            return true;
        }
        if (token.contains("&&")) {
            return true;
        }
        if (token.contains("=>")) {
            return true;
        }
        if (token.contains("!=")) {
            return true;
        }
        if (token.contains("<=")) {
            return true;
        }
        if (token.contains(">=")) {
            return true;
        }
        if (token.contains(":=")) {
            return true;
        }
        if (token.contains("=")) {
            return true;
        }
        if (token.contains(">")) {
            return true;
        }
        if (token.contains("<")) {
            return true;
        }
        if (token.contains("!")) {
            return true;
        }
        /*if (token.contains("+")) {
            return true;
        }
        if (token.contains("-")) {
            return true;
        }
        if (token.contains("*")) {
            return true;
        }
        if (token.contains("/")) {
            return true;
        }
        if (token.contains("^")) {
            return true;
        }*/
        return false;
    }

    private static List<String> parseToken(String token) {
        List<String> tokenArray = new ArrayList<>();
        if (token.contains("(")) {
            tokenArray.addAll(parse(token, "("));
        } else if (token.contains(")")) {
            tokenArray.addAll(parse(token, ")"));
        } else if (token.contains("[")) {
            tokenArray.addAll(parse(token, "["));
        } else if (token.contains("]")) {
            tokenArray.addAll(parse(token, "]"));
        } else if (token.contains("<>")) {
            tokenArray.addAll(parse(token, "<>"));
        } else if (token.contains("||")) {
            tokenArray.addAll(parse(token, "||"));
        } else if (token.contains("&&")) {
            tokenArray.addAll(parse(token, "&&"));
        } else if (token.contains("=>")) {
            tokenArray.addAll(parse(token, "=>"));
        } else if (token.contains("!=")) {
            tokenArray.addAll(parse(token, "!="));
        } else if (token.contains("<=")) {
            tokenArray.addAll(parse(token, "<="));
        } else if (token.contains(">=")) {
            tokenArray.addAll(parse(token, ">="));
        } else if (token.contains(":=")) {
            tokenArray.addAll(parse(token, ":="));
        } else if (token.contains("=")) {
            tokenArray.addAll(parse(token, "="));
        } else if (token.contains(">")) {
            tokenArray.addAll(parse(token, ">"));
        } else if (token.contains("<")) {
            tokenArray.addAll(parse(token, "<"));
        } else if (token.contains("!")) {
            tokenArray.addAll(parse(token, "!"));
        } else if (token.contains("+")) {
            tokenArray.addAll(parse(token, "+"));
        }/* else if (token.contains("-")) {
            tokenArray.addAll(parse(token, "-"));
        } else if (token.contains("*")) {
            tokenArray.addAll(parse(token, "*"));
        } else if (token.contains("/")) {
            tokenArray.addAll(parse(token, "/"));
        } else if (token.contains("^")) {
            tokenArray.addAll(parse(token, "^"));
        } */ else {
            tokenArray.add(token);
        }

        return tokenArray;
    }

    private static List<String> parse(String token, String symbol) {
        List<String> tokenArray = new ArrayList<>();
        while (token.contains(symbol)) {

            if (token.indexOf(symbol) == 0) {

                if (!symbol.isEmpty()) {
                    tokenArray.add(symbol);
                }

                token = token.substring(symbol.length(), token.length()).trim();
            } else {

                String left = token.substring(0, token.indexOf(symbol)).trim();
                tokenArray.addAll(parseToken(left));
                if (!symbol.isEmpty()) {
                    tokenArray.add(symbol);
                }
                token = token.substring(token.indexOf(symbol) + symbol.length(), token.length()).trim();
            }
        }

        if (!token.isEmpty()) {
            tokenArray.addAll(parseToken(token));
        }
        return tokenArray;
    }


    public static String toString(String[] tokens, int pos) {
        StringBuilder stringBuilder = new StringBuilder("");

        if (tokens.length >= pos + 1) {
            pos++;
        }

        for (int i = 0; i < pos; i++) {
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


}
