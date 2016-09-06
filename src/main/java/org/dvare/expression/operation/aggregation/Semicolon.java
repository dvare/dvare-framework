package org.dvare.expression.operation.aggregation;

import org.dvare.annotations.Operation;
import org.dvare.annotations.OperationType;
import org.dvare.binding.model.TypeBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;

import java.util.List;
import java.util.Stack;

@Operation(type = OperationType.AGGREGATION, symbols = {";"})
public class Semicolon extends AssignOperationExpression {
    public Semicolon() {
        super(";");
    }

    public Semicolon copy() {
        return new Semicolon();
    }

    @Override
    public int parse(String[] tokens, int pos, Stack<Expression> stack, TypeBinding aTypeBinding, TypeBinding vTypeBinding) throws ExpressionParseException {
        pos = parseOperands(tokens, pos + 1, stack, aTypeBinding, vTypeBinding);

        logger.debug("Aggregation Operation Call Expression : {}", getClass().getSimpleName());

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