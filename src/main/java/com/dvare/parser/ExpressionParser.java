/*The MIT License (MIT)

Copyright (c) 2016 Muhammad Hammad

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


package com.dvare.parser;

import com.dvare.config.ConfigurationRegistry;
import com.dvare.config.RuleConfiguration;
import com.dvare.exceptions.parser.ExpressionParseException;
import com.dvare.expression.BooleanExpression;
import com.dvare.expression.Expression;
import com.dvare.expression.operation.validation.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

public class ExpressionParser {

    static Logger logger = LoggerFactory.getLogger(ExpressionParser.class);

    private ConfigurationRegistry configurationRegistry = null;

    public ExpressionParser(RuleConfiguration ruleConfiguration) {
        configurationRegistry = ruleConfiguration.getConfigurationRegistry();
    }


    public Expression fromString(String expr, Class type) throws ExpressionParseException {

        if (expr != null && !expr.isEmpty()) {

            Stack<Expression> stack = new Stack<>();


            String[] tokens = ExpressionTokenizer.toToken(expr);

            if (tokens.length == 1) {
                if (tokens[0].equalsIgnoreCase("true")) {
                    return new BooleanExpression("true", true);
                } else if (tokens[0].equalsIgnoreCase("false")) {
                    return new BooleanExpression("false", false);
                }

            }


            for (int i = 0; i < tokens.length - 1; i++) {

                Operation op = configurationRegistry.getValidationOperation(tokens[i]);
                if (op != null) {
                    // create a new instance
                    op = op.copy();
                    i = op.parse(tokens, i, stack, type);
                }
            }


            if (stack.empty()) {
                throw new ExpressionParseException("Unable to Parse Expression");
            }
            Expression expression = stack.pop();
          /*  if (expression instanceof Operation) {
                Operation operation = (Operation) expression;
              Node<String> root= operation.AST();

                TreePrinter.printNode(root);
            }*/
            return expression;
        } else {
            String message = String.format("Expression is null or Empty");
            logger.error(message);
            throw new ExpressionParseException(message);
        }


    }
}