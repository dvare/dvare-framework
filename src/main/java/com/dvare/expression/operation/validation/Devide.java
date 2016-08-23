package com.dvare.expression.operation.validation;

import com.dvare.annotations.Operation;
import com.dvare.annotations.OperationType;
import com.dvare.expression.datatype.DataType;

@Operation(type = OperationType.VALIDATION, symbols = {"Div", "div", "/"}, dataTypes = {DataType.FloatType, DataType.IntegerType})
public class Devide extends ArithmeticOperationExpression {
    public Devide() {
        super("Div", "div", "/");
    }

    public Devide copy() {
        return new Devide();
    }

}