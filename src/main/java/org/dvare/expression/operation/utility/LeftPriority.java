package org.dvare.expression.operation.utility;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;

import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.PRIORITY_START)
public class LeftPriority extends OperationExpression {
    public LeftPriority() {
        super(OperationType.PRIORITY_START);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {

        pos = findNextExpression(tokens, pos + 1, stack, contexts);

        while (!stack.peek().getClass().equals(RightPriority.class)) {
            pos = findNextExpression(tokens, pos + 1, stack, contexts);
        }

        if (stack.peek().getClass().equals(RightPriority.class)) {
            // pop RightPriority
            this.rightOperand = stack.pop();
            // pop contents of parenthesis
            this.leftOperand = stack.pop();
            stack.push(this);
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

    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        return interpretOperand(leftOperand, instancesBinding);
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();

        // left parenthesis
        toStringBuilder.append(operationType.getTokens().get(0));
        toStringBuilder.append(" ");

        // parenthesis content
        if (leftOperand != null) {
            toStringBuilder.append(leftOperand.toString());
            toStringBuilder.append(" ");
        }

        // right parenthesis
        if (rightOperand != null) {
            toStringBuilder.append(rightOperand.toString());
            toStringBuilder.append(" ");
        }

        return toStringBuilder.toString();
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }
}