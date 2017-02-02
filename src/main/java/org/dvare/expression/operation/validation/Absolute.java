package org.dvare.expression.operation.validation;


import org.dvare.annotations.Operation;
import org.dvare.expression.operation.ArithmeticOperationExpression;
import org.dvare.expression.operation.OperationType;

@Operation(type = OperationType.ABSOLUTE)
public class Absolute extends ArithmeticOperationExpression {
    public Absolute() {
        super(OperationType.ABSOLUTE);
    }


}