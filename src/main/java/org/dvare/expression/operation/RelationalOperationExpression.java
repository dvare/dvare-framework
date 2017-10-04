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
package org.dvare.expression.operation;

import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.exceptions.parser.IllegalOperationException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.NullType;
import org.dvare.expression.literal.*;
import org.dvare.expression.operation.utility.ExpressionSeparator;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.parser.ExpressionTokenizer;
import org.dvare.util.TypeFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public abstract class RelationalOperationExpression extends OperationExpression {
    protected static Logger logger = LoggerFactory.getLogger(RelationalOperationExpression.class);


    public RelationalOperationExpression(OperationType operationType) {
        super(operationType);
    }


    private boolean isLegalOperation(DataType dataType) {

        Annotation annotation = this.getClass().getAnnotation(org.dvare.annotations.Operation.class);
        if (annotation != null) {
            org.dvare.annotations.Operation operation = (org.dvare.annotations.Operation) annotation;
            DataType dataTypes[] = operation.dataTypes();
            if (Arrays.asList(dataTypes).contains(dataType)) {
                return true;
            }
        }
        return false;
    }


    private int expression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts, TokenType tokenType, Side side)
            throws ExpressionParseException {
        Expression expression;
        OperationExpression op = ConfigurationRegistry.INSTANCE.getOperation(tokenType.token);
        if (op != null) {

            pos = op.parse(tokens, pos + 1, stack, contexts);

            expression = stack.pop();

        } else if (tokenType.type != null && contexts.getContext(tokenType.type) != null && TypeFinder.findType(tokenType.token, contexts.getContext(tokenType.type)) != null) {
            TypeBinding typeBinding = contexts.getContext(tokenType.type);
            DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
            expression = VariableType.getVariableExpression(tokenType.token, variableType, tokenType.type);
        } else {
            expression = LiteralType.getLiteralExpression(tokenType.token);
        }

        if (side.equals(Side.Right)) {
            while (pos + 1 < tokens.length) {
                ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
                OperationExpression nextOpp = configurationRegistry.getOperation(tokens[pos + 1]);
                if (nextOpp instanceof ChainOperationExpression || nextOpp instanceof AggregationOperationExpression) {
                    stack.push(expression);
                    pos = nextOpp.parse(tokens, pos + 1, stack, contexts);
                    expression = stack.pop();
                } else {
                    break;
                }


            }
        }

        stack.push(expression);
        return pos;
    }


    protected int parseOperands(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {

        String leftString = tokens[pos - 1];

        TokenType leftTokenType = findDataObject(leftString, contexts);


        // computing expression left side̵

        if (stack.isEmpty() || stack.peek() instanceof AssignOperationExpression || stack.peek() instanceof ExpressionSeparator) {
            pos = expression(tokens, pos, stack, contexts, leftTokenType, Side.Left);
        }

        this.leftOperand = stack.pop();


        pos = pos + 1; // after equal sign
        String rightString = tokens[pos];


        TokenType rightTokenType = findDataObject(rightString, contexts);


        // computing expression right side̵
        pos = expression(tokens, pos, stack, contexts, rightTokenType, Side.Right);
        this.rightOperand = stack.pop();


        return pos;
    }

    private void validate(Expression left, Expression right, String[] tokens, int pos) throws ExpressionParseException {
        if (left instanceof VariableExpression && right instanceof VariableExpression) {
            VariableExpression vL = (VariableExpression) left;
            VariableExpression vR = (VariableExpression) right;

            if (!toDataType(vL.getType()).equals(toDataType(vR.getType()))) {
                String message = String.format("%s OperationExpression  not possible between  type %s and %s near %s", this.getClass().getSimpleName(), toDataType(vL.getType()), toDataType(vR.getType()), ExpressionTokenizer.toString(tokens, pos));
                logger.error(message);
                throw new IllegalOperationException(message);
            }

        }


        if (!(left instanceof NullLiteral) && !(right instanceof NullLiteral)) {

            DataType leftDataType = null;
            DataType rightDataType = null;
            if (left instanceof VariableExpression) {

                leftDataType = toDataType(((VariableExpression) left).getType());

            } else if (left instanceof LiteralExpression) {
                leftDataType = toDataType(((LiteralExpression) left).getType());
            }


            if (right instanceof VariableExpression) {
                rightDataType = toDataType(((VariableExpression) right).getType());
            } else if (right instanceof LiteralExpression) {
                rightDataType = toDataType(((LiteralExpression) right).getType());
            }


            if (leftDataType != null && rightDataType != null) {

                if (leftDataType.equals(DataType.StringType)) {
                    if (!rightDataType.equals(DataType.StringType) && !rightDataType.equals(DataType.RegexType)) {

                        String message = String.format("%s OperationExpression not possible between  type %s and %s near %s", this.getClass().getSimpleName(), leftDataType, rightDataType, ExpressionTokenizer.toString(tokens, pos));
                        logger.error(message);
                        throw new IllegalOperationException(message);

                    }
                } else {

                    if (!leftDataType.equals(rightDataType) && (leftDataType != DataType.SimpleDateType && leftDataType != DataType.DateType && rightDataType != DataType.DateTimeType)) {
                        String message = String.format("%s OperationExpression not possible between  type %s and %s near %s", this.getClass().getSimpleName(), leftDataType, rightDataType, ExpressionTokenizer.toString(tokens, pos));
                        logger.error(message);
                        throw new IllegalOperationException(message);
                    }

                }

            }


            if (leftDataType != null && !isLegalOperation(leftDataType)) {
                String message = String.format("OperationExpression %s not possible on type %s at %s", this.getClass().getSimpleName(), leftDataType, ExpressionTokenizer.toString(tokens, pos));
                logger.error(message);
                throw new IllegalOperationException(message);
            }


        }


    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {

        if (pos - 1 >= 0 && tokens.length >= pos + 1) {

            pos = parseOperands(tokens, pos, stack, contexts);

            Expression left = this.leftOperand;
            Expression right = this.rightOperand;

            validate(left, right, tokens, pos);
            if (logger.isDebugEnabled()) {
                logger.debug("OperationExpression Call Expression : {}", getClass().getSimpleName());
            }
            stack.push(this);
            return pos;
        }
        throw new ExpressionParseException("Cannot assign literal to variable");
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
        for (; pos < tokens.length; pos++) {
            OperationExpression op = configurationRegistry.getOperation(tokens[pos]);
            if (op != null) {
                pos = op.parse(tokens, pos, stack, contexts);
                return pos;

            }
        }
        return pos;
    }


    protected Expression interpretOperandLeft(InstancesBinding instancesBinding, Expression leftOperand) throws InterpretException {
        return super.interpretOperand(leftOperand, instancesBinding);
    }


    protected Expression interpretOperandRight(InstancesBinding instancesBinding, Expression rightOperand) throws InterpretException {
        LiteralExpression<?> rightExpression = null;
        if (rightOperand instanceof OperationExpression) {
            OperationExpression operation = (OperationExpression) rightOperand;
            rightExpression = (LiteralExpression) operation.interpret(instancesBinding);
        } else if (rightOperand instanceof LiteralExpression) {
            rightExpression = (LiteralExpression<?>) rightOperand;
        } else if (rightOperand instanceof VariableExpression) {

            VariableExpression variableExpression = (VariableExpression) rightOperand;
            Object instance = instancesBinding.getInstance(variableExpression.getOperandType());

            if (instance instanceof List) {
                instance = ((List) instance).isEmpty() ? null : ((List) instance).get(0);
            }
            variableExpression = VariableType.setVariableValue(variableExpression, instance);
            rightExpression = LiteralType.getLiteralExpression(variableExpression.getValue(), variableExpression.getType());
        }

        if (dataTypeExpression == null && rightExpression != null) {
            dataTypeExpression = rightExpression.getType();
        }
        return rightExpression;
    }

    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {

        Expression leftValueOperand = interpretOperandLeft(instancesBinding, leftOperand);
        Expression rightValueOperand = interpretOperandRight(instancesBinding, rightOperand);


        if (leftValueOperand != null && rightValueOperand != null) {

            LiteralExpression leftLiteralExpression = toLiteralExpression(leftValueOperand);
            LiteralExpression rightLiteralExpression = toLiteralExpression(rightValueOperand);

            if (leftLiteralExpression instanceof ListLiteral && rightLiteralExpression instanceof ListLiteral) {

                dataTypeExpression = ((ListLiteral) leftLiteralExpression).getListType();
            }

            if (leftLiteralExpression instanceof NullLiteral || rightLiteralExpression instanceof NullLiteral) {
                dataTypeExpression = NullType.class;
            }


            try {
                return dataTypeExpression.newInstance().evaluate(this, leftLiteralExpression, rightLiteralExpression);
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error(e.getMessage(), e);
            }


        }
        return new BooleanLiteral(false);
    }


}