package org.dvare.expression.operation.arithmetic;

import org.dvare.annotations.Operation;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.operation.ArithmeticOperationExpression;
import org.dvare.expression.operation.OperationType;

@Operation(type = OperationType.ADD, dataTypes = {DataType.FloatType, DataType.IntegerType})
public class Add extends ArithmeticOperationExpression {
    public Add() {
        super(OperationType.ADD);
    }


}