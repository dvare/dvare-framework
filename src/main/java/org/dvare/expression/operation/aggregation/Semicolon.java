package org.dvare.expression.operation.aggregation;

import org.dvare.annotations.Operation;
import org.dvare.binding.model.TypeBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.operation.AssignOperationExpression;
import org.dvare.expression.operation.OperationType;

import java.util.List;
import java.util.Stack;

@Operation(type = OperationType.COLON)
public class Semicolon extends AssignOperationExpression {
    public Semicolon() {
        super(OperationType.COLON);
    }

    public Semicolon copy() {
        return new Semicolon();
    }

    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding aTypeBinding, TypeBinding vTypeBinding) throws ExpressionParseException {
        pos = parseOperands(tokens, pos + 1, stack, aTypeBinding, vTypeBinding);

        logger.debug("Aggregation OperationExpression Call Expression : {}", getClass().getSimpleName());

        stack.push(this);

        return pos;
    }

    @Override
    public Object interpret(Object aggregation, List<Object> dataSet) throws InterpretException {
        aggregation = leftOperand.interpret(aggregation, dataSet);
        aggregation = rightOperand.interpret(aggregation, dataSet);
        return aggregation;
    }
}