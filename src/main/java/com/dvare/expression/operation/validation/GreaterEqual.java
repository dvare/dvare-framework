package com.dvare.expression.operation.validation;


import com.dvare.annotations.Operation;
import com.dvare.annotations.OperationType;
import com.dvare.expression.datatype.DataType;

@Operation(type = OperationType.VALIDATION, symbols = {"ge", ">="}, dataTypes = {DataType.FloatType, DataType.IntegerType, DataType.StringType})
public class GreaterEqual extends EqualityOperationExpression {
    public GreaterEqual() {
        super("ge", ">=");
    }

    public GreaterEqual copy() {
        return new GreaterEqual();
    }


}