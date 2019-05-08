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
package org.dvare.expression.operation.utility;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.DataRow;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.util.ValueFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.PRINT)
public class PrintOperation extends OperationExpression {
    protected static Logger logger = LoggerFactory.getLogger(PrintOperation.class);

    public PrintOperation() {
        super(OperationType.PRINT);
    }

    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        pos = findNextExpression(tokens, pos + 1, stack, contexts);
        if (logger.isDebugEnabled()) {
            logger.debug("Operation Expression Call Expression : {}", getClass().getSimpleName());
        }
        stack.push(this);
        return pos;
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {

        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
        Stack<Expression> localStack = new Stack<>();
        for (int newPos = pos; newPos < tokens.length; newPos++) {
            String token = tokens[newPos];
            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {

                if (op instanceof RightPriority) {
                    this.leftOperand = localStack.pop();
                    return newPos;
                } else if (!(op instanceof LeftPriority)) {

                    newPos = op.parse(tokens, newPos, localStack, contexts);
                }

            } else {
                Expression expression = buildExpression(token, contexts, pos, tokens);
                localStack.add(expression);
            }
        }
        throw new ExpressionParseException(getClass().getSimpleName() + " Closing Bracket Not Found at " + pos);
    }

    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {

        if (leftOperand != null) {

            String key = leftOperand.toString();
            LiteralExpression literalExpression = leftOperand.interpret(instancesBinding);
            Object value = literalExpression.getValue();

            logger.info("Log -> " + key + ": " + value);

            Object instance = instancesBinding.getInstance("log");

            if (instance == null) {
                instance = new DataRow();
            }

            ValueFinder.updateValue(instance, key, value);
            instancesBinding.addInstance("log", instance);

            return literalExpression;
        }

        return new NullLiteral();
    }


    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();


        toStringBuilder.append(operationType.getSymbols().get(0));


        if (leftOperand != null) {
            toStringBuilder.append("(");
            toStringBuilder.append(leftOperand.toString());
            toStringBuilder.append(")");
            toStringBuilder.append(" ");


        } else {
            toStringBuilder.append("( )");
        }

        return toStringBuilder.toString();
    }
}