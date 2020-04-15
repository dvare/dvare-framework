package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.literal.NullLiteral;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.LAST)
public class Last extends AggregationOperationExpression {
    static Logger logger = LoggerFactory.getLogger(Last.class);


    public Last() {
        super(OperationType.LAST);
    }


    @Override
    public LiteralExpression interpret(InstancesBinding instancesBinding) throws InterpretException {
        List<?> values = extractValues(instancesBinding, leftOperand);
        if (values != null && !values.isEmpty()) {
            return LiteralType.getLiteralExpression(values.get(values.size() - 1), dataTypeExpression);
        }
        return new NullLiteral();
    }


}