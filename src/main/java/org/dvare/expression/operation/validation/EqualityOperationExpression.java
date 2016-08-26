package org.dvare.expression.operation.validation;

import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.exceptions.parser.IllegalOperationException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.datatype.DataTypeExpression;
import org.dvare.expression.literal.LiteralDataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.expression.veriable.VariableType;
import org.dvare.parser.ExpressionTokenizer;

import java.lang.annotation.Annotation;
import java.util.*;

public abstract class EqualityOperationExpression extends OperationExpression {

    protected DataTypeExpression dataType;
    protected Expression leftValueOperand;
    protected LiteralExpression rightValueOperand;

    public EqualityOperationExpression(String symbol) {
        this.symbols.add(symbol);
    }

    public EqualityOperationExpression(List<String> symbols) {
        this.symbols.addAll(symbols);
    }

    public EqualityOperationExpression(String... symbols) {
        this.symbols.addAll(Arrays.asList(symbols));
    }

    protected boolean isLegalOperation(DataType dataType) {

        Annotation annotation = this.getClass().getAnnotation(org.dvare.annotations.Operation.class);
        if (annotation != null && annotation instanceof org.dvare.annotations.Operation) {
            org.dvare.annotations.Operation operation = (org.dvare.annotations.Operation) annotation;
            DataType dataTypes[] = operation.dataTypes();
            if (Arrays.asList(dataTypes).contains(dataType)) {
                return true;
            }
        }
        return false;
    }


    protected int parseOperands(String[] tokens, int pos, Stack<Expression> stack, TypeBinding typeBinding) throws ExpressionParseException {

        String leftString = tokens[pos - 1];
        String rightString = tokens[pos + 1];
        pos = pos + 1;


        boolean leftVeriable = false;
        boolean rightVeriable = false;

        DataType leftType = findType(leftString, typeBinding);
        if (leftType != null) {
            leftVeriable = true;
        } else {
            leftType = LiteralDataType.computeDataType(rightString);
        }


        DataType rightType = findType(rightString, typeBinding);
        if (rightType != null) {
            rightVeriable = true;
        } else {
            rightType = LiteralDataType.computeDataType(rightString);
        }


        if (leftType == null && rightType != null) {
            leftType = rightType;
        } else if (rightType == null && leftType != null) {
            rightType = leftType;
        } else if (leftType == null && rightType == null) {
            throw new ExpressionParseException("Variable Type not Found");
        }

        Map<String, String> types = null;


        Expression left = null;
        if (stack.isEmpty()) {

            if (leftVeriable) {
                left = VariableType.getVariableType(leftString, leftType);
            } else {
                left = LiteralType.getLiteralExpression(leftString, leftType);
            }

        } else {
            left = stack.pop();
        }

        Expression right;
        Operation op = ConfigurationRegistry.INSTANCE.getValidationOperation(rightString);
        if (op != null) {
            op = op.copy();
            pos = op.parse(tokens, pos + 1, stack, typeBinding);
            right = stack.pop();

        } else if (rightVeriable) {
            right = VariableType.getVariableType(rightString, rightType);
        } else {

            LiteralExpression literalExpression = null;
            if (rightString.equals("[")) {
                List<String> values = new ArrayList<>();


                while (!tokens[++pos].equals("]")) {
                    String value = tokens[pos];
                    values.add(value);
                }

                literalExpression = LiteralType.getLiteralExpression(values.toArray(new String[values.size()]), rightType);
            } else {

                literalExpression = LiteralType.getLiteralExpression(rightString, rightType);
            }

            right = literalExpression;

        }

        this.leftOperand = left;
        this.rightOperand = right;

        if (left instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) left;
            dataType = variableExpression.getType();
        } else if (left instanceof LiteralExpression) {
            LiteralExpression literalExpression = (LiteralExpression) left;
            dataType = literalExpression.getType();
        }


        return pos;
    }


    @Override
    public int parse(final String[] tokens, int pos, Stack<Expression> stack, TypeBinding typeBinding) throws ExpressionParseException {
        if (pos - 1 >= 0 && tokens.length >= pos + 1) {
            pos = parseOperands(tokens, pos, stack, typeBinding);

            Expression left = this.leftOperand;
            Expression right = this.rightOperand;

            if (left instanceof VariableExpression && right instanceof VariableExpression) {
                VariableExpression vL = (VariableExpression) left;
                VariableExpression vR = (VariableExpression) right;

                if (!vL.getType().getDataType().equals(vR.getType().getDataType())) {
                    String message = String.format("%s Operation  not possible between  type %s and %s near %s", this.getClass().getSimpleName(), vL.getType().getDataType(), vR.getType().getDataType(), ExpressionTokenizer.toString(tokens, pos));
                    logger.error(message);
                    throw new IllegalOperationException(message);
                }

            }


            if (dataType != null && !isLegalOperation(dataType.getDataType())) {
                String message = String.format("Operation %s not possible on type %s at %s", this.getClass().getSimpleName(), dataType.getDataType(), ExpressionTokenizer.toString(tokens, pos));
                logger.error(message);
                throw new IllegalOperationException(message);
            }


            logger.debug("Operation Call Expression : {}", getClass().getSimpleName());

            stack.push(this);
            return pos;
        }
        throw new ExpressionParseException("Cannot assign literal to variable");
    }


    public void interpretOperand(Object object) throws InterpretException {

        Expression left = this.leftOperand;
        Expression leftExpression = null;
        if (left instanceof Operation) {
            Operation operation = (Operation) left;
            LiteralExpression literalExpression = (LiteralExpression) operation.interpret(object);
            if (literalExpression != null) {
                dataType = literalExpression.getType();
            }
            leftExpression = literalExpression;

        } else if (left instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) left;
            variableExpression = VariableType.setVariableValue(variableExpression, object);
            if (variableExpression != null) {
                dataType = variableExpression.getType();
            }
            leftExpression = variableExpression;
        } else if (left instanceof LiteralExpression) {
            LiteralExpression literalExpression = (LiteralExpression) left;
            if (literalExpression != null) {
                dataType = literalExpression.getType();
            }
            leftExpression = literalExpression;

        }


        Expression right = this.rightOperand;
        LiteralExpression<?> rightExpression = null;
        if (right instanceof Operation) {
            Operation operation = (Operation) right;
            rightExpression = (LiteralExpression) operation.interpret(object);

        } else if (right instanceof LiteralExpression) {
            rightExpression = (LiteralExpression<?>) right;
        } else if (right instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) right;
            variableExpression = VariableType.setVariableValue(variableExpression, object);
            rightExpression = LiteralType.getLiteralExpression(variableExpression.getValue(), variableExpression.getType());
        }


        if (dataType == null && rightExpression != null) {
            dataType = rightExpression.getType();
        }

        this.leftValueOperand = leftExpression;
        this.rightValueOperand = rightExpression;

    }


    @Override
    public Object interpret(Object object) throws InterpretException {
        interpretOperand(object);


        Expression leftExpression = leftValueOperand;
        LiteralExpression<?> rightExpression = rightValueOperand;

        if ((leftExpression != null && !(leftExpression instanceof NullLiteral)) && (rightExpression != null && !(rightExpression instanceof NullLiteral))) {
            return dataType.compare(this, leftExpression, rightExpression);

        }


        return false;

    }
}