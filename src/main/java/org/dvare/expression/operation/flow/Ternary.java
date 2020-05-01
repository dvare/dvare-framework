package org.dvare.expression.operation.flow;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.exceptions.parser.IllegalPropertyException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.BooleanLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.*;
import org.dvare.expression.operation.predefined.ToBoolean;
import org.dvare.expression.operation.utility.ExpressionSeparator;
import org.dvare.expression.operation.utility.LeftPriority;
import org.dvare.expression.operation.utility.RightPriority;
import org.dvare.expression.veriable.BooleanVariable;
import org.dvare.parser.ExpressionTokenizer;

import java.util.Stack;


@Operation(type = OperationType.TERNARY)
public class Ternary extends ConditionOperationExpression {

    public Ternary() {
        super(OperationType.TERNARY);
    }

    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        if (pos - 1 >= 0 && tokens.length >= pos + 1) {

            if (stack.isEmpty() || stack.peek() instanceof AssignOperationExpression || stack.peek() instanceof ExpressionSeparator) {

                if (!stack.isEmpty() && stack.peek() instanceof AssignOperationExpression) {
                    AssignOperationExpression expression = (AssignOperationExpression) stack.peek();
                    this.condition = expression.getRightOperand();
                } else {
                    String token = tokens[pos - 1];
                    this.condition = buildExpression(token, contexts, pos - 1, tokens);
                }


            } else {
                this.condition = stack.pop();
            }

            if (!(condition instanceof BooleanVariable) && !(leftOperand instanceof BooleanLiteral)
                    && !(condition instanceof RelationalOperationExpression) && !(leftOperand instanceof ToBoolean)) {
                String message = String.format("Left operand of ternary operation is not boolean type  near %s",
                        ExpressionTokenizer.toString(tokens, pos, pos - 5));
                logger.error(message);
                throw new IllegalPropertyException(message);
            }

            pos = findNextExpression(tokens, pos + 1, stack, contexts);

            if (!stack.isEmpty() && stack.peek() instanceof AssignOperationExpression) {
                AssignOperationExpression expression = (AssignOperationExpression) stack.peek();
                expression.setRightOperand(this);
            } else {
                stack.push(this);
            }

        }
        return pos;
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
        Stack<Expression> localStack = new Stack<>();
        for (int newPos = pos; newPos < tokens.length; newPos++) {
            String token = tokens[newPos];
            if (token.trim().equals(":")) {
                thenOperand = localStack.pop();
                continue;
            }
            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {

                if (op instanceof RightPriority) {
                    elseOperand = localStack.pop();
                    return newPos;
                } else if (!(op instanceof LeftPriority)) {
                    newPos = op.parse(tokens, newPos, localStack, contexts);
                }

            } else {
                Expression expression = buildExpression(token, contexts, pos, tokens);
                localStack.add(expression);
            }
        }
        throw new ExpressionParseException(getClass().getSimpleName() + " Closing Bracket Not Found at " + pos);

    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {

        boolean result = toBoolean(condition.interpret(instancesBinding));
        if (result) {
            return thenOperand.interpret(instancesBinding);
        } else {
            return elseOperand.interpret(instancesBinding);
        }

    }


    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();

        if (leftOperand != null) {
            toStringBuilder.append(leftOperand.toString());
            toStringBuilder.append(" ");
        }

        if (condition != null) {
            toStringBuilder.append(" ");
            toStringBuilder.append(condition.toString());
            toStringBuilder.append(" ");
        }

        toStringBuilder.append(operationType.getSymbols().get(0));
        toStringBuilder.append(" ");


        if (thenOperand != null) {
            toStringBuilder.append(" (");
            toStringBuilder.append(thenOperand.toString());
            toStringBuilder.append(" ");
        }

        if (elseOperand != null) {
            toStringBuilder.append(": ");
            toStringBuilder.append(elseOperand.toString());
            toStringBuilder.append(") ");
        }


        if (rightOperand != null) {
            toStringBuilder.append(rightOperand.toString());
            toStringBuilder.append(" ");
        }


        return toStringBuilder.toString();
    }
}
