package org.dvare.expression.operation.validation;

import org.dvare.annotations.OperationType;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.operation.EqualityOperationExpression;

@org.dvare.annotations.Operation(type = OperationType.VALIDATION, symbols = {"lt", "<"}, dataTypes = {DataType.FloatType, DataType.IntegerType, DataType.StringType, DataType.DateType, DataType.DateTimeType})
public class Less extends EqualityOperationExpression {
    public Less() {
        super("lt", "<");
    }

    public Less copy() {
        return new Less();
    }


}