package org.dvare.expression.operation.validation;

import org.dvare.annotations.Operation;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.operation.EqualityOperationExpression;
import org.dvare.expression.operation.OperationType;

@Operation(type = OperationType.GREATER, dataTypes = {DataType.FloatType, DataType.IntegerType, DataType.StringType, DataType.DateType, DataType.DateTimeType})
public class Greater extends EqualityOperationExpression {
    public Greater() {
        super(OperationType.GREATER);
    }


}