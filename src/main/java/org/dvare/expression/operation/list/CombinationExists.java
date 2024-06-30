package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.model.ContextsBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.exceptions.parser.ExpressionParseException;
import org.dvare.expression.Expression;
import org.dvare.expression.ExpressionVisitor;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.operation.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Stack;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.COMB_EXISTS)
public class CombinationExists extends Match {
    private static final Logger logger = LoggerFactory.getLogger(CombinationExists.class);


    public CombinationExists() {
        super(OperationType.COMB_EXISTS);
    }


    @Override
    public Integer parse(String[] tokens, int pos, Stack<Expression> stack, ContextsBinding contextss) throws ExpressionParseException {

        pos = findNextExpression(tokens, pos + 1, stack, contextss);

        computeParam(leftOperand);
        stack.push(this);
        return pos;
    }


    private void computeParam(List<Expression> expressions) throws ExpressionParseException {

        if (expressions == null || expressions.size() != 2) {
            String error = "Comb Exists Operation must have two parameter";
            logger.error(error);
            throw new ExpressionParseException(error);

        }


        if (logger.isDebugEnabled()) {
            logger.debug("OperationExpression Call Expression : {}", getClass().getSimpleName());
        }


    }

    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {

        List<Expression> expressions = this.leftOperand;

        /* values to match */

        Expression valueParam = expressions.get(0);
        List<?> values = buildValues(instancesBinding, valueParam);
        DataType dataType = toDataType(dataTypeExpression);

        /*match params*/
        Expression paramsExpression = expressions.get(1);
        List<?> matchParams = buildMatchParams(instancesBinding, paramsExpression);


        return match(dataType, values, matchParams, false, true);
    }

    @Override
    public <T> T accept(ExpressionVisitor<T> v) {
        return v.visit(this);
    }

}