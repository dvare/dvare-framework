package org.dvare.expression.operation;


import org.dvare.binding.model.ContextsBinding;
import org.dvare.binding.model.TypeBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.utility.LeftPriority;
import org.dvare.expression.operation.utility.RightPriority;
import org.dvare.expression.veriable.VariableType;
import org.dvare.util.TypeFinder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public abstract class ChainOperationExpression extends OperationExpression {

    protected List<Expression> rightOperand = new ArrayList<>();

    public ChainOperationExpression(OperationType operationType) {
        super(operationType);
    }

    private int parseOperands(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {

        String token = tokens[pos - 1];
        pos = pos + 1;


        if (stack.isEmpty()) {


            TokenType tokenType = findDataObject(token, contexts);

            if (tokenType.type != null && contexts.getContext(tokenType.type) != null && TypeFinder.findType(tokenType.token, contexts.getContext(tokenType.type)) != null) {

                TypeBinding typeBinding = contexts.getContext(tokenType.type);
                DataType variableType = TypeFinder.findType(tokenType.token, typeBinding);
                this.leftOperand = VariableType.getVariableExpression(tokenType.token, variableType, tokenType.type);

            } else {

                this.leftOperand = LiteralType.getLiteralExpression(token);
            }


        } else {
            this.leftOperand = stack.pop();


        }

        return pos;
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        pos = parseOperands(tokens, pos, stack, contexts);
        pos = findNextExpression(tokens, pos + 1, stack, contexts);
        if (logger.isDebugEnabled()) {
            logger.debug("Operation Expression Call Expression : {}", getClass().getSimpleName());
        }
        stack.push(this);
        return pos;
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {

        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
        Stack<Expression> localStack = new Stack<>();
        for (int newPos = pos; newPos < tokens.length; newPos++) {
            String token = tokens[newPos];
            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null) {

                if (op instanceof RightPriority) {
                    this.rightOperand = new ArrayList<>(localStack);
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
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder();

        if (leftOperand != null) {
            toStringBuilder.append(leftOperand.toString());
            toStringBuilder.append(" -> ");
        }

        toStringBuilder.append(operationType.getTokens().get(0));


        if (rightOperand != null) {
            toStringBuilder.append("(");
            Iterator<Expression> expressionIterator = rightOperand.iterator();
            while (expressionIterator.hasNext()) {
                Expression expression = expressionIterator.next();
                toStringBuilder.append(expression.toString());
                if (expressionIterator.hasNext()) {
                    toStringBuilder.append(", ");
                }
            }
            toStringBuilder.append(")");
            toStringBuilder.append(" ");


        } else {
            toStringBuilder.append(" ");
        }

        return toStringBuilder.toString();
    }

    public List<Expression> getRightListOperand() {
        return rightOperand;
    }

    public void setRightListOperand(List<Expression> rightOperand) {
        this.rightOperand = rightOperand;
    }

}