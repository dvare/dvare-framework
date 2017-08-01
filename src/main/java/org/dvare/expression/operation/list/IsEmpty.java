package org.dvare.expression.operation.list;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.binding.expression.ExpressionBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.datatype.BooleanType;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Operation(type = OperationType.IS_EMPTY)
public class IsEmpty extends AggregationOperationExpression {
    static Logger logger = LoggerFactory.getLogger(IsEmpty.class);


    public IsEmpty() {
        super(OperationType.IS_EMPTY);
    }


    @Override
    public Object interpret(ExpressionBinding expressionBinding, InstancesBinding instancesBinding) throws InterpretException {
        List<?> values = extractValues(expressionBinding, instancesBinding, leftOperand);

        if (values != null) {
            return LiteralType.getLiteralExpression(values.size() == 0, BooleanType.class);
        }

        return LiteralType.getLiteralExpression(true, BooleanType.class);
    }


}