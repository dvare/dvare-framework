/*The MIT License (MIT)

Copyright (c) 2016-2017 DVARE (Data Validation and Aggregation Rule Engine)

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
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.list.MapOperation;
import org.dvare.expression.operation.list.ValuesOperation;
import org.dvare.expression.operation.utility.GetExpOperation;
import org.dvare.expression.operation.validation.RightPriority;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TypeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public abstract class AggregationOperationExpression extends OperationExpression {
    private static Logger logger = LoggerFactory.getLogger(AggregationOperationExpression.class);
    protected List<Expression> rightOperand = new ArrayList<>();
    protected LiteralExpression leftExpression;

    public AggregationOperationExpression(OperationType operationType) {
        super(operationType);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ExpressionBinding expressionBinding, ContextsBinding contexts) throws ExpressionParseException {


        String token = tokens[pos - 1];
        pos = pos + 1;

        if (stack.isEmpty()) {
            ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
            OperationExpression operationExpression = configurationRegistry.getOperation(token);

            if (operationExpression != null) {
                if (operationExpression instanceof ListOperationExpression) {
                    pos = operationExpression.parse(tokens, pos, stack, expressionBinding, contexts);
                    this.leftOperand = stack.pop();
                }
            } else {
                TokenType tokenType = findDataObject(token, contexts);
                if (tokenType.type != null && contexts.getContext(tokenType.type) != null && TypeFinder.findType(tokenType.token, contexts.getContext(tokenType.type)) != null) {
                    TypeBinding typeBinding = contexts.getContext(tokenType.type);
                    DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
                    this.leftOperand = VariableType.getVariableType(tokenType.token, variableType, tokenType.type);
                } else {
                    throw new ExpressionParseException("Left Operand of Aggregation Operation must be List or Variable ");
                }
            }

        } else {
            this.leftOperand = stack.pop();
        }


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
        for (; pos < tokens.length; pos++) {
            String token = tokens[pos];
            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {

                if (op.getClass().equals(RightPriority.class)) {
                    this.rightOperand = new ArrayList<>(localStack);
                    return pos;
                } else {

                    pos = op.parse(tokens, pos, localStack, expressionBinding, contexts);
                }


            } else {

                localStack.add(buildExpression(token, contexts));

            }
        }
        return pos;
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();

        if (leftOperand != null) {
            toStringBuilder.append(leftOperand.toString());
            toStringBuilder.append(" ");
        }

        toStringBuilder.append(operationType.getSymbols().get(0));
        toStringBuilder.append(" ");

        if (rightOperand != null) {
            toStringBuilder.append(rightOperand.toString());
            toStringBuilder.append(" ");
        }

        return toStringBuilder.toString();
    }


    protected List<Object> buildValues(Expression expression, ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {

        if (expression instanceof ValuesOperation || expression instanceof MapOperation || expression instanceof GetExpOperation) {
            OperationExpression valuesOperation = (OperationExpression) expression;

            Object valuesResult = valuesOperation.interpret(expressionBinding, instancesBinding);

            if (valuesResult instanceof ListLiteral) {
                ListLiteral listLiteral = (ListLiteral) valuesResult;
                List values = listLiteral.getValue();
                dataTypeExpression = listLiteral.getType();
                return values;
            }

        }
        return null;
    }

    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {


        List dataSet = (List) instancesBinding.getInstance("data");
        for (Object bindings : dataSet) {

            Expression right = this.leftOperand;
            LiteralExpression<?> literalExpression = null;

            if (right instanceof OperationExpression) {
                OperationExpression operation = (OperationExpression) right;
                literalExpression = (LiteralExpression) operation.interpret(expressionBinding, instancesBinding);
            } else if (right instanceof VariableExpression) {
                VariableExpression variableExpression = (VariableExpression) right;
                variableExpression = VariableType.setVariableValue(variableExpression, bindings);
                literalExpression = LiteralType.getLiteralExpression(variableExpression.getValue(), variableExpression.getType());
            } else if (right instanceof LiteralExpression) {
                literalExpression = (LiteralExpression<?>) right;
            }

            if (literalExpression != null && !(literalExpression instanceof NullLiteral)) {

                try {
                    leftExpression = literalExpression.getType().newInstance().evaluate(this, leftExpression, literalExpression);
                } catch (InstantiationException | IllegalAccessException e) {
                    logger.error(e.getMessage(), e);
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("Updating value of  by " + leftExpression.getValue());
                }
            } else {
                throw new InterpretException("Literal Expression is null");
            }

        }

        return leftExpression;
    }


}