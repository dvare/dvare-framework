package com.dvare.expression.operation.validation;

import com.dvare.annotations.OperationType;
import com.dvare.config.ConfigurationRegistry;
import com.dvare.exceptions.parser.ExpressionParseException;
import com.dvare.expression.Expression;

import java.util.Stack;

@com.dvare.annotations.Operation(type = OperationType.VALIDATION, symbols = {"("})
public class LeftPriority extends OperationExpression {
    public LeftPriority() {
        super("(");
    }

    public LeftPriority copy() {
        return new LeftPriority();
    }


    @Override
    public int parse(String[] tokens, int pos, Stack<Expression> stack, Class type) throws ExpressionParseException {

        int i = findNextExpression(tokens, pos + 1, stack, type);

        while (!stack.peek().getClass().equals(RightPriority.class)) {
            i = findNextExpression(tokens, i + 1, stack, type);
        }

        if (stack.peek().getClass().equals(RightPriority.class)) {
            stack.pop();
        }

        return i;
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, Class type) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (int i = pos; i < tokens.length; i++) {

            Operation op = configurationRegistry.getValidationOperation(tokens[i]);
            if (op != null) {
                op = op.copy();
                // we found an operation

                if (op.getClass().equals(RightPriority.class)) {
                    stack.push(op);
                    return i;

                } else {
                    i = op.parse(tokens, i, stack, type);
                }


            }
        }
        return null;
    }

}