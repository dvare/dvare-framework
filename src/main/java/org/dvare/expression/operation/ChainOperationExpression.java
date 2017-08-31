/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Sogiftware.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package org.dvare.expression.operation;

import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.utility.RightPriority;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TypeFinder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public abstract class ChainOperationExpression extends OperationExpression {

    protected List<Expression> rightOperand = new ArrayList<>();

    public ChainOperationExpression(OperationType operationType) {
        super(operationType);
    }


    private int parseOperands(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {

        String token = tokens[pos - 1];
        pos = pos + 1;


        if (stack.isEmpty()) {


            TokenType tokenType = findDataObject(token, contexts);

            if (tokenType.type != null && contexts.getContext(tokenType.type) != null && TypeFinder.findType(tokenType.token, contexts.getContext(tokenType.type)) != null) {

                TypeBinding typeBinding = contexts.getContext(tokenType.type);
                DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
                this.leftOperand = VariableType.getVariableExpression(tokenType.token, variableType, tokenType.type);

            } else {

                this.leftOperand = LiteralType.getLiteralExpression(token);
            }


        } else {
            this.leftOperand = stack.pop();


        }

        return pos;
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ExpressionBinding expressionBinding, ContextsBinding contexts) throws ExpressionParseException {
        pos = parseOperands(tokens, pos, stack, contexts);
        pos = findNextExpression(tokens, pos + 1, stack, expressionBinding, contexts);
        if (logger.isDebugEnabled()) {
            logger.debug("Operation Expression Call Expression : {}", getClass().getSimpleName());
        }
        stack.push(this);
        return pos;
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ExpressionBinding expressionBinding, ContextsBinding contexts) throws ExpressionParseException {

        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
        Stack<Expression> localStack = new Stack<>();
        for (int newPos = pos; newPos < tokens.length; newPos++) {
            String token = tokens[newPos];
            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {

                if (op.getClass().equals(RightPriority.class)) {
                    this.rightOperand = new ArrayList<>(localStack);
                    return newPos;
                } else {

                    newPos = op.parse(tokens, newPos, localStack, expressionBinding, contexts);
                }


            } else {


                Expression expression = buildExpression(token, contexts);
                localStack.add(expression);


            }
        }
        throw new ExpressionParseException(getClass().getSimpleName() + " Closing Bracket Not Found at " + pos);
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();

        if (leftOperand != null) {
            toStringBuilder.append(leftOperand.toString());
            toStringBuilder.append(" -> ");
        }

        toStringBuilder.append(operationType.getSymbols().get(0));


        if (rightOperand != null) {
            toStringBuilder.append("(");
            Iterator<Expression> expressionIterator = rightOperand.iterator();
            while (expressionIterator.hasNext()) {
                Expression expression = expressionIterator.next();
                toStringBuilder.append(expression.toString());
                if (expressionIterator.hasNext()) {
                    toStringBuilder.append(", ");
                }
            }
            toStringBuilder.append(")");
            toStringBuilder.append(" ");


        } else {
            toStringBuilder.append(" ");
        }

        return toStringBuilder.toString();
    }

}