package org.dvare.expression.operation.arithmetic;

import org.dvare.annotations.Operation;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.operation.ArithmeticOperationExpression;
import org.dvare.expression.operation.OperationType;

@Operation(type = OperationType.MIN, dataTypes = {DataType.FloatType, DataType.IntegerType})
public class Min extends ArithmeticOperationExpression {
    public Min() {
        super(OperationType.MIN);
    }

    public Min copy() {
        return new Min();
    }


}