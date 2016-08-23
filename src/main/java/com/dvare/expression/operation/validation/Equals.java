package com.dvare.expression.operation.validation;

import com.dvare.annotations.Operation;
import com.dvare.annotations.OperationType;

@Operation(type = OperationType.VALIDATION, symbols = {"eq", "="})
public class Equals extends EqualityOperationExpression {
    public Equals() {
        super("eq", "=");
    }

    public Equals copy() {
        return new Equals();
    }

}