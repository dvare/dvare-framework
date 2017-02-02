package org.dvare.expression.operation.arithmetic;

import org.dvare.annotations.Operation;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.operation.ArithmeticOperationExpression;
import org.dvare.expression.operation.OperationType;

@Operation(type = OperationType.DIVIDE, dataTypes = {DataType.FloatType, DataType.IntegerType})
public class Divide extends ArithmeticOperationExpression {
    public Divide() {
        super(OperationType.DIVIDE);
    }


}