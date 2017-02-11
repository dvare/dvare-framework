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


package org.dvare.expression.operation;

import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.validation.LeftPriority;
import org.dvare.expression.operation.validation.RightPriority;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TypeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Stack;

public abstract class AggregationOperationExpression extends OperationExpression {
    private static Logger logger = LoggerFactory.getLogger(AggregationOperationExpression.class);

    public AggregationOperationExpression(OperationType operationType) {
        super(operationType);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        pos = findNextExpression(tokens, pos + 1, stack, contexts);
        if (!stack.isEmpty()) {
            this.rightOperand = stack.pop();
        }
        stack.push(this);
        return pos;
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
        for (; pos < tokens.length; pos++) {
            OperationExpression op = configurationRegistry.getOperation(tokens[pos]);
            if (op != null) {
                if (op instanceof LeftPriority) {

                    pos = parseArguments(tokens, pos + 1, stack, contexts);

                    while (!stack.peek().getClass().equals(RightPriority.class)) {
                        pos = parseArguments(tokens, pos, stack, contexts);
                    }

                    if (stack.peek().getClass().equals(RightPriority.class)) {
                        stack.pop();
                    }

                    return pos;
                } else {

                    pos = op.parse(tokens, pos, stack, contexts);
                    return pos;
                }
            }
        }
        return pos;
    }


    private Integer parseArguments(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (int i = pos; i < tokens.length; i++) {
            String token = tokens[i];

            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {

                if (op.getClass().equals(RightPriority.class)) {
                    stack.push(op);
                    return i;
                } else {
                    i = op.parse(tokens, i, stack, contexts);
                }
            } else {

                TokenType tokenType = findDataObject(token, contexts);
                if (tokenType.type != null && contexts.getContext(tokenType.type) != null && TypeFinder.findType(tokenType.token, contexts.getContext(tokenType.type)) != null) {
                    TypeBinding typeBinding = contexts.getContext(tokenType.type);
                    DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
                    VariableExpression variableExpression = VariableType.getVariableType(tokenType.token, variableType, tokenType.type);
                    stack.add(variableExpression);
                } else {
                    LiteralExpression literalExpression = LiteralType.getLiteralExpression(token);
                    stack.add(literalExpression);
                }
            }


        }

        throw new ExpressionParseException("Operation Closing Bracket Not Found");
    }

    @Override
    public Object interpret(InstancesBinding instancesBinding) throws InterpretException {


        LiteralExpression leftExpression = null;
        if (leftOperand instanceof LiteralExpression) {
            leftExpression = (LiteralExpression) leftOperand;
        } else {
            leftExpression = new NullLiteral();
        }

        List dataSet = (List) instancesBinding.getInstance("data");
        for (Object bindings : dataSet) {

            Expression right = this.rightOperand;
            LiteralExpression<?> literalExpression = null;
            if (right instanceof OperationExpression) {
                OperationExpression operation = (OperationExpression) right;
                literalExpression = (LiteralExpression) operation.interpret(instancesBinding);
            } else if (right instanceof VariableExpression) {
                VariableExpression variableExpression = (VariableExpression) right;
                variableExpression = VariableType.setVariableValue(variableExpression, bindings);
                literalExpression = LiteralType.getLiteralExpression(variableExpression.getValue(), variableExpression.getType());
            } else if (right instanceof LiteralExpression) {
                literalExpression = (LiteralExpression<?>) right;
            }

            if (literalExpression != null && !(literalExpression instanceof NullLiteral)) {

                leftExpression = literalExpression.getType().evaluate(this, leftExpression, literalExpression);
                logger.debug("Updating value of  by " + leftExpression.getValue());

            } else {
                throw new InterpretException("Literal Expression is null");
            }

        }

        return leftExpression;
    }


}