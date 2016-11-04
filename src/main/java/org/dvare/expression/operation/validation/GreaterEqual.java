package org.dvare.expression.operation.validation;


import org.dvare.annotations.Operation;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.operation.EqualityOperationExpression;
import org.dvare.expression.operation.OperationType;

@Operation(type = OperationType.GREATER_EQUAL, dataTypes = {DataType.FloatType, DataType.IntegerType, DataType.StringType, DataType.DateType, DataType.DateTimeType})
public class GreaterEqual extends EqualityOperationExpression {
    public GreaterEqual() {
        super(OperationType.GREATER_EQUAL);
    }

    public GreaterEqual copy() {
        return new GreaterEqual();
    }


}