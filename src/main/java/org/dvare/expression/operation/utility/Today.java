package org.dvare.expression.operation.utility;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.config.ConfigurationRegistry;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.DateLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;

import java.time.LocalDate;
import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.TO_DAY)
public class Today extends OperationExpression {


    public Today() {
        super(OperationType.TO_DAY);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {

        pos = findNextExpression(tokens, pos + 1, stack, contexts);

        stack.push(this);


        return pos;
    }


    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        ConfigurationRegistry configurationRegistry = ConfigurationRegistry.INSTANCE;
        for (int i = pos; i < tokens.length; i++) {
            String token = tokens[i];
            OperationExpression op = configurationRegistry.getOperation(token);
            if (op != null && op.getClass().equals(RightPriority.class)) {
                return i;
            }
        }
        return null;
    }


    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        LocalDate date = LocalDate.now();
        return new DateLiteral(date);
    }


}