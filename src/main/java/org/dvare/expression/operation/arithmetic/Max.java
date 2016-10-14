package org.dvare.expression.operation.arithmetic;

import org.dvare.annotations.OperationType;
import org.dvare.expression.datatype.DataType;
import org.dvare.expression.operation.validation.ArithmeticOperationExpression;

@org.dvare.annotations.Operation(type = OperationType.VALIDATION, symbols = {"Max", "max"}, dataTypes = {DataType.FloatType, DataType.IntegerType})
public class Max extends ArithmeticOperationExpression {
    public Max() {
        super("Max", "max");
    }

    public Max copy() {
        return new Max();
    }

}