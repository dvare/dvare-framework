package org.dvare.expression.operation;


import org.dvare.binding.model.ContextsBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public abstract class ConditionOperationExpression extends OperationExpression {
    protected final static Logger logger = LoggerFactory.getLogger(ConditionOperationExpression.class);

    protected Expression condition = null;
    protected Expression thenOperand = null;
    protected Expression elseOperand = null;

    public ConditionOperationExpression(OperationType operationType) {
        super(operationType);
    }

    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        pos = findNextExpression(tokens, pos + 1, stack, contexts);
        return pos;
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
        for (int i = pos; i < tokens.length; i++) {
            OperationExpression op = configurationRegistry.getOperation(tokens[i]);
            if (op != null) {

                i = op.parse(tokens, i, stack, contexts);
                return i;
            }

        }
        return null;
    }


    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();

        if (leftOperand != null) {
            toStringBuilder.append(leftOperand.toString());
            toStringBuilder.append(" ");
        }

        toStringBuilder.append(operationType.getTokens().get(0));
        toStringBuilder.append(" ");


        if (condition != null) {
            toStringBuilder.append(" ");
            toStringBuilder.append(condition.toString());
            toStringBuilder.append(" ");
        }

        if (thenOperand != null) {
            toStringBuilder.append("THEN ");
            toStringBuilder.append(thenOperand.toString());
            toStringBuilder.append(" ");
        }

        if (elseOperand != null) {
            toStringBuilder.append("ELSE ");
            toStringBuilder.append(elseOperand.toString());
            toStringBuilder.append(" ");
        }


        if (rightOperand != null) {
            toStringBuilder.append(rightOperand.toString());
            toStringBuilder.append(" ");
        }


        return toStringBuilder.toString();
    }

    public Expression getCondition() {
        return condition;
    }

    public void setCondition(Expression condition) {
        this.condition = condition;
    }

    public Expression getThenOperand() {
        return thenOperand;
    }

    public void setThenOperand(Expression thenOperand) {
        this.thenOperand = thenOperand;
    }

    public Expression getElseOperand() {
        return elseOperand;
    }

    public void setElseOperand(Expression elseOperand) {
        this.elseOperand = elseOperand;
    }

}
