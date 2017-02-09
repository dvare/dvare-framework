package org.dvare.expression.operation.validation;

import org.dvare.annotations.Operation;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.operation.OperationExpression;
import org.dvare.expression.operation.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

@Operation(type = OperationType.END_FOREACH)
public class EndForEach extends OperationExpression {
    private static Logger logger = LoggerFactory.getLogger(EndForEach.class);


    public EndForEach() {
        super(OperationType.END_FOREACH);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        return pos;
    }

    @Override
    public Integer findNextExpression(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        return pos;
    }
}