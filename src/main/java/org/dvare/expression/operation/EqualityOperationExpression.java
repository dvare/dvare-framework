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
import org.dvare.exceptions.parser.IllegalOperationException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.NullType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
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

public abstract class EqualityOperationExpression extends OperationExpression {
    protected static Logger logger = LoggerFactory.getLogger(EqualityOperationExpression.class);


    public EqualityOperationExpression(OperationType operationType) {
        super(operationType);
    }


    protected boolean isLegalOperation(DataType dataType) {

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


    private int expression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts, TokenType tokenType) throws ExpressionParseException {
        Expression expression;
        OperationExpression op = ConfigurationRegistry.INSTANCE.getOperation(tokenType.token);
        if (op != null) {


            pos = op.parse(tokens, pos + 1, stack, contexts);

            expression = stack.pop();


        } else if (tokenType.type != null && contexts.getContext(tokenType.type) != null && TypeFinder.findType(tokenType.token, contexts.getContext(tokenType.type)) != null) {

            TypeBinding typeBinding = contexts.getContext(tokenType.type);

            DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
            expression = VariableType.getVariableType(tokenType.token, variableType, tokenType.type);

        } else {
            expression = LiteralType.getLiteralExpression(tokenType.token);
        }


        while (pos + 1 < tokens.length) {
            ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
            OperationExpression testOp = configurationRegistry.getOperation(tokens[pos + 1]);
            if (testOp instanceof ChainOperationExpression) {
                stack.push(expression);
                pos = testOp.parse(tokens, pos + 1, stack, contexts);
                expression = stack.pop();
            }

            break;
        }

        stack.push(expression);
        return pos;
    }


    protected int parseOperands(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {

        String leftString = tokens[pos - 1];


        TokenType leftTokenType = findDataObject(leftString, contexts);


        // computing expression left side̵

        if (stack.isEmpty()) {
            pos = expression(tokens, pos, stack, contexts, leftTokenType);
        }

        this.leftOperand = stack.pop();


        pos = pos + 1; // after equal sign
        String rightString = tokens[pos];


        TokenType rightTokenType = findDataObject(rightString, contexts);


        // computing expression right side̵
        pos = expression(tokens, pos, stack, contexts, rightTokenType);
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

            logger.debug("OperationExpression Call Expression : {}", getClass().getSimpleName());

            stack.push(this);
            return pos;
        }
        throw new ExpressionParseException("Cannot assign literal to variable");
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
        for (int i = pos; i < tokens.length; i++) {
            OperationExpression op = configurationRegistry.getOperation(tokens[i]);
            if (op != null) {

                i = op.parse(tokens, i, stack, contexts);
                return i;

            }
        }
        return pos;
    }


    public void interpretOperand(InstancesBinding instancesBinding) throws InterpretException {

        Expression leftExpression = super.interpretOperand(leftOperand, instancesBinding);

        Expression right = this.rightOperand;
        LiteralExpression<?> rightExpression = null;
        if (right instanceof OperationExpression) {
            OperationExpression operation = (OperationExpression) right;
            rightExpression = (LiteralExpression) operation.interpret(instancesBinding);
        } else if (right instanceof LiteralExpression) {
            rightExpression = (LiteralExpression<?>) right;
        } else if (right instanceof VariableExpression) {

            VariableExpression variableExpression = (VariableExpression) right;
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

        this.leftValueOperand = leftExpression;
        this.rightValueOperand = rightExpression;

    }


    @Override
    public Object interpret(InstancesBinding instancesBinding) throws InterpretException {

        interpretOperand(instancesBinding);

        if (leftValueOperand != null && rightValueOperand != null) {

            LiteralExpression left = toLiteralExpression(leftValueOperand);
            LiteralExpression right = toLiteralExpression(rightValueOperand);

            if (left instanceof NullLiteral || right instanceof NullLiteral) {
                dataTypeExpression = NullType.class;
            }


            try {
                return dataTypeExpression.newInstance().compare(this, left, right);
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error(e.getMessage(), e);
            }


        }
        return false;
    }


}