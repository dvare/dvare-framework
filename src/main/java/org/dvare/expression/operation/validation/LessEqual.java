package org.dvare.expression.operation.validation;

import org.dvare.annotations.OperationType;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.operation.EqualityOperationExpression;

@org.dvare.annotations.Operation(type = OperationType.VALIDATION, symbols = {"le", "<="}, dataTypes = {DataType.FloatType, DataType.IntegerType, DataType.StringType, DataType.DateType, DataType.DateTimeType})
public class LessEqual extends EqualityOperationExpression {
    public LessEqual() {
        super("le", "<=");
    }

    public LessEqual copy() {
        return new LessEqual();
    }


}