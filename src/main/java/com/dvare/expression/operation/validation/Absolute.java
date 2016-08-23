package com.dvare.expression.operation.validation;


import com.dvare.annotations.Operation;
import com.dvare.annotations.OperationType;

@Operation(type = OperationType.VALIDATION, symbols = {"Abs", "abs"})
public class Absolute extends ArithmeticOperationExpression {
    public Absolute() {
        super("Abs", "abs");
    }

    public Absolute copy() {
        return new Absolute();
    }

}