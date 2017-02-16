package org.dvare.expression.operation.aggregation;

import org.dvare.annotations.Operation;
import org.dvare.binding.data.InstancesBinding;
import org.dvare.exceptions.interpreter.InterpretException;
import org.dvare.expression.datatype.IntegerType;
import org.dvare.expression.literal.LiteralExpression;
import org.dvare.expression.literal.LiteralType;
import org.dvare.expression.operation.AggregationOperationExpression;
import org.dvare.expression.operation.OperationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Operation(type = OperationType.LENGTH)
public class Length extends AggregationOperationExpression {
    static Logger logger = LoggerFactory.getLogger(Length.class);


    public Length() {
        super(OperationType.LENGTH);
    }


    @Override
    public Object interpret(InstancesBinding instancesBinding) throws InterpretException {

        List<Object> dataSet = (List) instancesBinding.getInstance("data");

        LiteralExpression literalExpression = LiteralType.getLiteralExpression(dataSet.size(), IntegerType.class);

        return literalExpression;
    }


}