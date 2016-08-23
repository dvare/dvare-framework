package com.dvare.expression.operation.validation;

import com.dvare.annotations.Operation;
import com.dvare.annotations.OperationType;
import com.dvare.expression.datatype.DataType;

@Operation(type = OperationType.VALIDATION, symbols = {"le", "<="}, dataTypes = {DataType.FloatType, DataType.IntegerType, DataType.StringType})
public class LessEqual extends EqualityOperationExpression {
    public LessEqual() {
        super("le", "<=");
    }

    public LessEqual copy() {
        return new LessEqual();
    }


}