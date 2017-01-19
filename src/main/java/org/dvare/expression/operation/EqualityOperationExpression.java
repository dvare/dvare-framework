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


    private int expression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes, String token, String type) throws ExpressionParseException {
        Expression expression = null;
        OperationExpression op = ConfigurationRegistry.INSTANCE.getOperation(token);
        if (op != null) {
            op = op.copy();

            pos = op.parse(tokens, pos + 1, stack, selfTypes, dataTypes);

            expression = stack.pop();

        } else if (type != null && type.equals(SELF_ROW) && selfTypes.getTypes().containsKey(token)) {
            DataType variableType = TypeFinder.findType(token, selfTypes);
            expression = VariableType.getVariableType(token, variableType);
        } else if (type != null && type.equals(DATA_ROW) && dataTypes.getTypes().containsKey(token)) {
            DataType variableType = TypeFinder.findType(token, dataTypes);
            expression = VariableType.getVariableType(token, variableType);
        } else {

            if (token.equals("[")) {

                OperationExpression operationExpression = new ListOperationExpression();
                pos = operationExpression.parse(tokens, pos, stack, selfTypes, dataTypes);
                expression = stack.pop();

            } else {

                expression = LiteralType.getLiteralExpression(token);
            }


        }

        stack.push(expression);
        return pos;
    }


    protected int parseOperands(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {

        String leftString = tokens[pos - 1];
        String rightString = tokens[pos + 1];
        pos = pos + 1;

        String typeString = findDataObject(leftString, selfTypes, dataTypes);
        String typeStringTokens[] = typeString.split(":");
        if (typeStringTokens.length == 2) {
            leftString = typeStringTokens[0];
            leftOperandType = typeStringTokens[1];
        }


        typeString = findDataObject(rightString, selfTypes, dataTypes);
        typeStringTokens = typeString.split(":");
        if (typeStringTokens.length == 2) {
            rightString = typeStringTokens[0];
            rightOperandType = typeStringTokens[1];
        }


        // computing expression left side̵

        if (stack.isEmpty()) {
            pos = expression(tokens, pos, stack, selfTypes, dataTypes, leftString, leftOperandType);
        }

        this.leftOperand = stack.pop();

        // computing expression right side̵


        pos = expression(tokens, pos, stack, selfTypes, dataTypes, rightString, rightOperandType);
        this.rightOperand = stack.pop();


        return pos;
    }

    private void validate(Expression left, Expression right, String[] tokens, int pos) throws ExpressionParseException {
        if (left instanceof VariableExpression && right instanceof VariableExpression) {
            VariableExpression vL = (VariableExpression) left;
            VariableExpression vR = (VariableExpression) right;

            if (!vL.getType().getDataType().equals(vR.getType().getDataType())) {
                String message = String.format("%s OperationExpression  not possible between  type %s and %s near %s", this.getClass().getSimpleName(), vL.getType().getDataType(), vR.getType().getDataType(), ExpressionTokenizer.toString(tokens, pos));
                logger.error(message);
                throw new IllegalOperationException(message);
            }

        }


        if (!(left instanceof NullLiteral) && !(right instanceof NullLiteral)) {

            DataType leftDataType = null;
            DataType rightDataType = null;
            if (left instanceof VariableExpression) {

                leftDataType = ((VariableExpression) left).getType().getDataType();

            } else if (left instanceof LiteralExpression) {
                leftDataType = ((LiteralExpression) left).getType().getDataType();
            }


            if (right instanceof VariableExpression) {
                rightDataType = ((VariableExpression) right).getType().getDataType();
            } else if (right instanceof LiteralExpression) {
                rightDataType = ((LiteralExpression) right).getType().getDataType();
            }


            if (leftDataType != null && rightDataType != null) {

                if (leftDataType.equals(DataType.StringType)) {
                    if (!rightDataType.equals(DataType.StringType) && !rightDataType.equals(DataType.RegexType)) {

                        String message = String.format("%s OperationExpression not possible between  type %s and %s near %s", this.getClass().getSimpleName(), leftDataType, rightDataType, ExpressionTokenizer.toString(tokens, pos));
                        logger.error(message);
                        throw new IllegalOperationException(message);

                    }
                } else {

                    if (!leftDataType.equals(rightDataType)) {
                        String message = String.format("%s OperationExpression not possible between  type %s and %s near %s", this.getClass().getSimpleName(), leftDataType, rightDataType, ExpressionTokenizer.toString(tokens, pos));
                        logger.error(message);
                        throw new IllegalOperationException(message);
                    }

                }

            }
        }


        if (dataTypeExpression != null && !isLegalOperation(dataTypeExpression.getDataType())) {
            String message = String.format("OperationExpression %s not possible on type %s at %s", this.getClass().getSimpleName(), dataTypeExpression.getDataType(), ExpressionTokenizer.toString(tokens, pos));
            logger.error(message);
            throw new IllegalOperationException(message);
        }


    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {

        if (pos - 1 >= 0 && tokens.length >= pos + 1) {

            pos = parseOperands(tokens, pos, stack, selfTypes, dataTypes);

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
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
        for (int i = pos; i < tokens.length; i++) {
            OperationExpression op = configurationRegistry.getOperation(tokens[i]);
            if (op != null) {
                op = op.copy();
                i = op.parse(tokens, i, stack, selfTypes, dataTypes);
                return i;

            }
        }
        return pos;
    }


    public void interpretOperand(final Object selfRow, final Object dataRow) throws InterpretException {


        Object leftDataRow = null;
        if (leftOperandType != null && leftOperandType.equals(SELF_ROW)) {
            leftDataRow = selfRow;
        } else if (leftOperandType != null && leftOperandType.equals(OperationExpression.DATA_ROW)) {
            leftDataRow = dataRow;
        } else {
            leftDataRow = selfRow;
        }

        Expression leftExpression = null;
        Expression left = this.leftOperand;
        if (left instanceof OperationExpression) {
            OperationExpression operation = (OperationExpression) left;

            LiteralExpression literalExpression = null;
            if (selfRow != null && dataRow != null) {
                literalExpression = (LiteralExpression) operation.interpret(selfRow, dataRow);
            } else {
                literalExpression = (LiteralExpression) operation.interpret(selfRow);
            }

            if (literalExpression != null) {
                dataTypeExpression = literalExpression.getType();
            }
            leftExpression = literalExpression;

        } else if (left instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) left;
            variableExpression = VariableType.setVariableValue(variableExpression, leftDataRow);
            if (variableExpression != null) {
                dataTypeExpression = variableExpression.getType();
            }
            leftExpression = variableExpression;
        } else if (left instanceof LiteralExpression) {
            LiteralExpression literalExpression = (LiteralExpression) left;
            if (literalExpression != null) {
                dataTypeExpression = literalExpression.getType();
            }
            leftExpression = literalExpression;

        }


        Object rightDataRow = null;
        if (rightOperandType != null && rightOperandType.equals(SELF_ROW)) {
            rightDataRow = selfRow;
        } else if (rightOperandType != null && rightOperandType.equals(OperationExpression.DATA_ROW)) {
            rightDataRow = dataRow;
        } else {
            rightDataRow = selfRow;
        }


        Expression right = this.rightOperand;
        LiteralExpression<?> rightExpression = null;
        if (right instanceof OperationExpression) {
            OperationExpression operation = (OperationExpression) right;
            if (selfRow != null && dataRow != null) {
                rightExpression = (LiteralExpression) operation.interpret(selfRow, dataRow);
            } else {
                rightExpression = (LiteralExpression) operation.interpret(selfRow);
            }
        } else if (right instanceof LiteralExpression) {
            rightExpression = (LiteralExpression<?>) right;
        } else if (right instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) right;
            variableExpression = VariableType.setVariableValue(variableExpression, rightDataRow);
            rightExpression = LiteralType.getLiteralExpression(variableExpression.getValue(), variableExpression.getType());
        }


        if (dataTypeExpression == null && rightExpression != null) {
            dataTypeExpression = rightExpression.getType();
        }

        this.leftValueOperand = leftExpression;
        this.rightValueOperand = rightExpression;

    }


    @Override
    public Object interpret(final Object selfRow, final Object dataRow) throws InterpretException {
        interpretOperand(selfRow, dataRow);

        if (leftValueOperand != null && rightValueOperand != null) {

            LiteralExpression left = toLiteralExpression(leftValueOperand);
            LiteralExpression right = toLiteralExpression(rightValueOperand);

            if (left instanceof NullLiteral || right instanceof NullLiteral) {
                dataTypeExpression = new NullType();
            }

            return dataTypeExpression.compare(this, left, right);

        }
        return false;
    }

    @Override
    public Object interpret(Object dataRow) throws InterpretException {
        interpretOperand(dataRow, null);

        if (leftValueOperand != null && rightValueOperand != null) {

            LiteralExpression left = toLiteralExpression(leftValueOperand);
            LiteralExpression right = toLiteralExpression(rightValueOperand);

            if (left instanceof NullLiteral || right instanceof NullLiteral) {
                dataTypeExpression = new NullType();
            }

            return dataTypeExpression.compare(this, left, right);

        }
        return false;
    }


}