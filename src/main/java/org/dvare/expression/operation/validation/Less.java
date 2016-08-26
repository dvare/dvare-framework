package org.dvare.expression.operation.validation;

import org.dvare.annotations.OperationType;
import org.dvare.expression.datatype.DataType;

@org.dvare.annotations.Operation(type = OperationType.VALIDATION, symbols = {"lt", "<"}, dataTypes = {DataType.FloatType, DataType.IntegerType, DataType.StringType})
public class Less extends EqualityOperationExpression {
    public Less() {
        super("lt", "<");
    }

    public Less copy() {
        return new Less();
    }


}