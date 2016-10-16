package org.dvare.expression.operation.arithmetic;

import org.dvare.annotations.OperationType;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.operation.ArithmeticOperationExpression;

@org.dvare.annotations.Operation(type = OperationType.VALIDATION, symbols = {"Add", "add", "+"}, dataTypes = {DataType.FloatType, DataType.IntegerType})
public class Add extends ArithmeticOperationExpression {
    public Add() {
        super("Add", "add", "+");
    }

    public Add copy() {
        return new Add();
    }

}