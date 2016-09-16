package org.dvare.expression.operation.validation;

import org.dvare.annotations.OperationType;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.operation.Operation;

import java.util.Stack;

@org.dvare.annotations.Operation(type = OperationType.VALIDATION, symbols = {"("})
public class LeftPriority extends OperationExpression {
    public LeftPriority() {
        super("(");
    }

    public LeftPriority copy() {
        return new LeftPriority();
    }


    @Override
    public int parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding typeBinding) throws ExpressionParseException {

        int i = findNextExpression(tokens, pos + 1, stack, typeBinding);

        while (!stack.peek().getClass().equals(RightPriority.class)) {
            i = findNextExpression(tokens, i + 1, stack, typeBinding);
        }

        if (stack.peek().getClass().equals(RightPriority.class)) {
            stack.pop();
        }

        return i;
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding typeBinding) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (int i = pos; i < tokens.length; i++) {

            Operation op = configurationRegistry.getOperation(tokens[i]);
            if (op != null) {
                op = op.copy();
                // we found an operation

                if (op.getClass().equals(RightPriority.class)) {
                    stack.push(op);
                    return i;

                } else {
                    i = op.parse(tokens, i, stack, typeBinding);
                }


            }
        }
        return null;
    }

}