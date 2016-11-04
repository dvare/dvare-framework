package org.dvare.expression.operation.arithmetic;

import org.dvare.annotations.Operation;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.operation.ArithmeticOperationExpression;
import org.dvare.expression.operation.OperationType;

@Operation(type = OperationType.MAX, dataTypes = {DataType.FloatType, DataType.IntegerType})
public class Max extends ArithmeticOperationExpression {
    public Max() {
        super(OperationType.MAX);
    }

    public Max copy() {
        return new Max();
    }

}