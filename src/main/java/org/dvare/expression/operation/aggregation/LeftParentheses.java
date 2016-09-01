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


package org.dvare.expression.operation.aggregation;

import org.dvare.annotations.Operation;
import org.dvare.annotations.OperationType;
import org.dvare.binding.function.FunctionBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.FunctionExpression;
import org.dvare.expression.NamedExpression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralDataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TypeFinder;

import java.util.Stack;

@Operation(type = OperationType.AGGREGATION, symbols = {"("})
public class LeftParentheses extends AssignOperationExpression {
    public LeftParentheses() {
        super("(");
    }

    public LeftParentheses copy() {
        return new LeftParentheses();
    }


    @Override
    public int parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding aTypeBinding, TypeBinding vTypeBinding) throws ExpressionParseException {

        int i = findNextExpression(tokens, pos + 1, stack, aTypeBinding, vTypeBinding);

        while (!stack.peek().getClass().equals(RightParentheses.class)) {
            //get veriable and put into stack

            i = findNextExpression(tokens, i, stack, aTypeBinding, vTypeBinding);
        }

        if (stack.peek().getClass().equals(RightParentheses.class)) {
            stack.pop();
        }


        // logic set things


        return i;
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding aTypeBinding, TypeBinding vTypeBinding) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (int i = pos; i < tokens.length; i++) {
            String token = tokens[i];

            AggregationOperation op = configurationRegistry.getAggregationOperation(token);
            if (op != null) {
                op = op.copy();
                // we found an operation

                if (op.getClass().equals(RightParentheses.class)) {
                    stack.push(op);
                    return i;
                }


            } else if (configurationRegistry.getFunction(token) != null) {

                FunctionBinding table = configurationRegistry.getFunction(token);
                FunctionExpression tableExpression = new FunctionExpression(token, table);
                stack.add(tableExpression);

            } else if (token.matches("self\\..{1,}|data\\..{1,}")) {
                String name = token.substring(2, token.length());
                DataType type = null;
                if (token.matches("self\\..{1,}+")) {


                    type = TypeFinder.findType(name, aTypeBinding);
                } else if (token.matches("data\\..{1,}")) {
                    type = TypeFinder.findType(name, vTypeBinding);
                }

                if (type != null) {
                    VariableExpression variableExpression = VariableType.getVariableType(name, type);
                    stack.add(variableExpression);
                } else {
                    NamedExpression namedExpression = new NamedExpression(token);
                    stack.add(namedExpression);
                }
            } else if (vTypeBinding.getTypes().containsKey(token)) {
                DataType type = TypeFinder.findType(token, vTypeBinding);
                VariableExpression variableExpression = VariableType.getVariableType(token, type);
                stack.add(variableExpression);

            } else if (!token.equals(",")) {
                String type = LiteralDataType.computeType(token);
                LiteralExpression literalExpression = LiteralType.getLiteralExpression(token, DataType.valueOf(type));
                stack.add(literalExpression);
            }
        }
        return null;
    }

}