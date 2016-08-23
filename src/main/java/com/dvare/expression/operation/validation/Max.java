package com.dvare.expression.operation.validation;

import com.dvare.annotations.Operation;
import com.dvare.annotations.OperationType;
import com.dvare.expression.datatype.DataType;

@Operation(type = OperationType.VALIDATION, symbols = {"Max", "max"}, dataTypes = {DataType.FloatType, DataType.IntegerType})
public class Max extends ArithmeticOperationExpression {
    public Max() {
        super("Max", "max");
    }

    public Max copy() {
        return new Max();
    }

}