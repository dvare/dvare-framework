package org.dvare.expression.operation.predefined;

import org.apache.commons.lang3.tuple.Pair;
import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.datatype.PairType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.ChainOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.TO_PAIR)
public class ToPair extends ChainOperationExpression {
    private static final Logger logger = LoggerFactory.getLogger(ToPair.class);


    public ToPair() {
        super(OperationType.TO_PAIR);
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
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {

        if (rightOperand.size() == 2) {
            LiteralExpression keyParam = super.interpretOperand(rightOperand.get(0), instancesBinding);
            LiteralExpression valueParam = super.interpretOperand(rightOperand.get(1), instancesBinding);

            Pair pair = Pair.of(keyParam.getValue(), valueParam.getValue());

            return LiteralType.getLiteralExpression(pair, PairType.class);
        }

        return new NullLiteral();
    }


}