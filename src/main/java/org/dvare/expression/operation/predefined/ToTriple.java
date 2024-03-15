package org.dvare.expression.operation.predefined;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.TripleType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.dvare.util.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.TO_TRIPLE)
public class ToTriple extends ChainOperationExpression {
    private static final Logger logger = LoggerFactory.getLogger(ToTriple.class);

    public ToTriple() {
        super(OperationType.TO_TRIPLE);
    }

    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contexts) throws ExpressionParseException {
        pos = findNextExpression(tokens, pos + 1, stack, contexts);
        if (logger.isDebugEnabled()) {
            logger.debug("Operation Expression Call Expression : {}", getClass().getSimpleName());
        }
        stack.push(this);
        return pos;
    }

    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {

        if (rightOperand.size() == 3) {
            LiteralExpression<?> leftParam = super.interpretOperand(rightOperand.get(0), instancesBinding);
            LiteralExpression<?> middleParam = super.interpretOperand(rightOperand.get(1), instancesBinding);
            LiteralExpression<?> rightParam = super.interpretOperand(rightOperand.get(2), instancesBinding);

            Triple<?, ?, ?> triple = Triple.of(leftParam.getValue(), middleParam.getValue(), rightParam.getValue());

            return LiteralType.getLiteralExpression(triple, TripleType.class);
        }

        return new NullLiteral<>();
    }

    @Override
    public void accept(ExpressionVisitor v) {
        super.accept(v);
        v.visit(this);
    }

}
