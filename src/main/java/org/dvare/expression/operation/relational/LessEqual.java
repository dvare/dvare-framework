package org.dvare.expression.operation.relational;

import org.dvare.annotations.Operation;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.operation.OperationType;
import org.dvare.expression.operation.RelationalOperationExpression;

@Operation(type = OperationType.LESS_EQUAL, dataTypes = {DataType.FloatType, DataType.IntegerType, DataType.StringType, DataType.DateType, DataType.DateTimeType, DataType.SimpleDateType})
public class LessEqual extends RelationalOperationExpression {
    public LessEqual() {
        super(OperationType.LESS_EQUAL);
    }


}