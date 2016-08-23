package com.dvare.expression.operation.validation;

import com.dvare.annotations.Operation;
import com.dvare.annotations.OperationType;
import com.dvare.exceptions.parser.ExpressionParseException;
import com.dvare.exceptions.parser.IllegalOperationException;
import com.dvare.expression.Expression;
import com.dvare.expression.literal.ListLiteral;
import com.dvare.expression.literal.LiteralExpression;
import com.dvare.expression.veriable.VariableExpression;
import com.dvare.parser.ExpressionTokenizer;

import java.util.Stack;

@Operation(type = OperationType.VALIDATION, symbols = {"IN", "In", "in"})
public class In extends EqualityOperationExpression {
    public In() {
        super("IN", "In", "in");
    }

    public In copy() {
        return new In();
    }


    @Override
    public int parse(final String[] tokens, int pos, Stack<Expression> stack, Class type) throws ExpressionParseException {
        if (pos - 1 >= 0 && tokens.length >= pos + 1) {


            pos = parseOperands(tokens, pos, stack, type);

            Expression left = this.leftOperand;
            Expression right = this.rightOperand;

            String message = null;

            if (right instanceof VariableExpression) {
                VariableExpression variableExpression = (VariableExpression) right;
                if (!variableExpression.isList()) {
                    message = String.format("List Operation %s not possible on type %s near %s", this.getClass().getSimpleName(), variableExpression.getType().getDataType(), ExpressionTokenizer.toString(tokens, pos + 2));

                }
            } else if (right instanceof LiteralExpression) {
                LiteralExpression literalExpression = (LiteralExpression) right;
                if (!(literalExpression instanceof ListLiteral)) {
                    message = String.format("List Operation %s not possible on type %s near %s", this.getClass().getSimpleName(), literalExpression.getType().getDataType(), ExpressionTokenizer.toString(tokens, pos + 2));

                }
            }

            if (message != null) {
                logger.error(message);
                throw new IllegalOperationException(message);

            }


            if (dataType != null && !isLegalOperation(dataType.getDataType())) {

                String message2 = String.format("Operation %s not possible on type %s near %s", this.getClass().getSimpleName(), left.getClass().getSimpleName(), ExpressionTokenizer.toString(tokens, pos + 2));
                logger.error(message2);
                throw new IllegalOperationException(message2);
            }

            logger.debug("Operation Call Expression : {}", getClass().getSimpleName());

            stack.push(this);
            return pos;
        }
        throw new ExpressionParseException("Cannot assign literal to variable");
    }

}