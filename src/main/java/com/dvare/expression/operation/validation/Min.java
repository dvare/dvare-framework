package com.dvare.expression.operation.validation;

import com.dvare.annotations.Operation;
import com.dvare.annotations.OperationType;
import com.dvare.expression.datatype.DataType;

@Operation(type = OperationType.VALIDATION, symbols = {"Min", "min"}, dataTypes = {DataType.FloatType, DataType.IntegerType})
public class Min extends ArithmeticOperationExpression {
    public Min() {
        super("Min", "min");
    }

    public Min copy() {
        return new Min();
    }

}