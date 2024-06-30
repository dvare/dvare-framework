package org.dvare.expression.operation.utility;

import org.dvare.annotations.Operation;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;

import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.RIGHT_END)
public class RightPriority extends OperationExpression {
    public RightPriority() {
        super(OperationType.RIGHT_END);
    }

    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        return pos;
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        return pos;
    }

    @Override
    public String toString() {
        return operationType.getTokens().get(0);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}