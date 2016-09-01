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


package org.dvare.parser;

import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.config.RuleConfiguration;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.operation.aggregation.AggregationOperation;
import org.dvare.expression.operation.condition.ConditionOperation;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TypeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Stack;

public class AggregationExpressionParser {


    static Logger logger = LoggerFactory.getLogger(AggregationExpressionParser.class);
    private ConfigurationRegistry configurationRegistry = null;

    public AggregationExpressionParser(RuleConfiguration ruleConfiguration) {
        configurationRegistry = ruleConfiguration.getConfigurationRegistry();
    }


    public Expression fromString(String expr, Map<String, String> vtypes, Map<String, String> atypes) throws ExpressionParseException {
        TypeBinding vTypeBinding = ExpressionParser.translate(vtypes);
        TypeBinding aTypeBinding = ExpressionParser.translate(atypes);
        return fromString(expr, vTypeBinding, aTypeBinding);
    }

    public Expression fromString(String expr, Class type, Class type2) throws ExpressionParseException {

        TypeBinding vtypes = ExpressionParser.translate(type);
        TypeBinding atypes = ExpressionParser.translate(type2);
        return fromString(expr, vtypes, atypes);
    }

    public Expression fromString(String expr, TypeBinding vtypes, TypeBinding atypes) throws ExpressionParseException {

        if (expr != null && !expr.isEmpty()) {
            if ((expr.contains("if") || expr.contains("IF")) && (expr.contains("endif") || expr.contains("ENDIF"))) {
                return fromStringCondtion(expr, vtypes, atypes);
            } else {
                return fromStringSimple(expr, vtypes, atypes);
            }
        } else {
            String message = String.format("Expression is null or Empty");
            logger.error(message);
            throw new ExpressionParseException(message);
        }

    }


    public Expression fromStringSimple(String expr, TypeBinding vtypes, TypeBinding atypes) throws ExpressionParseException {
        Stack<Expression> stack = new Stack<>();

        String[] tokens = ExpressionTokenizer.toToken(expr);

        for (int i = 0; i < tokens.length - 1; i++) {
            if (atypes.getTypes().containsKey(tokens[i])) {
                String name = tokens[i];
                DataType type = TypeFinder.findType(name, atypes);
                VariableExpression variableExpression = VariableType.getVariableType(name, type);
                stack.add(variableExpression);

            } else {

                AggregationOperation op = configurationRegistry.getAggregationOperation(tokens[i]);
                if (op != null) {
                    // create a new instance
                    op = op.copy();
                    i = op.parse(tokens, i, stack, vtypes, atypes);
                }
            }
        }
        if (stack.empty()) {
            throw new ExpressionParseException("Unable to Parse Expression");
        }
        return stack.pop();


    }

    public Expression fromStringCondtion(String expr, TypeBinding vtypes, TypeBinding atypes) throws ExpressionParseException {
        if (expr != null && !expr.isEmpty()) {
            Stack<Expression> stack = new Stack<>();

            String[] tokens = ExpressionTokenizer.toToken(expr);

            for (int i = 0; i < tokens.length - 1; i++) {

                ConditionOperation op = configurationRegistry.getConditionOperation(tokens[i]);
                if (op != null) {
                    op = op.copy();
                    i = op.parse(tokens, i, stack, vtypes, atypes);
                }

            }
            if (stack.empty()) {
                throw new ExpressionParseException("Unable to Parse Expression");
            }
            return stack.pop();
        } else {
            String message = String.format("Expression is null or Empty");
            logger.error(message);
            throw new ExpressionParseException(message);
        }


    }
}