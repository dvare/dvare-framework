package org.dvare.expression.operation.relational;

import org.dvare.annotations.Operation;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.exceptions.parser.IllegalOperationException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.EqualityOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.veriable.ListVariable;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.parser.ExpressionTokenizer;

import java.util.Stack;

@Operation(type = OperationType.BETWEEN, dataTypes = {DataType.FloatType, DataType.IntegerType, DataType.DateTimeType, DataType.DateType, DataType.SimpleDateType})
public class Between extends EqualityOperationExpression {
    public Between() {
        super(OperationType.BETWEEN);
    }


    @Override
    public Integer parse(final String[] tokens, int pos, Stack<Expression> stack, ExpressionBinding expressionBinding, ContextsBinding contexts) throws ExpressionParseException {
        if (pos - 1 >= 0 && tokens.length >= pos + 1) {
            pos = parseOperands(tokens, pos, stack, expressionBinding, contexts);
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

        if (right instanceof VariableExpression && !(right instanceof ListVariable)) {
            VariableExpression variableExpression = (VariableExpression) right;
            message = String.format("Between OperationExpression %s not possible on type %s near %s", this.getClass().getSimpleName(), toDataType(variableExpression.getType()), ExpressionTokenizer.toString(tokens, pos + 2));
        } else if (right instanceof LiteralExpression) {
            LiteralExpression literalExpression = (LiteralExpression) right;
            if (!(literalExpression instanceof ListLiteral)) {
                message = String.format("List OperationExpression %s not possible on type %s near %s", this.getClass().getSimpleName(), toDataType(literalExpression.getType()), ExpressionTokenizer.toString(tokens, pos + 2));

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

/*

        if (dataTypeExpression != null && !isLegalOperation(dataTypeExpression.getDataType())) {

            String message2 = String.format("OperationExpression %s not possible on type %s near %s", this.getClass().getSimpleName(), left.getClass().getSimpleName(), ExpressionTokenizer.toString(tokens, pos + 2));
            logger.error(message2);
            throw new IllegalOperationException(message2);
        }
*/
        if (logger.isDebugEnabled()) {
            logger.debug("OperationExpression Call Expression : {}", getClass().getSimpleName());
        }

    }

}