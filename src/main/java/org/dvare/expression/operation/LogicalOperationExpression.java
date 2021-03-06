package org.dvare.expression.operation;


import org.dvare.binding.model.ContextsBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.operation.flow.ENDIF;
import org.dvare.expression.operation.utility.EndForAll;
import org.dvare.expression.operation.utility.RightPriority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
public abstract class LogicalOperationExpression extends OperationExpression {
    protected static Logger logger = LoggerFactory.getLogger(LogicalOperationExpression.class);

    public LogicalOperationExpression(OperationType operationType) {
        super(operationType);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        Expression left = stack.pop();

        pos = findNextExpression(tokens, pos + 1, stack, contexts);

        Expression right = stack.pop();

        this.leftOperand = left;
        this.rightOperand = right;
        if (logger.isDebugEnabled()) {
            logger.debug("OperationExpression Call Expression : {}", getClass().getSimpleName());
        }
        stack.push(this);

        return pos;
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
        for (; pos < tokens.length; pos++) {
            OperationExpression op = configurationRegistry.getOperation(tokens[pos]);
            if (op != null) {

                if (op instanceof RightPriority || op instanceof EndForAll || op instanceof ENDIF/* || (op instanceof AggregationOperationExpression && !stack.isEmpty())*/) {
                    return pos - 1;
                }


                pos = op.parse(tokens, pos, stack, contexts);


                if (pos + 1 < tokens.length) {
                    OperationExpression testOp = configurationRegistry.getOperation(tokens[pos + 1]);
                    if (testOp instanceof LogicalOperationExpression) {
                        return pos;
                    }
                }


            }
        }
        return pos;
    }


}