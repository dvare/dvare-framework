package com.dvare.expression.operation.validation;

import com.dvare.annotations.Operation;
import com.dvare.annotations.OperationType;
import com.dvare.expression.datatype.DataType;

@Operation(type = OperationType.VALIDATION, symbols = {"lt", "<"}, dataTypes = {DataType.FloatType, DataType.IntegerType, DataType.StringType})
public class Less extends EqualityOperationExpression {
    public Less() {
        super("lt", "<");
    }

    public Less copy() {
        return new Less();
    }


}