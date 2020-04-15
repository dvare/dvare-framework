package org.dvare.expression.operation.logical;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.literal.BooleanLiteral;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.LogicalOperationExpression;
import org.dvare.expression.operation.OperationType;

import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.NOT)
public class Not extends LogicalOperationExpression {
    public Not() {
        super(OperationType.NOT);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        int i = findNextExpression(tokens, pos + 1, stack, contexts);
        this.rightOperand = stack.pop();
        stack.push(this);
        return i;
    }


    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {
        return new BooleanLiteral(!toBoolean(this.rightOperand.interpret(instancesBinding)));
    }


}