package org.dvare.expression.operation.validation;

import org.dvare.annotations.Operation;
import org.dvare.binding.model.TypeBinding;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.exceptions.parser.IllegalOperationException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.EqualityOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.parser.ExpressionTokenizer;

import java.util.Stack;

@Operation(type = OperationType.BETWEEN, dataTypes = {DataType.FloatType, DataType.IntegerType, DataType.DateTimeType, DataType.DateType})
public class Between extends EqualityOperationExpression {
    public Between() {
        super(OperationType.BETWEEN);
    }

    public Between copy() {
        return new Between();
    }

    @Override
    public Integer parse(final String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {
        if (pos - 1 >= 0 && tokens.length >= pos + 1) {
            pos = parseOperands(tokens, pos, stack, selfTypes, dataTypes);
            testBetweenOperation(tokens, pos);
            stack.push(this);
            return pos;
        }
        throw new ExpressionParseException("Cannot assign literal to variable");
    }

    @Override
    public Integer parse(final String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes) throws ExpressionParseException {
        if (pos - 1 >= 0 && tokens.length >= pos + 1) {
            pos = parseOperands(tokens, pos, stack, selfTypes, null);
            testBetweenOperation(tokens, pos);
            stack.push(this);
            return pos;
        }
        throw new ExpressionParseException("Cannot assign literal to variable");
    }


    private void testBetweenOperation(String[] tokens, int pos) throws ExpressionParseException {

        Expression left = this.leftOperand;
        Expression right = this.rightOperand;

        String message = null;

        if (right instanceof VariableExpression) {
            VariableExpression variableExpression = (VariableExpression) right;
            if (!variableExpression.isList()) {
                message = String.format("Between OperationExpression %s not possible on type %s near %s", this.getClass().getSimpleName(), variableExpression.getType().getDataType(), ExpressionTokenizer.toString(tokens, pos + 2));

            } else {
                if (variableExpression.getListSize() != 2) {
                    message = String.format("OperationExpression %s required 2 values", this.getClass().getSimpleName());
                    logger.error(message);
                    throw new IllegalOperationException(message);
                }
            }
        } else if (right instanceof LiteralExpression) {
            LiteralExpression literalExpression = (LiteralExpression) right;
            if (!(literalExpression instanceof ListLiteral)) {
                message = String.format("List OperationExpression %s not possible on type %s near %s", this.getClass().getSimpleName(), literalExpression.getType().getDataType(), ExpressionTokenizer.toString(tokens, pos + 2));

            } else {
                ListLiteral listLiteral = (ListLiteral) right;
                if (listLiteral.getSize() != 2) {
                    message = String.format("OperationExpression %s required 2 values", this.getClass().getSimpleName());
                    logger.error(message);
                    throw new IllegalOperationException(message);
                }
            }
        }

        if (message != null) {
            logger.error(message);
            throw new IllegalOperationException(message);

        }


        if (dataType != null && !isLegalOperation(dataType.getDataType())) {

            String message2 = String.format("OperationExpression %s not possible on type %s near %s", this.getClass().getSimpleName(), left.getClass().getSimpleName(), ExpressionTokenizer.toString(tokens, pos + 2));
            logger.error(message2);
            throw new IllegalOperationException(message2);
        }

        logger.debug("OperationExpression Call Expression : {}", getClass().getSimpleName());


    }

}