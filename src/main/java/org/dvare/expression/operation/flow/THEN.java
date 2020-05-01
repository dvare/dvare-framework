package org.dvare.expression.operation.flow;

import org.dvare.annotations.Operation;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
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
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
        for (; pos < tokens.length; pos++) {
            OperationExpression op = configurationRegistry.getOperation(tokens[pos]);
            /*if (op != null) {

                pos = op.parse(tokens, pos, stack, contexts);
                return pos;
            }*/

            if (op instanceof ELSE || op instanceof ENDIF) {
                if (stack.isEmpty()) {
                    throw new ExpressionParseException("ENDIF not found");
                }
                return --pos;
            } else if (op != null) {
                pos = op.parse(tokens, pos, stack, contexts);
            }

        }
        return null;
    }


}