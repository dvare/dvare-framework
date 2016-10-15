package org.dvare.expression.operation.arithmetic;

import org.dvare.annotations.OperationType;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.operation.ArithmeticOperationExpression;

@org.dvare.annotations.Operation(type = OperationType.VALIDATION, symbols = {"Min", "min"}, dataTypes = {DataType.FloatType, DataType.IntegerType})
public class Min extends ArithmeticOperationExpression {
    public Min() {
        super("Min", "min");
    }

    public Min copy() {
        return new Min();
    }

}