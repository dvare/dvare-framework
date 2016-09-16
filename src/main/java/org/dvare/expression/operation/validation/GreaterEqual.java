package org.dvare.expression.operation.validation;


import org.dvare.annotations.OperationType;
import org.dvare.expression.datatype.DataType;

@org.dvare.annotations.Operation(type = OperationType.VALIDATION, symbols = {"ge", ">="}, dataTypes = {DataType.FloatType, DataType.IntegerType, DataType.StringType, DataType.DateType, DataType.DateTimeType})
public class GreaterEqual extends EqualityOperationExpression {
    public GreaterEqual() {
        super("ge", ">=");
    }

    public GreaterEqual copy() {
        return new GreaterEqual();
    }


}