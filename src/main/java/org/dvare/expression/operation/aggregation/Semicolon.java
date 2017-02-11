package org.dvare.expression.operation.aggregation;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.operation.AssignOperationExpression;
import org.dvare.expression.operation.OperationType;

import java.util.Stack;

@Operation(type = OperationType.COLON)
public class Semicolon extends AssignOperationExpression {
    public Semicolon() {
        super(OperationType.COLON);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        pos = super.parse(tokens, pos + 1, stack, contexts);

        logger.debug("Aggregation OperationExpression Call Expression : {}", getClass().getSimpleName());

        /*stack.push(this);*/

        return pos;
    }

    @Override
    public Object interpret(InstancesBinding instancesBinding) throws InterpretException {
        instancesBinding = (InstancesBinding) leftOperand.interpret(instancesBinding);
        instancesBinding = (InstancesBinding) rightOperand.interpret(instancesBinding);
        return instancesBinding;
    }


}