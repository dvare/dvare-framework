package org.dvare.expression.operation.relational;

import org.dvare.annotations.Operation;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.exceptions.parser.IllegalOperationException;
import org.dvare.expression.Expression;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.literal.ListLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.RelationalOperationExpression;
import org.dvare.expression.veriable.ListVariable;
import org.dvare.expression.veriable.VariableExpression;
import org.dvare.parser.ExpressionTokenizer;

import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.IN)
public class In extends RelationalOperationExpression {
    public In() {
        super(OperationType.IN);
    }


    public In(OperationType operationType) {
        super(operationType);
    }

    @Override
    public Integer parse(final String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        if (pos - 1 >= 0 && tokens.length >= pos + 1) {
            pos = parseOperands(tokens, pos, stack, contexts);
            testInOperation(tokens, pos);
            stack.push(this);
            return pos;
        }
        throw new ExpressionParseException("Cannot assign literal to variable");
    }

    private void testInOperation(String[] tokens, int pos) throws ExpressionParseException {
        /*Expression left = this.leftOperand;*/
        Expression right = this.rightOperand;

        String message = null;

        if (right instanceof VariableExpression && !(right instanceof ListVariable)) {
            VariableExpression<?> variableExpression = (VariableExpression<?>) right;

            message = String.format("List OperationExpression %s not possible on type %s near %s", this.getClass().getSimpleName(), toDataType(variableExpression.getType()), ExpressionTokenizer.toString(tokens, pos + 2));


        } else if (right instanceof LiteralExpression) {
            LiteralExpression<?> literalExpression = (LiteralExpression<?>) right;
            if (!(literalExpression instanceof ListLiteral)) {
                message = String.format("List OperationExpression %s not possible on type %s near %s", this.getClass().getSimpleName(), toDataType(literalExpression.getType()), ExpressionTokenizer.toString(tokens, pos + 2));

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
        }*/
        if (logger.isDebugEnabled()) {
            logger.debug("OperationExpression Call Expression : {}", getClass().getSimpleName());
        }
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}