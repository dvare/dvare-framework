package org.dvare.expression.operation.validation;

import org.dvare.annotations.Operation;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.operation.EqualityOperationExpression;
import org.dvare.expression.operation.OperationType;

@Operation(type = OperationType.LESS_EQUAL, dataTypes = {DataType.FloatType, DataType.IntegerType, DataType.StringType, DataType.DateType, DataType.DateTimeType, DataType.SimpleDateType})
public class LessEqual extends EqualityOperationExpression {
    public LessEqual() {
        super(OperationType.LESS_EQUAL);
    }


}