package org.dvare.expression.operation.flow;

import org.dvare.annotations.Operation;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.operation.ConditionOperationExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;

import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.THEN)
public class THEN extends ConditionOperationExpression {

    public THEN() {
        super(OperationType.THEN);
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        var oldPos = pos;
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
        for (; pos < tokens.length; pos++) {
            var token = tokens[pos];
            OperationExpression op = configurationRegistry.getOperation(token);
            /*if (op != null) {

                pos = op.parse(tokens, pos, stack, contexts);
                return pos;
            }*/

            if (op instanceof ELSE || op instanceof ENDIF) {
                checkSingleNonOperationExpression(tokens, oldPos, pos, stack, contexts);

                if (stack.isEmpty()) {
                    throw new ExpressionParseException("ELSE or ENDIF not found");
                }

                return --pos;
            } else if (op != null) {
                pos = op.parse(tokens, pos, stack, contexts);
            }
        }
        return pos;
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }

}