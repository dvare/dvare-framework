package org.dvare.expression.operation.utility;

import org.dvare.annotations.Operation;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;

import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.LEFT_PRIORITY)
public class LeftPriority extends OperationExpression {
    public LeftPriority() {
        super(OperationType.LEFT_PRIORITY);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {

        pos = findNextExpression(tokens, pos + 1, stack, contexts);

        while (!stack.peek().getClass().equals(RightPriority.class)) {
            pos = findNextExpression(tokens, pos + 1, stack, contexts);
        }

        if (stack.peek().getClass().equals(RightPriority.class)) {
            stack.pop();
        }

        return pos;
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (; pos < tokens.length; pos++) {

            OperationExpression op = configurationRegistry.getOperation(tokens[pos]);
            if (op != null) {


                if (op.getClass().equals(RightPriority.class)) {
                    stack.push(op);
                    return pos;

                } else {
                    pos = op.parse(tokens, pos, stack, contexts);
                }


            }
        }
        return pos;
    }
}