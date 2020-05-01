package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.datatype.BooleanType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author Muhammad Hammad
 * @since 2016-06-30
 */
@Operation(type = OperationType.NOT_EMPTY)
public class NotEmpty extends AggregationOperationExpression {
    static Logger logger = LoggerFactory.getLogger(NotEmpty.class);


    public NotEmpty() {
        super(OperationType.NOT_EMPTY);
    }


    @Override
    public LiteralExpression<?> interpret(InstancesBinding instancesBinding) throws InterpretException {
        List<?> values = extractValues(instancesBinding, leftOperand);

        if (values != null) {
            return LiteralType.getLiteralExpression(values.size() != 0, BooleanType.class);
        }
        return LiteralType.getLiteralExpression(false, BooleanType.class);
    }


}