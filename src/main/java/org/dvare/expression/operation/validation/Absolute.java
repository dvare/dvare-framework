package org.dvare.expression.operation.validation;


import org.dvare.annotations.Operation;
import org.dvare.annotations.OperationType;

@Operation(type = OperationType.VALIDATION, symbols = {"Abs", "abs"})
public class Absolute extends ArithmeticOperationExpression {
    public Absolute() {
        super("Abs", "abs");
    }

    public Absolute copy() {
        return new Absolute();
    }

}