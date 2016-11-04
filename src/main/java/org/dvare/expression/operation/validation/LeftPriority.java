package org.dvare.expression.operation.validation;

import org.dvare.annotations.Operation;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.ValidationOperationExpression;

import java.util.Stack;

@Operation(type = OperationType.LEFT_PRIORITY)
public class LeftPriority extends ValidationOperationExpression {
    public LeftPriority() {
        super(OperationType.LEFT_PRIORITY);
    }

    public LeftPriority copy() {
        return new LeftPriority();
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding typeBinding) throws ExpressionParseException {

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
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {

        int i = findNextExpression(tokens, pos + 1, stack, selfTypes, dataTypes);

        while (!stack.peek().getClass().equals(RightPriority.class)) {
            i = findNextExpression(tokens, i + 1, stack, selfTypes, dataTypes);
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

            OperationExpression op = configurationRegistry.getOperation(tokens[i]);
            if (op != null) {
                op = op.copy();

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

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, TypeBinding selfTypes, TypeBinding dataTypes) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;

        for (int i = pos; i < tokens.length; i++) {

            OperationExpression op = configurationRegistry.getOperation(tokens[i]);
            if (op != null) {
                op = op.copy();

                if (op.getClass().equals(RightPriority.class)) {
                    stack.push(op);
                    return i;

                } else {
                    i = op.parse(tokens, i, stack, selfTypes, dataTypes);
                }


            }
        }
        return null;
    }
}