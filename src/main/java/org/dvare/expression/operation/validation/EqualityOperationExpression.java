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
import org.dvare.util.TypeFinder;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

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


    protected int parseOperands(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {

        String leftString = tokens[pos - 1];
        String rightString = tokens[pos + 1];
        pos = pos + 1;


        DataType variableType = null;
        if (dataTypes != null && leftString.matches(dataPatten)) {
            leftString = leftString.substring(5, leftString.length());
            // variableType = dataTypes.get(leftString);
            variableType = TypeFinder.findType(leftString, dataTypes);
            leftType = DATA;
        } else {
            if (leftString.matches(selfPatten)) {
                leftString = leftString.substring(5, leftString.length());
            }
            // variableType = selfTypes.get(leftString);
            variableType = TypeFinder.findType(leftString, selfTypes);
            leftType = SELF;
        }


        if (dataTypes != null && rightString.matches(dataPatten)) {
            rightString = rightString.substring(5, rightString.length());
            rightType = DATA;
        } else if (rightString.matches(selfPatten)) {
            rightString = rightString.substring(5, rightString.length());
            rightType = SELF;
        } else {
            rightType = SELF;
        }


        if (variableType == null) {
            if (rightType != null && rightType.equals(SELF) && selfTypes.getTypes().containsKey(rightString)) {
                //  variableType = selfTypes.get(rightString);
                variableType = TypeFinder.findType(rightString, selfTypes);
            } else if (rightType != null && rightType.equals(DATA) && dataTypes.getTypes().containsKey(rightString)) {
                // variableType = dataTypes.get(rightString);
                variableType = TypeFinder.findType(rightString, dataTypes);
            } else {
                variableType = LiteralDataType.computeDataType(rightString);
            }
        }


        Expression left = null;
        if (stack.isEmpty()) {

            if (leftType != null && leftType.equals(SELF) && selfTypes.getTypes().containsKey(leftString)) {
                left = VariableType.getVariableType(leftString, variableType);
            } else if (leftType != null && leftType.equals(DATA) && dataTypes.getTypes().containsKey(leftString)) {
                left = VariableType.getVariableType(leftString, variableType);
            } else {
                left = LiteralType.getLiteralExpression(leftString, variableType);
            }

        } else {
            left = stack.pop();
        }


        Expression right;
        ValidationOperation op = ConfigurationRegistry.INSTANCE.getValidationOperation(rightString);
        if (op != null) {
            op = op.copy();
            pos = op.parse(tokens, pos + 1, stack, selfTypes);
            right = stack.pop();

        } else if (rightType != null && rightType.equals(SELF) && selfTypes.getTypes().containsKey(rightString)) {
            // DataType rightType = selfTypes.get(rightString);
            DataType rightType = TypeFinder.findType(rightString, selfTypes);
            right = VariableType.getVariableType(rightString, rightType);
        } else if (rightType != null && rightType.equals(DATA) && dataTypes.getTypes().containsKey(rightString)) {
            // DataType rightType = dataTypes.get(rightString);
            DataType rightType = TypeFinder.findType(rightString, dataTypes);
            right = VariableType.getVariableType(rightString, rightType);
        } else {

            LiteralExpression literalExpression = null;
            if (rightString.equals("[")) {

                List<String> values = new ArrayList<>();
                while (!tokens[++pos].equals("]")) {
                    String value = tokens[pos];
                    values.add(value);
                }

                literalExpression = LiteralType.getLiteralExpression(values.toArray(new String[values.size()]), variableType);
            } else {
                literalExpression = LiteralType.getLiteralExpression(rightString, variableType);
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
    public int parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {

        if (pos - 1 >= 0 && tokens.length >= pos + 1) {
            pos = parseOperands(tokens, pos, stack, selfTypes, dataTypes);

            Expression left = this.leftOperand;
            Expression right = this.rightOperand;

            if (left instanceof VariableExpression && right instanceof VariableExpression) {
                VariableExpression vL = (VariableExpression) left;
                VariableExpression vR = (VariableExpression) right;

                if (!vL.getType().getDataType().equals(vR.getType().getDataType())) {
                    String message = String.format("%s ValidationOperation  not possible between  type %s and %s near %s", this.getClass().getSimpleName(), vL.getType().getDataType(), vR.getType().getDataType(), ExpressionTokenizer.toString(tokens, pos));
                    logger.error(message);
                    throw new IllegalOperationException(message);
                }

            }


            if (dataType != null && !isLegalOperation(dataType.getDataType())) {
                String message = String.format("ValidationOperation %s not possible on type %s at %s", this.getClass().getSimpleName(), dataType.getDataType(), ExpressionTokenizer.toString(tokens, pos));
                logger.error(message);
                throw new IllegalOperationException(message);
            }


            logger.debug("ValidationOperation Call Expression : {}", getClass().getSimpleName());

            stack.push(this);
            return pos;
        }
        throw new ExpressionParseException("Cannot assign literal to variable");
    }

    @Override
    public int parse(final String[] tokens, int pos, Stack<Expression> stack, TypeBinding typeBinding) throws ExpressionParseException {
        if (pos - 1 >= 0 && tokens.length >= pos + 1) {
            pos = parseOperands(tokens, pos, stack, typeBinding, null);

            Expression left = this.leftOperand;
            Expression right = this.rightOperand;

            if (left instanceof VariableExpression && right instanceof VariableExpression) {
                VariableExpression vL = (VariableExpression) left;
                VariableExpression vR = (VariableExpression) right;

                if (!vL.getType().getDataType().equals(vR.getType().getDataType())) {
                    String message = String.format("%s ValidationOperation  not possible between  type %s and %s near %s", this.getClass().getSimpleName(), vL.getType().getDataType(), vR.getType().getDataType(), ExpressionTokenizer.toString(tokens, pos));
                    logger.error(message);
                    throw new IllegalOperationException(message);
                }

            }


            if (dataType != null && !isLegalOperation(dataType.getDataType())) {
                String message = String.format("ValidationOperation %s not possible on type %s at %s", this.getClass().getSimpleName(), dataType.getDataType(), ExpressionTokenizer.toString(tokens, pos));
                logger.error(message);
                throw new IllegalOperationException(message);
            }


            logger.debug("ValidationOperation Call Expression : {}", getClass().getSimpleName());

            stack.push(this);
            return pos;
        }
        throw new ExpressionParseException("Cannot assign literal to variable");
    }


    public void interpretOperand(final Object selfRow, final Object dataRow) throws InterpretException {


        Object leftDataRow = null;
        if (leftType != null && leftType.equals(SELF)) {
            leftDataRow = selfRow;
        } else if (leftType != null && leftType.equals(DATA)) {
            leftDataRow = dataRow;
        } else {
            leftDataRow = selfRow;
        }

        Expression leftExpression = null;
        Expression left = this.leftOperand;
        if (left instanceof ValidationOperation) {
            ValidationOperation validationOperation = (ValidationOperation) left;

            LiteralExpression literalExpression = null;
            if (selfRow != null && dataRow != null) {
                literalExpression = (LiteralExpression) validationOperation.interpret(selfRow, dataRow);
            } else {
                literalExpression = (LiteralExpression) validationOperation.interpret(selfRow);
            }

            if (literalExpression != null) {
                dataType = literalExpression.getType();
            }
            leftExpression = literalExpression;

        } else if (left instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) left;
            variableExpression = VariableType.setVariableValue(variableExpression, leftDataRow);
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


        Object rightDataRow = null;
        if (rightType != null && rightType.equals(SELF)) {
            rightDataRow = selfRow;
        } else if (rightType != null && rightType.equals(DATA)) {
            rightDataRow = dataRow;
        } else {
            rightDataRow = selfRow;
        }


        Expression right = this.rightOperand;
        LiteralExpression<?> rightExpression = null;
        if (right instanceof ValidationOperation) {
            ValidationOperation validationOperation = (ValidationOperation) right;
            if (selfRow != null && dataRow != null) {
                rightExpression = (LiteralExpression) validationOperation.interpret(selfRow, dataRow);
            } else {
                rightExpression = (LiteralExpression) validationOperation.interpret(selfRow);
            }
        } else if (right instanceof LiteralExpression) {
            rightExpression = (LiteralExpression<?>) right;
        } else if (right instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) right;
            variableExpression = VariableType.setVariableValue(variableExpression, rightDataRow);
            rightExpression = LiteralType.getLiteralExpression(variableExpression.getValue(), variableExpression.getType());
        }


        if (dataType == null && rightExpression != null) {
            dataType = rightExpression.getType();
        }

        this.leftValueOperand = leftExpression;
        this.rightValueOperand = rightExpression;

    }


    @Override
    public Object interpret(final Object selfRow, final Object dataRow) throws InterpretException {
        interpretOperand(selfRow, dataRow);

        Expression leftExpression = leftValueOperand;
        LiteralExpression<?> rightExpression = rightValueOperand;

        if ((leftExpression != null && !(leftExpression instanceof NullLiteral)) && (rightExpression != null && !(rightExpression instanceof NullLiteral))) {
            return dataType.compare(this, leftExpression, rightExpression);

        }
        return false;
    }

    @Override
    public Object interpret(Object dataRow) throws InterpretException {
        interpretOperand(dataRow, null);


        Expression leftExpression = leftValueOperand;
        LiteralExpression<?> rightExpression = rightValueOperand;

        if ((leftExpression != null && !(leftExpression instanceof NullLiteral)) && (rightExpression != null && !(rightExpression instanceof NullLiteral))) {
            return dataType.compare(this, leftExpression, rightExpression);

        }


        return false;

    }
}