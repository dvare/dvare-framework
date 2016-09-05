package org.dvare.expression.operation.validation;

import org.dvare.annotations.OperationType;
import org.dvare.expression.datatype.DataType;

@org.dvare.annotations.Operation(type = OperationType.VALIDATION, symbols = {"gt", ">"}, dataTypes = {DataType.FloatType, DataType.IntegerType, DataType.StringType, DataType.DateType, DataType.DateTimeType})
public class Greater extends EqualityOperationExpression {
    public Greater() {
        super("gt", ">");
    }

    public Greater copy() {
        return new Greater();
    }

}